package triple;

/**
 * 三元组的结点
 * {@link BlankNode}，{@link TrunkNode}，{@link ImplicitPredicate}
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public abstract class Node {
	
	protected Node() {}
	
	/**
	 * 生成Javascript的结点创建代码
	 * @return Javascript的结点创建代码
	 */
	public abstract String toJSCreateString();
}
