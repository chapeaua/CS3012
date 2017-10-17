import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import org.junit.Test;

public class LowestCommonAncestorTest {


    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
      new LowestCommonAncestor();
    }
	
	
	//~ Check that the implementation works with a null case ........................................................
	@Test
	public void TestNullCases() {
		assertEquals("LCA Failed with 3 null nodes ",null ,LowestCommonAncestor.LCA(null,null,null));
	}
	//No implementation of the BST as it wasn t in the brief just manually creating a BST for test purposes that you can see below
   // Take a common Binary Search Tree for all cases checking the LCA class
   // .....20........................................................
   //...8.......22...........
   //4....12..21...23
   //...10..14
   //Node root=new Node(20);
   //root.left=new Node(8);
   //root.left.left=new Node(4);
   //root.left.right=new Node(12);
   //root.left.right.left=new Node(10);
   //root.left.right.right=new Node(14);
   //root.right=new Node(22);
   //root.right.left=new Node(21);
   //root.right.right=new Node(23);
	
	@Test
	public void TestLCA(){
		   Node root=new Node(20);
		   root.left=new Node(8);
		   root.left.left=new Node(4);
		   root.left.right=new Node(12);
		   root.left.right.left=new Node(10);
		   root.left.right.right=new Node(14);
		   root.right=new Node(22);
		   root.right.left=new Node(21);
		   root.right.right=new Node(23);
		   //Basically this assertsequals the LCA of 4 and 12 ,where the expected result is 8
		   Node expectedResult=root.left;
		   //Basically this assertsequals the LCA of 21 and 21,where the expected result is 22 
		   Node expectedResult1=root.right;
		   //AssertEquals the LCA of 10 and 14 where the result is not directly to the left or right of the root
		   Node expectedResult2=root.left.right;
		   assertEquals("LCA test when result is on the left of the root",expectedResult,LowestCommonAncestor.LCA(root,root.left.left,root.left.right));
		   assertEquals("LCA test when result is on the right of the root",expectedResult1,LowestCommonAncestor.LCA(root,root.right.left,root.right.right));
		   assertEquals("LCA test when result is not a direct child of the root",expectedResult2,LowestCommonAncestor.LCA(root,root.left.right.left,root.left.right.right));
	}
	@Test
	public void TestNode(){
	
	}
	
}