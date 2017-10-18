import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {
	//Function for testing the node class
	public static int NodetestFunctionfordata(Node n1){
		return n1.data;
		
	}
	public static Node NodetestFunctionforleft(Node n1){
		return n1.left;
		
	}
	public static Node NodetestFunctionforright(Node n1){
		return n1.right;
		
	}
	
	
	
	
	@Test
	public void TestNode(){
		   Node root=new Node(20);
		   int expectedResult=20;
		   Node expectedNodeLeft=root.left;
		   Node expectedNodeRight=root.left;
		   assertEquals("Testing the Node class data attribute",expectedResult,NodeTest.NodetestFunctionfordata(root)); 
		   assertEquals("Testing the Node class left attribute",expectedNodeLeft,NodeTest.NodetestFunctionforleft(root));
		   assertEquals("Testing the Node class right attribute",expectedNodeRight,NodeTest.NodetestFunctionforright(root));
	}

}
