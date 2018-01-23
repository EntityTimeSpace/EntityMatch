package triple;

import java.io.Serializable;

/**
 * Trunk结点
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class TrunkNode extends Node implements Serializable {
	private static final long serialVersionUID = 489594859923921589L;
	
	private int trunkIndex;
	private String alias;
	
	/**
	 * 构造函数
	 * @param trunkIndex 下标
	 * @param alias 别名
	 */
	public TrunkNode(int trunkIndex, String alias) {
		this.trunkIndex = trunkIndex;
		this.alias = alias;
	}
	
	/**
	 * 拷贝构造函数
	 * @param node 拷贝对象
	 */
	public TrunkNode(TrunkNode node) {
		this.trunkIndex = node.trunkIndex;
		this.alias = node.alias;
	}
	
	/**
	 * 构造函数
	 * @param trunkIndex 下标
	 */
	public void setTrunkIndex(int trunkIndex) {
		this.trunkIndex = trunkIndex;
	}
	
	public int getTrunkIndex() {
		return trunkIndex;
	}
	
	@Override
	public String toString() {
		return alias;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof TrunkNode && trunkIndex == ((TrunkNode) obj).trunkIndex;
	}

	@Override
	public String toJSCreateString() {
		return "Node(TRUNK_NODE," + trunkIndex + ",'" + alias + "')";
	}
	
	@Override
	public int hashCode() {
		return trunkIndex;
	}
}
