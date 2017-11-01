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
	//Test for a false root this test should return
	//@Test
	//public void testfalseroot() {
	//	LCA2 graph = new LCA2();
	//	graph.makeroot(1, 'a');
	//	graph.addNode('b', 2,graph.root);
	//	assertEquals("Return root", 'b', DAGLCATEST.returnident(graph.root));
	//}
	//Returns False +1
	@Test
	//Basic Add test
	public void testAdd(){
		LCA2 graph = new LCA2();
		graph.makeroot(1,'a');
		graph.addNode('b',2,graph.root);
		assertEquals("Add 1st Node",'b',DAGLCATEST.returnident(graph.root.children.get(0)));		
		graph.addNode('c', 3, graph.root.children.get(0));
		assertEquals("Add second Node",'c',DAGLCATEST.returnident(graph.root.children.get(0).children.get(0)));
		graph.addNode('d',4,graph.root);
		assertEquals("Add thrid",'d',DAGLCATEST.returnident(graph.root.children.get(1)));	
	}
	//Test add when node doesn't exist  
	//@Test
	//public void testfalseAdd(){
	//	LCA2 graph = new LCA2();
	//	graph.makeroot(1,'a');
	//	assertEquals("Add 1st Node",'b',DAGLCATEST.returnident(graph.root.children.get(0)));
	//	
	//}
	//Returns False ,aswell if you think about it adding to itself like the root adding the root it will not interfere with the program working
	
	
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
	//Testing for Node when the Node isnt there
	//@Test
	//public void testfalseSearch(){
	//	LCA2 graph = new LCA2();
	//	graph.makeroot(1,'a');
	//	assertEquals("Search root",'b',DAGLCATEST.returnident(graph.SearchNode('b')));
	//}
	//Returns False

	
	//My DAG for these next test 
	//.............a1............
	//.........../....\...........
	//........b2......c3........
	//......./..\.../...\
	//.....d4...e5.......f5
	
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
		//Next test really show my implementation works it test the LCA of e5 and f5 where e5 is both connected to b2 and c3 ,test should return the c3 Node and it does.
		graph.addNode('e', 5, graph.root.children.get(1));
		graph.addNode('f', 5, graph.root.children.get(1));
		assertEquals("Check LCA in DAG", 'c', DAGLCATEST.returnident(graph.SearchLCA(graph.root, graph.root.children.get(1).children.get(0), graph.root.children.get(1).children.get(1))));
	}
	
	//@Test
	//public void testfalseLCA(){
	//	LCA2 graph = new LCA2();
	//	graph.makeroot(1,'a');
	//	graph.addNode('b', 2, graph.root);
	//	graph.addNode('c', 3, graph.root);
	//	assertEquals("Check LCA in DAG", 'b', DAGLCATEST.returnident(graph.SearchLCA(graph.root, graph.root.children.get(0), graph.root.children.get(1))));
	//	graph.addNode('d', 4, graph.root.children.get(0));
	//	graph.addNode('e', 5, graph.root.children.get(0));
	//	assertEquals("Check LCA in DAG", 'd', DAGLCATEST.returnident(graph.SearchLCA(graph.root, graph.root.children.get(0).children.get(0), graph.root.children.get(0).children.get(1))));
	//}Returns false
	//Returns False , checked for false positives
}
