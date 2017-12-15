from pygithub3 import Github
import getpass, os

# Very Simple GitHub Access ,didn't play to much in this as the library does most of the work
username = raw_input("GitHub username: ")
password = getpass.getpass("GitHub password: ")
g = Github(username, password)

os.system("clear")
print("username " + username + "\n")
print("Public repos: " + str(g.get_user().public_repos))
print("Disk usage used" + str(g.get_user().disk_usage))
for repo in g.get_user().get_repos():
    print "Repo:" + str(repo.name) 
    print "Last Uworked on " + str(repo.pushed_at)

    