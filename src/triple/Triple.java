package triple;

import java.io.Serializable;
import java.util.List;

/**
 * 三元组
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class Triple implements Serializable {
	private static final long serialVersionUID = 152280310199243630L;
	
	private Node subject;
	private Node predicate;
	private Node object;
	
	public Triple(Node subject, Node predicate, Node object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public Node getSubject() {
		return subject;
	}
	
	public Node getPredicate() {
		return predicate;
	}
	
	public Node getObject() {
		return object;
	}
	
	/**
	 * 获取最大的空白结点下标，用于在页面中创建空白结点集
	 * @return 获取最大的空白结点下标
	 */
	public int getMaxBlankNodeId() {
		int maxId = -1;
		if ( subject instanceof BlankNode && maxId < ((BlankNode) subject).getId() )
			maxId = ((BlankNode) subject).getId();
		if ( predicate instanceof BlankNode && maxId < ((BlankNode) subject).getId() )
			maxId = ((BlankNode) predicate).getId();
		if ( object instanceof BlankNode && maxId < ((BlankNode) object).getId() )
			maxId = ((BlankNode) object).getId();
		return maxId;
	}
	
	/**
	 * 根据基地址计算{@link triple.node.TrunkNode}和{@link triple.node.BlankNode}的index
	 * @param baseTrunkIndex {@link triple.node.TrunkNode}的基地址
	 * @param baseBlankId {@link triple.node.BlankNode}的基地址
	 */
	public void addBaseAddress(int baseTrunkIndex, int baseBlankId) {
		if ( subject instanceof TrunkNode )
			((TrunkNode) subject).setTrunkIndex(((TrunkNode) subject).getTrunkIndex() + baseTrunkIndex);
		if ( subject instanceof BlankNode )
			((BlankNode) subject).setId(((BlankNode) subject).getId() + baseBlankId);
		if ( predicate instanceof TrunkNode )
			((TrunkNode) predicate).setTrunkIndex(((TrunkNode) predicate).getTrunkIndex() + baseTrunkIndex);
		if ( predicate instanceof BlankNode )
			((BlankNode) predicate).setId(((BlankNode) predicate).getId() + baseBlankId);
		if ( object instanceof TrunkNode )
			((TrunkNode) object).setTrunkIndex(((TrunkNode) object).getTrunkIndex() + baseTrunkIndex);
		if ( object instanceof BlankNode )
			((BlankNode) object).setId(((BlankNode) object).getId() + baseBlankId);
	}
	
	/**
	 * 获取triples中最大的{@link triple.node.BlankNode}索引
	 * @param triples 三元组集
	 * @return 三元组集中最大的{@link triple.node.BlankNode}索引
	 */
	public static int getMaxBlankNodeId(List<Triple> triples) {
		int maxId = -1;
		for (Triple triple : triples) {
			int subMaxId = triple.getMaxBlankNodeId();
			if ( subMaxId > maxId ) {
				maxId = subMaxId;
			}
		}
		return maxId;
	}
	
	/**
	 * 根据基地址计算{@link triple.node.TrunkNode}和{@link triple.node.BlankNode}的index
	 * @param triples 三元组集
	 * @param baseTrunkIndex {@link triple.node.TrunkNode}的基地址
	 * @param baseBlankId {@link triple.node.BlankNode}的基地址
	 */
	public static void addBaseAddress(
			List<Triple> triples, int baseTrunkIndex, int baseBlankId) {
		for (Triple triple : triples) {
			triple.addBaseAddress(baseTrunkIndex, baseBlankId);
		}
	}
	
	/**
	 * 分裂所有的{@link triple.node.Node}
	 */
	public void detachNodes() {
		subject = NodeFactory.copy(subject);
		predicate = NodeFactory.copy(predicate);
		object = NodeFactory.copy(object);
	}
	
	/**
	 * 分裂三元组集中所有的{@link triple.node.Node}
	 * @param triples 三元组集
	 */
	public static void detachNodes(List<Triple> triples) {
		for (Triple triple : triples) {
			triple.detachNodes();
		}
	}
	
	@Override
	public String toString() {
		return "subject:" + subject.toString() + ", " +
				"predicate:" + predicate.toString() + ", " +
				"object:" + object.toString()+"\n";
	}
	
	/**
	 * 生成Javascript的结点创建代码
	 * @return Javascript的结点创建代码
	 */
	public String toJSCreateString() {
		return "Triple(" + subject.toJSCreateString() + "," +
							predicate.toJSCreateString() + "," +
							object.toJSCreateString() + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		return subject.equals(((Triple) obj).subject) &&
				predicate.equals(((Triple) obj).predicate) &&
				object.equals(((Triple) obj).object);
	}
}
