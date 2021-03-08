import java.util.ArrayList;
import java.util.List;

public class Node {
		public Node(String val) {
			this.val= val;
		}
		public Node(String val, boolean isLeaf) {
			this.val = val;
			this.leaf= isLeaf;
		}
		public Node(String val, String column, String predicate, boolean isLeaf) {
			this.val = val;
			this.leaf= isLeaf;
			this.predicate = predicate;
			this.featureName = column;
		}
		public String getVal() {
			return val;
		}
		public void setVal(String val) {
			this.val = val;
		}
		public List<Node> getChildren() {
			return children;
		}
		public void setLeft(List<Node> children) {
			this.children = children;
		}
		public void addChild(Node child) {
			children.add(child);
		}
		public String toString() {
			return val + " " + (leaf ? "LEAF" : "NODE");
		}
		public String getFeatureName() {
			return featureName;
		}
		public void setFeatureName(String featureName) {
			this.featureName = featureName;
		}
		public String getPredicate() {
			return predicate;
		}
		public void setPredicate(String predicate) {
			this.predicate = predicate;
		}
		public boolean isLeaf() {
			return leaf;
		}
		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		String val;
		boolean leaf;
		String featureName;
		String predicate;
		List<Node> children = new ArrayList<Node>();
	}
