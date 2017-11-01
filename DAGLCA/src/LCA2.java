import java.util.*;

public class LCA2 {
		public Node root;
		
		//creating the root of the graph
		public void makeroot(int data,char ident){
			Node root=new Node();
			root.data=data;
			root.ident=ident;
			root.parents = new ArrayList<Node>();
			root.children = new ArrayList<Node>();
			this.root=root;
		}

