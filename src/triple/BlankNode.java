package triple;

import java.io.Serializable;


/**
 * 空白结点
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class BlankNode extends Node implements Serializable {
	private static final long serialVersionUID = 3297587238877858484L;
	
	private int id;
	
	/**
	 * 构造函数
	 * @param id 下标
	 */
	public BlankNode(int id) {
		this.id = id;
	}
	
	/**
	 * 拷贝构造函数
	 * @param node 拷贝对象
	 */
	public BlankNode(BlankNode node) {
		this.id = node.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "BlankNode-" + id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof BlankNode && ((BlankNode) obj).id == id;
	}

	@Override
	public String toJSCreateString() {
		return "Node(BLANK_NODE," + id + ",'')";
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
