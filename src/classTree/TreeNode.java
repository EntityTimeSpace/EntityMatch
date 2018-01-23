/**
 * 
 */
package classTree;

import org.json.simple.JSONObject;

/**
 * @author "Cunxin Jia"
 *
 */
public class TreeNode implements Comparable<TreeNode>{
	private String label;
	private String uri;
	private ClassTree subTree;
	
	
	
	/**
	 * @param label
	 * @param uri
	 */
	public TreeNode(String label, String uri) {
		this.label = label;
		this.uri = uri;
		this.subTree = null;
	}
	/**
	 * @param label
	 * @param uri
	 * @param subTree
	 */
	public TreeNode(String label, String uri, ClassTree subTree) {
		this.label = label;
		this.uri = uri;
		this.subTree = subTree;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the url
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param url the uri to set
	 */
	public void setUrl(String uri) {
		this.uri = uri;
	}
	/**
	 * @return the subTree
	 */
	public ClassTree getSubTree() {
		return subTree;
	}
	/**
	 * @param subTree the subTree to set
	 */
	public void setSubTree(ClassTree subTree) {
		this.subTree = subTree;
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("label", label);
		json.put("uri", uri);			
		if(subTree != null) {
			String subTreeJSONString = subTree.toString();
			json.put("subTree", subTree);
		}
		return json.toString();
	}
	/* 两个对象排序时使用的方法，根据树的子节点大小进行升序排序
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TreeNode o) {
		int thisNum = 0;
		int thatNum = 0;
		if(subTree != null) {
			thisNum = subTree.getNodes().size();
		}
		if(o != null && o.getSubTree() != null) {
			thatNum = o.getSubTree().getNodes().size();
		}
		return thatNum - thisNum;
	}
}


