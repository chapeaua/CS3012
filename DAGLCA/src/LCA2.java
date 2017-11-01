import java.util.*;

public class LCA2 {
		public Node root;
		
		//creating the root of the 
		public void makeroot(int data,char ident){
			Node root=new Node();
			root.data=data;
			root.ident=ident;
			root.parents = new ArrayList<Node>();
			root.children = new ArrayList<Node>();
			this.root=root;
		}
		//Search method to find a set node using his identity
		public Node SearchNode(char ident){
			Node result=new Node() ;
			if(ident==root.ident){return root;}
			else{
				for(int i=0;i<root.children.size();i++){
					result=SearchNode(ident,root.children.get(i));
				}
				if(result!=null){return result;}			
			}
			return null;
		}
		private Node SearchNode(char ident,Node n1){
			Node result;
			if(ident==n1.ident){return n1;}
			else{
				for(int i=0;i < n1.children.size();i++){
					result=SearchNode(ident,n1.children.get(i));
					if(result !=null){return result;}	
				}
			}
			return null;
		}
	//adding a set node with a identity char ,data ,and his parent	
		public void addNode(char ident, int data,Node parent){
			Node Node = new Node();
			Node.ident = ident;
			Node.children = new ArrayList<Node>();
			Node.data = data;
			Node.parents.add(parent);
			parent.children.add(Node);
		}
		

}
