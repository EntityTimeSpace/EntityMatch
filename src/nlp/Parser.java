package nlp;

import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;

/**
 * 语法解析类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class Parser {

	
	public static void main(String[] args) {
		String testString = "薄熙来自从担任商务部长以来，一直兢兢业业。";
		LexicalizedParser lp = LexicalizedParser.loadModel(MODEL_CHINESE_PCFG);
		Tree parse = lp.apply(Sentence.toWordList(
				Segmenter.getInstance().segment(testString)));
		parse.pennPrint();
		System.out.println();
		
//		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
//		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
//		ChineseGrammaticalStructure cgs = new ChineseGrammaticalStructure(parse);
//		List<TypedDependency> tdl = cgs.typedDependenciesCCprocessed();
//		System.out.println("Hello");
//		System.out.println(tdl);
//		System.out.println();
	}
	
	
	static public final String MODEL_PREFIX = "edu/stanford/nlp/models/lexparser/";
	static public final String MODEL_CHINESE_PCFG = MODEL_PREFIX + "chinesePCFG.ser.gz";
	static public final String MODEL_CHINESE_FACTORED = MODEL_PREFIX + "chineseFactored.ser.gz";
	
	private LexicalizedParser lp;
	static private Parser instance = null;
	
	private String model;
	
	private Parser() {
		lp = LexicalizedParser.loadModel(MODEL_CHINESE_PCFG);//加载模型文件，语料库
		model = MODEL_CHINESE_PCFG;
	}
	
	private Parser(String model) {
		lp = LexicalizedParser.loadModel(model);
		this.model = model;
	}
	
	public static Parser getInstance() {
		return (instance == null) ? (instance = new Parser()) : instance;
	}
	
	public String getModel() {
		return model;
	}

	/**
	 * @deprecated
	 * 解析wordStrings的语法
	 * @param wordStrings 已经分好词的句子，词与词之间用空格隔开。
	 * @return 语法树
	 */
	public Tree parse(String wordStrings) {
		return lp.apply(wordStrings);
	}
	
	/**
	 * 解析wordStrings的语法
	 * @param wordStrings 已经分好词的句子，每个词对于List中的一个元素
	 * @return 语法树
	 */
	public Tree parse(List<String> wordStrings) {
		return lp.apply(Sentence.toWordList(wordStrings));
	}
	
	/**
	 * 获取parseTree的依赖关系集
	 * @param parseTree 语法树
	 * @return 依赖关系集
	 */
	public List<TypedDependency> getDependency(Tree parseTree) {
		ChineseGrammaticalStructure cgs = new ChineseGrammaticalStructure(parseTree);
		return cgs.typedDependenciesCCprocessed();
	}
	
	/**
	 * 获取wordStrings的依赖关系集
	 * @param wordStrings 已经分好词的句子，每个词对于List中的一个元素
	 * @return 依赖关系集
	 */
	public List<TypedDependency> getDependency(List<String> wordStrings) {
		Tree parseTree = parse(wordStrings);
		return getDependency(parseTree);
	}
}
