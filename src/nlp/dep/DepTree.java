package nlp.dep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import nlp.Parser;
import triple.Triple;
import triple.BlankNode;
import triple.ImplicitPredicate;
import triple.Node;
import triple.TrunkNode;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.TypedDependency;


/**
 * 依存树
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class DepTree {
	
	private DepNode root = null;
	private List<Entry<String, String>> types = null;
	private List<TaggedWord> tagWords = null;
	private int blankNodeId = 0;
	
	/**
	 * 构造函数
	 * @param deps 依存关系{@link Parser}
	 * @param types 词的类型{@link annotate.Annotate}
	 * @param tagWords 词性{@link nlp.Tagger}
	 */
	public DepTree(List<TypedDependency> deps, 
			List<Entry<String, String>> types,
			List<TaggedWord> tagWords) {
		this.types = types;
		this.tagWords = tagWords;
		
		for (TypedDependency dep : deps) {
			//System.out.println("shortName========"+dep.reln().getShortName());
			if ( dep.reln().getShortName().equals("root") ) {
				root = new DepNode(dep);
				break;
			}
		}
		
		/* 用宽度优先搜索，从依存关系构造依存树 */
		Queue<DepNode> queue = new LinkedList<DepNode>();
		queue.add(root);
		Set<Integer> visited = new HashSet<Integer>();
		if(root == null) return;
		visited.add(root.getIndex());//标记从属词或受支配词,getIndex()返回从属词在原始分词中的序号,based on 1
		while ( !queue.isEmpty() ) {
			DepNode currentNode = queue.poll();
			//System.out.println("序号："+currentNode.toString()+"-----"+currentNode.getIndex() +types.get(currentNode.getIndex() - 1).getValue() ); 
			//if ( tagWords.get(currentNode.getIndex() - 1).tag().equals("NT") ||types.get(currentNode.getIndex() - 1).getValue().equals("http://ws.nju.edu.cn/nju28/Time")) {
			if ( types.get(currentNode.getIndex() - 1).getValue().equals("http://ws.nju.edu.cn/nju28/Time")) {	
				
				/* 时间修正 */
				currentNode.setReln("tmod");
				System.out.println("从属词为时间======"+currentNode.toString()+"=="+currentNode.getReln());
				
			}else if(types.get(currentNode.getIndex() - 1).getValue().equals("http://ws.nju.edu.cn/nju28/Space")){
				/*地点名词,dmy增加*/
				currentNode.setReln("pobj");
			} else if ( currentNode.getReln().equals("nummod") && 
					!tagWords.get(currentNode.getIndex() - 1).tag().equals("CD") ) {
				/* 判别伪数字修饰 */
//				System.out.println(currentNode);
				currentNode.setReln("nn");
			} else if ( currentNode.getReln().equals("dep") && 
					tagWords.get(currentNode.getIndex() - 1).tag().equals("CD") ) {
				/* 修正为数字 */
				currentNode.setReln("nummod");
			}
			//System.out.println("序号后 ："+currentNode.toString()+"-----"+currentNode.getIndex()+currentNode.getReln()  ); 
//			System.out.println(currentNode);
			List<TypedDependency> relatives = getChildrenDependencies(currentNode.getDep(), deps, visited);
			List<DepNode> children = new ArrayList<DepNode>();
			for (TypedDependency relative : relatives) {
				DepNode child = new DepNode(relative, currentNode);
				queue.add(child);
				visited.add(child.getIndex());
				children.add(child);
			}
			currentNode.setChildren(children);
		}
	}
	

	
	
	/**
	 * 获取依存关系中，root的孩子
	 * @param root 待获取子结点的结点
	 * @param deps 依存关系集
	 * @param visited 访问过的点集合
	 * @return root的孩子
	 */
	private static List<TypedDependency> getChildrenDependencies(
					TypedDependency root, List<TypedDependency> deps, Set<Integer> visited) {
		List<TypedDependency> res = new ArrayList<TypedDependency>();
		for (TypedDependency dep : deps) {
			if ( root.dep().equals(dep.gov()) && !visited.contains(dep.dep().index()) ) {
				res.add(dep);
			}
		}
		return res;
	}
	
	/**
	 * 从当前依存树提取三元组
	 * @return 三元组集
	 */
	public List<Triple> analyze() {
		List<Triple> res = new ArrayList<Triple>();//创建存放最终结果三元组的三元组列表
		
		/* 获取主语，并分析主语 */
		if(root==null) return null;
		List<DepNode> subjects = root.getSubjects();//获取动词的所有主语,包括并列主语,conj,nsubj....从根开始寻找
		List<Node> realNameSubjects = new ArrayList<Node>();//真实名字主语
		for (DepNode subject : subjects) {
			System.out.println("------------------------");
			Entry<Node, List<Triple>> subjectAndTriples = subject.analyzeEntry(this);//分析主语关联的三元组
			System.out.println("------------------------");
			realNameSubjects.add(subjectAndTriples.getKey());
			res.addAll(subjectAndTriples.getValue());
		}
		if ( realNameSubjects.size() == 0 )
			realNameSubjects.add(new ImplicitPredicate(""));
		
		/* 获取谓宾头 */
		List<DepNode> heads = root.getPredicateObjectHeads();
		System.out.println("谓宾头 ====="+heads.toString());
		for (DepNode head : heads) {
			/* 获取谓宾关系 */
			List<Entry<DepNode, DepNode>> predicateObjects = head.getPredicateObjects(types);

			for (Entry<DepNode, DepNode> predicateObject : predicateObjects) {
				/* 分析谓语 */
				res.addAll(predicateObject.getKey().analyzePredicate(this));
				System.out.println("谓宾关系========"+predicateObject.toString());
				/* 获取数量关系 */
				List<Entry<Entry<Node, DepNode>, Entry<Node, Node>>> predicateQuantities = 
						predicateObject.getValue().getPredicateQuantities(predicateObject.getKey());

				/* 分析宾语 */
				Entry<Node, List<Triple>> nodeAndTriples = predicateObject.getValue().analyzeEntry(this);
				res.addAll(nodeAndTriples.getValue());
				
				/* 把谓宾对拼成三元组 */
				for (Node subject : realNameSubjects) {
					if ( predicateQuantities.size() > 0 ) {
						for (Entry<Entry<Node, DepNode>, Entry<Node, Node>> 
										predicateQuantity : predicateQuantities) {
							res.addAll(analyzeQuantity(
									predicateQuantity.getValue(), 
									nodeAndTriples.getKey(), 
									subject, 
									predicateQuantity.getKey().getKey(), 
									predicateQuantity.getKey().getValue()
							));
						}
						System.out.println("-----------有数量关系----------");
					} else {
						/* 无数量关系 */
						
						res.add(new Triple(
								subject, 
								predicateObject.getKey().toTrunkNode(), 
								nodeAndTriples.getKey()
						));
						System.out.println("-----------没有数量关系----------");
					}
				}
			}
		}
		return res;
	}
	
	/**
	 * 分析量词短语，求得相关三元组
	 * @param quantity 数量对
	 * @param nameNode 名字结点
	 * @param subject 主语
	 * @param predicate 原谓语
	 * @param firstObject 原宾语
	 * @return 相关的三元组
	 */
	List<Triple> analyzeQuantity(
			Entry<Node, Node> quantity, Node nameNode, Node subject, 
			Node predicate, DepNode firstObject) {
		List<Triple> res = new ArrayList<Triple>();
		if ( quantity == null ) return res;
		
		int blankNodeId = newBlankNodeId();
		
		res.add(new Triple(
				subject, 
				predicate,
				new BlankNode(blankNodeId)
		));
//		System.out.println("nameNode: " + nameNode + " quantity: " + quantity);
		if ( quantity.getValue().equals(nameNode) ) {
			List<DepNode> depNodes = firstObject.getChildren("dep");
			depNodes.addAll(firstObject.getModifiers());
			for (DepNode depNode : depNodes) {
//				System.out.println(types.get(depNode.getIndex() - 1));
				if ( !types.get(depNode.getIndex() - 1).getValue().equals("Undefined") ) {
					res.add(new Triple(
							new BlankNode(blankNodeId),
							new ImplicitPredicate("名字"),
							new TrunkNode(depNode.getIndex() - 1, depNode.getName())
					));
				}
			}
		} else {
			res.add(new Triple(
					new BlankNode(blankNodeId),
					new ImplicitPredicate("名字"),
					types.get(((TrunkNode) nameNode).getTrunkIndex()).getValue().equals("Undefined") ? 
							firstObject.toTrunkNode() : nameNode
			));
		}
		res.add(new Triple(
				new BlankNode(blankNodeId),
				new ImplicitPredicate("数量"),
				quantity.getKey()
		));
		res.add(new Triple(
				new BlankNode(blankNodeId),
				new ImplicitPredicate("单位"),
				quantity.getValue()
		));
		return res;
	}
	
	public DepNode getRoot() {
		return root;
	}
	
	private void print(DepNode root) {
		System.out.println(root);
		for (DepNode child : root.getChildren()) {
			print(child);
		}
	}
	
	public void print() {
		print(root);
	}
	
	public List<Entry<String, String>> getTypes() {
		return types;
	}
	
	public List<TaggedWord> getTagWords() {
		return tagWords;
	}
	
	/**
	 * 分配一个新的BlankNodeId
	 * @return 新的BlankNodeId
	 */
	public int newBlankNodeId() {
		return blankNodeId ++;
	}
	
	/**
	 * 广度遍历打印
	 */
	public String toString(){
		String s = "[ ";
		Queue<DepNode> queue = new LinkedList<DepNode>();
		queue.add(root);
		//Set<Integer> visited = new HashSet<Integer>();
		if(root == null) return "error";
		//visited.add(root.getIndex());
		while ( !queue.isEmpty() ) {
			DepNode currentNode = queue.poll();
			List<DepNode> children = currentNode.getChildren();
			for(DepNode child : children){
				queue.add(child);
				s+=child.getReln()+ "(" + child.getDep().gov() + ", " + child.getDep().dep() + ") ,";
			}
		}
		return s+"]";
	}
}
