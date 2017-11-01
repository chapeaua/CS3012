import static org.junit.Assert.*;

import org.junit.Test;

public class DAGLCATEST {

//Functions for testing 
	
		//Function for testing the node class
		public static int returndata(Node n1){
			return n1.data;
			
		}
		public static char returnident(Node n1){
			return n1.ident;
			
		}
		
	
	
	@Test
	public void testroot() {
		LCA2 graph = new LCA2();
		graph.makeroot(1, 'a');
		assertEquals("Return root", 'a', DAGLCATEST.returnident(graph.root));
	}
	
	@Test
	public void testAdd(){
		LCA2 graph = new LCA2();
		graph.makeroot(1,'a');
		graph.addNode('b',2,graph.root);
		assertEquals("Add 1st Node",'b',DAGLCATEST.returnident(graph.root.children.get(0)));		
		graph.addNode('c', 3, graph.root.children.get(0));
		assertEquals("Add secnd Node",'c',DAGLCATEST.returnident(graph.root.children.get(0).children.get(0)));
		graph.addNode('d',4,graph.root);
		assertEquals("Add thrid",'d',DAGLCATEST.returnident(graph.root.children.get(1)));	
	}
	
	@Test
	public void testSearch(){
		LCA2 graph = new LCA2();
		graph.makeroot(1,'a');
		assertEquals("Search root",'a',DAGLCATEST.returnident(graph.SearchNode('a')));
		graph.addNode('b',2,graph.root);
		assertEquals("Search new Node",'b',DAGLCATEST.returnident(graph.SearchNode('b')));
		graph.addNode('c',2,graph.root.children.get(0));
		assertEquals("Search last Node",'c',DAGLCATEST.returnident(graph.SearchNode('c')));
	}
	
	@Test
	public void testLCA(){
		LCA2 graph = new LCA2();
		graph.makeroot(1,'a');
		graph.addNode('b', 2, graph.root);
		graph.addNode('c', 3, graph.root);
		assertEquals("Check LCA in DAG", 'a', DAGLCATEST.returnident(graph.SearchLCA(graph.root, graph.root.children.get(0), graph.root.children.get(1))));
		graph.addNode('d', 4, graph.root.children.get(0));
		graph.addNode('e', 5, graph.root.children.get(0));
		assertEquals("Check LCA in DAG", 'b', DAGLCATEST.returnident(graph.SearchLCA(graph.root, graph.root.children.get(0).children.get(0), graph.root.children.get(0).children.get(1))));
	}
}
