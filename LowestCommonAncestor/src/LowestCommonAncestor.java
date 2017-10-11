public class LowestCommonAncestor {
	public static  Node LCA(Node root, Node n1, Node n2) {
		if (root == null) {
			return null;
		}
		
		if (root.data > n1.data && root.data > n2.data) {
			return LCA(root.left, n1, n2);

		}
		
		else if (root.data <= n1.data && root.data < n2.data) {
			return LCA(root.right, n1, n2);
		}
		
		return root;
	}

}

class Node {
	int data;
	Node left;
	Node right;

	public Node(int data) {
		this.data = data;
		left = null;
		right = null;
	}
}