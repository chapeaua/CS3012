
from __future__ import division
import six
import os
import sys
import errno
import tempfile
import os.path as path
import argparse
from copy import deepcopy
from threading import Thread
from io import BytesIO
from six.moves import queue
from six.moves.urllib.request import urlopen
from pygithub3 import Github
from pygraphviz import AGraph
from PIL import Image, ImageDraw

SUPPORTED_INPUT_FORMATS = ['dot']
AVATAR_DOWNLOADING_PARALLEL_LEVEL = 10

def processarguments():

    class _NoPassword: pass
    class _NoToken: pass
    parser = argparse.ArgumentParser(
        description=__doc__,
        formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument(
        '-u', '--username')
    parser.add_argument(
        '-p', '--password', nargs='?',
        default=_NoPassword)
    parser.add_argument(
        '-t', '--token', metavar='TOKEN', nargs='?',
        default=_NoToken)
    parser.add_argument(
        '-if', '--input-format', choices=SUPPORTED_INPUT_FORMATS,)
    parser.add_argument(
        '-o', '--output', type=argparse.FileType('wb'), required=True,)
    parser.add_argument(
        '-of', '--output-format')
    parser.add_argument(
        '--orgs', metavar='ORGANIZATION', nargs='*', default=[],
        help='organizations to start fetching data with')
    parser.add_argument(
        '--users', metavar='USERNAME', nargs='*', default=[])
    parser.add_argument(
        '-na', '--no-avatars', action='store_false', dest='avatars')
    parser.add_argument(
        '-fg', '--full-graph', action='store_true')
    arguments = parser.parse_args()
    if arguments.username and arguments.password is _NoPassword:
        parser.error('password should be specified')
    if arguments.password is not _NoPassword and arguments.username is None:
        parser.error('username should be specified')
    if arguments.password is not _NoPassword and arguments.token is not _NoToken:
        parser.error('password and token cannot be used together')
    if arguments.output is sys.stdout and not arguments.output_format:
        parser.error('output format not found')
    if arguments.output and not arguments.output_format:
        arguments.output_format = path.splitext(arguments.output.name)[1][1:]
    if not arguments.orgs and not arguments.users:
        parser.error('no USER OR ORGS  provided')
    if arguments.password is _NoPassword:
        arguments.password = None
    else:
        if arguments.password is None:
            arguments.password = raw_input('Enter password: ')
    if arguments.token is _NoToken:
        arguments.token = None
    else:
        if arguments.token is None:
            arguments.token = raw_input('Enter token: ')
    return arguments
def savespace(graph_data):
   
    graph_data = deepcopy(graph_data)
    for username, info in six.iteritems(graph_data):
        info['followers'] = [
            f
            for f in info.get('followers', [])
            if 'followers' in graph_data.get(f, {})
        ]
        info['following'] = [
            f
            for f in info.get('following', [])
            if 'followers' in graph_data.get(f, {})
        ]
    return graph_data
def GettingData(options):
    def get_or_set(username):
        try:
            info = graph_data[username]
        except KeyError:
            graph_data[username] = info = {}
        return info
    github = Github(
        login=options.username, password=options.password,
        token=options.token)

    graph_data = {}
    usernames = set(options.users)
    sys.stderr.write('Getting Data.\n')
    for org_name in set(options.orgs):
        users = github.orgs.members.list_public(org_name).all()
        for user in users:
            usernames.add(user.login)
    for username in usernames:
        followers = github.users.followers.list(username).all()
        following = github.users.followers.list_following(username).all()
        info = get_or_set(username)
        info['followers'] = [f.login for f in followers]
        info['following'] = [f.login for f in following]
    if not options.full_graph:
        graph_data = savespace(graph_data)
    if options.avatars:
        for username, info in list(six.iteritems(graph_data)):
            info['avatar_url'] = github.users.get(username).avatar_url
            if options.full_graph:
                for f in set(info['followers'] + info['following']):
                    f_info = get_or_set(f)
                    if 'avatar_url' in f_info:
                        continue
                    f_info['avatar_url'] = github.users.get(f).avatar_url
    sys.stderr.write('complete.')
    return graph_data


def get_avatars_cache_dir():
    return path.join(tempfile.gettempdir(), 'github-social-graph')
def get_avatar_path(node):
    return path.join(get_avatars_cache_dir(), '{}.png'.format(node))
def create_graph(graph_data, input_format, avatars):
    def add_node(node):
        attrs = {
            'label': node,
            'shape': 'circle',
            'margin': 0,
            'color': '#6baed6',
            'fontcolor': '#993333',
            'fontsize': 25,
        }
        if avatars:
            if 'avatar_url' in graph_data.get(node, {}):
                attrs['image'] = get_avatar_path(node)
                attrs['label'] = ''
                attrs['xlabel'] = node
                attrs['width'] = 60 / 96
                attrs['height'] = 60 / 96
                attrs['fixedsize'] = 'true'
        graph.add_node(node, **attrs)

    graph_attrs = {
        'directed': True,
        'dpi': 96,
        'background': '#0033cc',
    }
    graph = AGraph(graph_data, **graph_attrs)
    return graph
def download_avatars(graph_data):
    def is_cached(username):
        avatar_path = get_avatar_path(username)
        if path.exists(avatar_path):
            return True
    def mkdirp(dirname):
        try:
            os.makedirs(dirname)
        except OSError as exc:
            if exc.errno == errno.EEXIST and path.isdir(dirname):
                pass
            else:
                raise
    def download(urlsq, res):
        while True:
            try:
                url, username = urlsq.get_nowait()
            except queue.Empty:
                return
            try:
                data = urlopen(url).read()
                res.append((data, username))
            finally:
                urlsq.task_done()
    mkdirp(get_avatars_cache_dir())
    urls_usernames = [
        (info['avatar_url'], username)
        for username, info in six.iteritems(graph_data)
        if 'avatar_url' in info and not is_cached(username)]
    if not urls_usernames:
        return
    urlsq = queue.Queue()
    for u in urls_usernames:
        urlsq.put(u)
    res = []
    threads_num = min(AVATAR_DOWNLOADING_PARALLEL_LEVEL, len(urls_usernames))
    for _ in six.moves.range(threads_num):
        Thread(target=download, args=(urlsq, res)).start()
    urlsq.join()
    for data, username in res:
        with open(get_avatar_path(username), 'wb') as fh:
            fh.write(process_avatar(data))
def process_avatar(data):

    image = Image.open(BytesIO(data))
    width, height = image.size
    mask = Image.new('RGBA', (width, height), (255, 255, 255, 0))
    draw = ImageDraw.Draw(mask)
    draw.ellipse([0, 0, width, height], fill=(255,255,255,255))
    mask.paste(image, mask=mask)
    image = mask
    image.thumbnail((60, 60), Image.ANTIALIAS)
    output = BytesIO()
    image.save(output, 'PNG')
    return output.getvalue()
def draw_graph(graph, output, format):
    graph.draw(output, format=format, prog='dot', args='-q')

def main():
    options = processarguments()

    
    graph_data = GettingData(options)

    
    graph = create_graph(graph_data, options.input_format, options.avatars)
    if options.output_format == 'dot':
        graph.write(options.output)
    else:
        if options.avatars:
            download_avatars(graph_data)
            draw_graph(graph, options.output, options.output_format)
    if options.output is not sys.stdout:
        options.output.close()



if __name__ == '__main__':
    main()
