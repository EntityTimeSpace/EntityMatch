package triple;

/**
 * 三元组结点工厂{@link Node}
 * @author Da Huang (dhuang.cn@gmail.com)
 * 
 */
public class NodeFactory {
	
	static final public int BLANK_NODE = 0;
	static final public int IMPLICIT_PREDICATE = 1;
	static final public int TRUNK_NODE = 2;
	
	static private NodeFactory instance = null;
	
	private NodeFactory() {}
	
	public static NodeFactory getInstance() {
		if ( instance == null )
			instance = new NodeFactory();
		return instance;
	}
	
	/**
	 * 创建空白结点{@link BlankNode}
	 * @param index 空白结点的下标
	 * @return 以index为下标的空白结点
	 */
	public BlankNode createBlankNode(int index) {
		return new BlankNode(index);
	}
	
	/**
	 * 创建隐含词结点{@link ImplicitPredicate}
	 * @param name 隐含词
	 * @return 隐含词结点
	 */
	public ImplicitPredicate createImplicitPredicate(String name) {
		return new ImplicitPredicate(name);
	}
	
	/**
	 * 创建Trunk结点{@link TrunkNode}
	 * @param index Trunk结点的下标
	 * @param alias Trunk结点的别名
	 * @return Trunk结点
	 */
	public TrunkNode createtTrunkNode(int index, String alias) {
		return new TrunkNode(index, alias);
	}
	
	/**
	 * 拷贝结点node
	 * @param node 待拷贝对象
	 * @return 拷贝得到的对象
	 */
	public static Node copy(Node node) {
		if ( node instanceof TrunkNode )
			return new TrunkNode((TrunkNode) node);
		else if ( node instanceof ImplicitPredicate )
			return new ImplicitPredicate((ImplicitPredicate) node);
		else if ( node instanceof BlankNode )
			return new BlankNode((BlankNode) node);
		else 
			return null; 
	}
	
	/**
	 * 创建一个三元组结点
	 * @param type 结点类型
	 * @param entryIndex {@link BlankNode}或{@link TrunkNode}的下标
	 * @param alias {@link TrunkNode}的别名或{@link ImplicitPredicate}的下标
	 * @return 三元组结点
	 */
	public Node createNode(String type, String entryIndex, String alias) {
		Node node = null;
		int typeInt = Integer.parseInt(type);
		int index = -1;
		switch (typeInt) {
			case BLANK_NODE:
				index = Integer.parseInt(entryIndex);
				node = new BlankNode(index);
				break;
				
			case IMPLICIT_PREDICATE:
				node = new ImplicitPredicate(alias);
				break;
				
			case TRUNK_NODE:
				index = Integer.parseInt(entryIndex);
				node = new TrunkNode(index, alias);
				break;
				
			default: break;
		}
		return node;
	}
}
