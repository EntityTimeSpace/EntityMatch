package nlp.dep;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import triple.Triple;
import triple.*;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * 依存树结点
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class DepNode {
	private TypedDependency dep = null;
	private List<DepNode> children = null;
	private String reln = null;
	private DepNode parent = null;
	String name = null;
	
	/**
	 * 构造函数
	 * @param dep 依存关系{@link nlp.Parser}
	 */
	public DepNode(TypedDependency dep) {
		this(dep, null);
	}
	
	/**
	 * 构造函数
	 * @param dep 依存关系{@link nlp.Parser}
	 * @param parent 该结点的父结点
	 */
	public DepNode(TypedDependency dep, DepNode parent) {
		this.dep = dep;
		this.parent = parent;
		this.reln = dep.reln().getShortName();
		this.name = dep.dep().nodeString();
	}
	
	public String getName() {
//		return dep.dep().nodeString();
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getIndex() {
		return dep.dep().index();
	}
	
	public DepNode getParent() {
		return parent;
	}
	
	public String getReln() {
		return reln;
	}
	
	public void setReln(String reln) {
		this.reln = reln;
	}
	
	public TypedDependency getDep() {
		return dep;
	}
	
	public List<DepNode> getChildren() {
		return children;
	}
	public List<DepNode> getChildren(String dep) {
		List<DepNode> res = new ArrayList<DepNode>();
		for (DepNode child : children) {
			if ( child.getReln().equals(dep) )
				res.add(child);
		}
		return res;
		
	}
	
	public DepNode getFirstChild() {
		return ( children.size() > 0 ) ? children.get(0) : null;
	}
	
	/**
	 * 获取首个依存关系为dep的子结点
	 * @param dep 依存关系
	 * @return 首个依存关系为dep的子结点
	 */
	public DepNode getFirstChild(String dep) {
		for (DepNode child : children) {
			if ( child.getReln().equals(dep) )
				return child;
		}
		return null;
	}
	
	/**
	 * 获取所有依存关系为dep的子结点
	 * @param dep 依存关系
	 * @return 依存关系为dep的子结点
	 */
	public void setChildren(List<DepNode> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return dep.toString();
	}
	
	/**
	 * 获取依存关系为“修饰关系”的子结点
	 * @return 依存关系为“修饰关系”的子结点
	 */
	public List<DepNode> getModifiers() {
		return getNodes(
				"nn", "advmod", "amod", "rcmod",
				"assmod", "tmod", "plmod", "mmod", "ordmod", 
				"neg", "comod", "vmod", "prtmod", "dvpmod", 
				"prnmod", "prep","pobj"//dmy修改,增加"pobj"
		);
	}
	
	/**
	 * 获取依存关系为“和”的子结点
	 * @param nodes 待获取对应子结点的结点集合
	 * @return 依存关系为“和”的子结点
	 */
	public static List<DepNode> getConjNodes(List<DepNode> nodes) {
		List<DepNode> conjNodes = new ArrayList<DepNode>();
		for (DepNode node : nodes) {
			conjNodes.addAll(node.getChildren("conj"));
		}
		return conjNodes;
	}
	
	/**
	 * 获取依存关系满足relns的子节点，并取出其中的“和”结点
	 * @param relns 依存关系集合
	 * @return 依存关系满足relns的子节点，以及它们的“和”结点
	 */
	private List<DepNode> getNodes(String ... relns) {
		ArrayList<DepNode> nodes = (ArrayList<DepNode>) getNodeHeads(relns);
		nodes.addAll(getConjNodes(getNodeHeads(relns)));
		return nodes;
	}
	
	/**
	 * 获取依存关系满足relns的子节点
	 * @param relns 依存关系集合
	 * @return 依存关系满足relns的子节点
	 */
	private List<DepNode> getNodeHeads(String ... relns) {
		List<DepNode> nodes = new ArrayList<DepNode>();
		for (String reln : relns) {
			List<DepNode> children = getChildren(reln);
			nodes.addAll(children);
		}
		return nodes;
	}
	
	/**
	 * 获取依存关系为“主语”的子结点，并取出其中的“和”结点
	 * @return 依存关系为“主语”的子结点，以及它们的“和”结点
	 */
	public List<DepNode> getSubjects() {
		return getNodes("nsubj", "xsubj", "top", "nsubjpass");
	}
	
	/**
	 * 获取依存关系为“宾语”的子结点
	 * @return 依存关系为“宾语”的子结点
	 */
	public List<DepNode> getPredicateObjectHeads() {
		List<DepNode> res =  getNodeHeads(
				"dobj", "pobj", "range", "attr", "dep",
				"ccomp", "pccomp", "lccomp", "rcomp");
		for (DepNode conjNode : getNodeHeads("conj", "ccomp", "pcomp", "lcomp", "rcomp")) {
			res.addAll(conjNode.getPredicateObjectHeads());
		}
		return res;
	}
	
	/**
	 * 获取依存关系为“宾语”的子结点，并取出其中的“和”结点
	 * @return 依存关系为“宾语”的子结点，以及它们的“和”结点
	 */
	public List<DepNode> getObjects() {
		return getNodes("dobj", "pobj", "range", "attr", "dep");
	}
	
	

	/**
	 * 根据词的实体类型集合types，获取以当前结点为中心的“谓语-宾语”对
	 * @param types 词的实体类型集合
	 * @return 以当前结点为中心的“谓语-宾语”对
	 */
	public List<Entry<DepNode, DepNode>> getPredicateObjects(List<Entry<String, String>> types) {
		/* 获取谓语集 */
		DepNode predicateHead = getParent();
		List<DepNode> predicates = new ArrayList<DepNode>();
		predicates.add(predicateHead);
		predicates.addAll(predicateHead.getChildren("conj"));

		/* 获取宾语集 */
		DepNode objectHead = this;
		List<DepNode> objects = new ArrayList<DepNode>();
		objects.add(objectHead);
		objects.addAll(objectHead.getChildren("conj"));
		
		List<Entry<DepNode, DepNode>> res = new ArrayList<Entry<DepNode, DepNode>>();
		/* 调整谓语、宾语（原宾语可能是子句的谓语，子句的主语可能是主句的谓语） */
		if ( getReln().equals("dep") && types.get(getIndex() - 1).getValue().equals("Undefined") ||
				Arrays.asList("ccomp", "pccomp", "lccomp", "rcomp").contains(getReln()) ) {
			List<DepNode> subObjects = getObjects();
			if ( subObjects.size() > 0 ) {
				List<DepNode> subSubjects = getSubjects();
				if ( subSubjects.size() > 0 ) {
					predicates = subSubjects;
				} else {
					predicates = objects;
				}
				objects = subObjects;
			}
			
			//dmy add
			List<DepNode> timeAndplace =  getNodes("tmod", "prep");
			if(timeAndplace.size() > 0){
				DepNode predicate = null;
				DepNode object = null;
				for(DepNode depNode : timeAndplace){
					if(depNode.getReln().equals("tmod")){
						predicate = new DepNode(this.getDep());
						predicate.setChildren(getChildren());
						predicate.setName(predicate.getName()+"时间");
						object = depNode;
					}else if(depNode.getReln().equals("prep")){
						predicate = new DepNode(this.getDep());
						predicate.setName(predicate.getName()+"地点");
						predicate.setChildren(getChildren());
						object = depNode.getNodes("pobj").get(0);
					}
					
					res.add(new SimpleEntry<DepNode, DepNode>(predicate, object));
//					res.add(new SimpleEntry<Node, DepNode>(new ImplicitPredicate(predicate.toString()+"时间"), object));
				}
				
				
				return res;
			}

			//dmy add
		}
		
		/* 组合谓语、宾语 */
		for (DepNode predicate : predicates) {
			for (DepNode object : objects) {
				res.add(new SimpleEntry<DepNode, DepNode>(predicate, object));
			}
		}
		return res;
	}
	
	/**
	 * 获取数量关系
	 * @return “数-量”对
	 */
	public Entry<Node, Node> getQuantity() {
		DepNode clfNode = getFirstChild("clf");
		DepNode nummodNode = getFirstChild("nummod");
		if ( clfNode != null ) {
			DepNode nummod = clfNode.getFirstChild("nummod");
			if ( nummod != null )
				return new AbstractMap.SimpleEntry<Node, Node>(
						nummod.toTrunkNode(), clfNode.toTrunkNode()
				);
		} else if ( nummodNode != null ) {
			return new AbstractMap.SimpleEntry<Node, Node>(
					nummodNode.toTrunkNode(), toTrunkNode() 
			);
		}
		return null;
	}
	
	/**
	 * 从当前结点出发，获取谓词-量词对
	 * @param predicate 谓词结点
	 * @return predicate, name, quantity 列表
	 */
	public List<Entry<Entry<Node, DepNode>, Entry<Node, Node>>> 
						getPredicateQuantities(DepNode predicate) {
		List<Entry<Entry<Node, DepNode>, Entry<Node, Node>>> res = 
				new ArrayList<Entry<Entry<Node, DepNode>, Entry<Node, Node>>>();
		Entry<Node, Node> quantity = getQuantity();
		
		if ( quantity != null ) {
			res.add(new SimpleEntry<Entry<Node, DepNode>, Entry<Node, Node>>(
					new SimpleEntry<Node, DepNode>(predicate.toTrunkNode(), this),
					quantity
			));
			
		} else if ( getReln().equals("dep") ) {
			List<DepNode> subDeps = getObjects();
			for (DepNode subDep : subDeps) {
				res.addAll(subDep.getPredicateQuantities(this));
			}
		}
		return res;
	}
	
	/**
	 * 获取“属性-值”对
	 * @param concernDEP 标记是否考虑dep关系（dep在主句中用于发现子句谓词，在子句中用于发现子句实体属性）
	 * @return “属性-值”对
	 */
	private List<Entry<Node, DepNode>> getAttrValues(boolean concernDEP) {
		List<Entry<Node, DepNode>> attrValues = new ArrayList<Entry<Node, DepNode>>();
		/* 获取所有修饰词 */
		List<DepNode> attrs = getModifiers();
		if ( concernDEP ) {
			List<DepNode> depNodes = getChildren("dep");
			attrs.addAll(depNodes);
			attrs.addAll(getConjNodes(depNodes));
		}
		
		for (DepNode attr : attrs) {
			/* 挖出简单修饰关系和介宾修饰关系 */
			List<DepNode> objs = attr.getObjects();
			List<DepNode> plmods = attr.getChildren("plmod");
			for (DepNode plmod : plmods) {
				objs.addAll(plmod.getObjects());
			}
			/*List<DepNode> subPreps = attr.getChildren("prep");
			for (DepNode subPrep : subPreps) {
				objs.addAll(subPrep.getObjects());
			}
			for (DepNode obj : objs) {
				attrValues.add(new SimpleEntry<Node, DepNode>(attr.toTrunkNode(), obj));
			}
			
			if ( objs.size() == 0 ) {
				attrValues.add(new SimpleEntry<Node, DepNode>(
						attr.getReln().equals("tmod") ? 
//								new TrunkNode(getIndex() - 1, getName() + "时间") :
								new ImplicitPredicate("时间") :
								new ImplicitPredicate("性质"), 
						attr
				));
			}*/
		}
		return attrValues;
	}
	
	/**
	 * 根据词的实体类型types，找出当前实体的真实名字（有时候，修饰词才是真实的名字）
	 * @param types 词的实体类型
	 * @return 当前实体的真实名字所代表的依存结点
	 */
	private DepNode getRealNameNode(List<Entry<String, String>> types) {//若当前节点从属词不为Undefined,则返回当前节点，否则查找子节点中第一个从属词不为Undefined的节点
		//获取修饰关系子节点
		List<DepNode> attrs = getModifiers();
		attrs.addAll(getNodes("dep"));//获取其他未知关系
		
		if ( types.get(getIndex() - 1).getValue().equals("Undefined") ) {
			for (int i = 0; i < attrs.size(); i ++) {
				if ( !types.get(attrs.get(i).getIndex() - 1).getValue().equals("Undefined") ) {
					return attrs.get(i);
				}
			}
		}
		return this;
	}
	
	/**
	 * 以当前词为中心，分析其作为谓词时，的关联三元组
	 * @return 三元组
	 */
	public List<Triple> analyzePredicate(DepTree depTree) {
		List<Triple> triples = new ArrayList<Triple>();
		triples.addAll(analyzePlace(depTree));
		
		List<Entry<Node, DepNode>> attrValues = getAttrValues(false);
		for (Entry<Node, DepNode> attrValue : attrValues) {
			triples.add(new Triple(
					this.toTrunkNode(), 
					attrValue.getKey(), 
					attrValue.getValue().toTrunkNode()
			));
		}
		return triples;
	}
	
	/**
	 * 以当前词为中心，分析其作为主/宾语时，的关联三元组
	 * @param depTree 相对应的依存树{@link DepTree}
	 * @return 真实名字和三元组
	 */
	public Entry<Node, List<Triple>> analyzeEntry(DepTree depTree) {
		List<Triple> triples = new ArrayList<Triple>();
		/* 揪出当前实体的真实名字 */
		DepNode realNameNode = getRealNameNode(depTree.getTypes());
		System.out.println("realNameNode=========="+realNameNode);
		List<Entry<Node, DepNode>> attrValues = getAttrValues(true);
		System.out.println(attrValues.size());
		if ( !equals(realNameNode) ) {
			attrValues.add(new SimpleEntry<Node, DepNode>(
					new ImplicitPredicate("类型"), this
			));
		}
		
		for (Entry<Node, DepNode> attrValue : attrValues) {
			Entry<Node, Node> quantity = attrValue.getValue().getQuantity();
			
			if ( !realNameNode.toTrunkNode().equals(attrValue.getValue().toTrunkNode()) ) {
				if ( quantity == null )
					triples.add(new Triple(
							realNameNode.toTrunkNode(), 
							attrValue.getKey(), 
							attrValue.getValue().toTrunkNode()
					));
				else
					triples.addAll(depTree.analyzeQuantity(
							quantity, 
							attrValue.getValue().toTrunkNode(),
							realNameNode.toTrunkNode(), 
							attrValue.getKey(), 
							attrValue.getValue()
					));
			}
			for (DepNode node : realNameNode.getChildren("conj")) {
				if ( quantity == null )
					triples.add(new Triple(
							node.toTrunkNode(), 
							attrValue.getKey(), 
							attrValue.getValue().toTrunkNode()
					));
				else
					triples.addAll(depTree.analyzeQuantity(
							quantity, 
							attrValue.getValue().toTrunkNode(),
							node.toTrunkNode(), 
							attrValue.getKey(), 
							attrValue.getValue()
					));
			}
		}
		//System.out.println("TrunkNode==="+realNameNode.toTrunkNode().toJSCreateString());
		return new SimpleEntry<Node, List<Triple>>(realNameNode.toTrunkNode(), triples);
	}
	
	/**
	 * 分析当前结点的位置信息，求得相关三元组
	 * @param depTree 该结点所在的{@link DepTree}
	 * @return 相关的三元组
	 */
	private List<Triple> analyzePlace(DepTree depTree) {
		List<Triple> res = new ArrayList<Triple>();
		Set<DepNode> positions = getPosition(depTree);
		for (DepNode position : positions) {
			int blankNodeId = depTree.newBlankNodeId();
			res.add(new Triple(
					toTrunkNode(), 
					new ImplicitPredicate("地点"), 
					new BlankNode(blankNodeId)
			));
			res.add(new Triple(
					new BlankNode(blankNodeId), 
					new ImplicitPredicate("方位"), 
					new TrunkNode(position.getIndex() - 1, position.getName())
			));
			for (DepNode locNode : position.getNodes("lobj")) {
				res.add(new Triple(
						new BlankNode(blankNodeId), 
						new ImplicitPredicate("名字"), 
						new TrunkNode(locNode.getIndex() - 1, locNode.getName())
				));
			}
		}
		return res;
	}
	
	/**
	 * 获取当前结点的有关位置的描述结点
	 * @param depTree 该结点所在的{@link DepTree}
	 * @return 有关位置的描述结点
	 */
	private Set<DepNode> getPosition(DepTree depTree) {
		Set<DepNode> res = new HashSet<DepNode>();
		res.addAll(getNodes("loc"));
		for (DepNode depNode : getNodes("dep")) {
			if ( depTree.getTagWords().get(depNode.getIndex() - 1).equals("LC") ) {
				res.add(depNode);
			}
		}
		return res;
	}
	
	/**
	 * 把依存结点转换为三元组结点中的{@link triple.node.TrunkNode}
	 * @return {@link triple.node.TrunkNode}
	 */
	public Node toTrunkNode() {
		return new TrunkNode(getIndex() - 1, getName());//getName()返回从属词名字
	}
	
	@Override
	public boolean equals(Object obj) {
		return toTrunkNode().equals(((DepNode) obj).toTrunkNode());
	}
	
	@Override
	public int hashCode() {
		return getIndex();
	}
}
