package triple;

import java.io.Serializable;

/**
 * 隐含词结点(三元组中的隐含词)
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class ImplicitPredicate extends Node implements Serializable {
	private static final long serialVersionUID = 500525703008557001L;
	
	private String name;
	
	/**
	 * 构造函数
	 * @param name 词名字
	 */
	public ImplicitPredicate(String name) {
		this.name = name;
	}
	
	/**
	 * 拷贝构造函数
	 * @param node 拷贝对象
	 */
	public ImplicitPredicate(ImplicitPredicate node) {
		this.name = node.name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ImplicitPredicate && name.equals((String) obj);
	}

	@Override
	public String toJSCreateString() {
		return "Node(IMPLICIT_PREDICATE,'','" + name + "')";
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
