package nlp;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import nlp.Parser;
import nlp.Segmenter;
import nlp.dep.DepTree;
import nlp.pattern.EntityPattern;
import nlp.pattern.NumberPattern;
import nlp.pattern.TimePattern;
import triple.Triple;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * 标注类，用于标注实体和三元组
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class Annotate {
	static private Annotate instance = null;
	
	private Annotate() {}
	
	public static Annotate getInstance() {
		if ( instance == null )
			instance = new Annotate();
		return instance;
	}
	
	/**
	 * 标注实体
	 * @param taggedWords 已经进行过词性标注的词语集{@link nlp.Tagger}
	 * @return 标注后的词语集，第一个分量是词语本身，第二个分量是实体类型
	 */
	public List<Entry<String, String>> annotateEntity(List<TaggedWord> taggedWords,String sentence) {
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		List<Entry<String, String>> trunks = new ArrayList<Entry<String,String>>();
		
		for (TaggedWord taggedWord : taggedWords) {
			String type = "Undefined";
			

				

				//type = EntityPattern.getType(taggedWord.value());
				
			
					/* 匹配时间 */
			if ( TimePattern.matches(taggedWord.value())) {
//					System.out.println(taggedWord.value());
				type = "http://ws.nju.edu.cn/nju28/Time";
//					type = "Time";
			} else{
				List<Term> termList = segment.seg(taggedWord.value());
				for(Term term:termList){
					//System.out.println("term"+term.nature);
					if(term.nature.toString().equals("ns")||term.nature.toString().equals("nsf")||term.nature.toString().equals("ntu")||term.nature.toString().equals("nts")||term.nature.toString().equals("nto")||term.nature.toString().equals("nth")||term.nature.toString().equals("ntch")||term.nature.toString().equals("ntc")||term.nature.toString().equals("ntcb")||term.nature.toString().equals("ntcf")){
						type = "http://ws.nju.edu.cn/nju28/Space";
						break;
					}
				}
			}
			if(taggedWord.value().trim().equals(":")){
				type = "Undefined";
			}
			
			//System.out.println("type++++++++++"+taggedWord.value()+type+taggedWord.tag());
			
/*			if(taggedWord.tag().equals("NT")){
				type = "http://ws.nju.edu.cn/nju28/Time";
			}
			else if(taggedWord.tag().equals("NR")){
				type = "http://ws.nju.edu.cn/nju28/Space";
			}*/
			
			//trunks.set(0,new SimpleEntry<String, String>(taggedWord.value(), type));
//			type = (type == null) ? ((taggedWord.tag().equals("NT")||taggedWord.tag().equals("NR"))?taggedWord.tag():"Undefined") : type;
			trunks.add(new SimpleEntry<String, String>(taggedWord.value(), type));
			
		}
		
		return trunks;
	}
	

	/**
	 * 标注三元组
	 * @param segWords 分词块，请以句号结尾
	 * @param types 词的实体类型
	 * @param tagWords 词性，该参数虽然可以通过{@link nlp.Tagger}获得，但为了提高效率，还是加入了这个参数
	 * @return 三元组集合
	 */
	public List<Triple> annotateTriple(
			List<String> segWords,
			List<Entry<String, String>> types,
			List<TaggedWord> tagWords) {
		System.out.println(segWords);
		System.out.println(types);
		System.out.println(tagWords);
		
		List<Triple> res = new ArrayList<Triple>();
		
		/* 这里进行逐句分割，否则内存不足 */
		int start = 0;
		int end = 0;
		int length = 0;
		int baseBlankId = 0;
		for (int i = 0; i < segWords.size(); i ++) {
			if ( segWords.get(i).equals("。") ) {
				end = i + 1;
				length = end - start;
				List<String> subSegWords = length > 80 ? 
								segWords.subList(start, start + 80) : 
								segWords.subList(start, end);
				Tree parse = Parser.getInstance().parse(subSegWords);
			/*	System.out.println("句法分析树=========");
				parse.pennPrint();
				System.out.println("句法分析树=========");*/
				//System.out.println("语法树========"+parse);
				List<TypedDependency> deps = Parser.getInstance().getDependency(parse);//依存关系
				System.out.println("依存关系集========"+deps);
//				System.out.println(start + "  " + end);
				DepTree tree = new DepTree(deps, types.subList(start, end), tagWords.subList(start, end));
				System.out.println("更正后的依存树"+tree.toString());
				List<Triple> subRes = tree.analyze();
				if(subRes == null) return null;
				int maxBlankId = Triple.getMaxBlankNodeId(subRes);
				Triple.detachNodes(subRes);
				Triple.addBaseAddress(subRes, start, baseBlankId);
				baseBlankId += maxBlankId + 1;
//				System.out.println(baseBlankId);
				res.addAll(subRes);
				start = end;
			}
		}
		return res;
	}
	
	/**
	 * 根据words获取新词
	 * @param words 最终分词结果
	 * @return 新词集
	 */
	public List<Entry<String, String>> getNewWords(List<Entry<String, String>> words) {
		List<Entry<String, String>> res = new ArrayList<Entry<String,String>>();
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> word : words) {
			sb.append(word.getKey());
		}
		List<String> originWords = Segmenter.getInstance().segment(sb.toString());
		
		System.out.println("originWords====="+originWords);
		
		/* 通过对比自动分词和手动分词结果，求得新词 */
		int i = 0, length_i = 0;
		int j = 0, length_j = 0;
		while ( length_i < sb.length() && length_j < sb.length() ) {
//			System.out.printf("begin: i=%d, j=%d, length_i=%d, length_j=%d\n", i, j, length_i, length_j);
			length_i += words.get(i).getKey().length();
			length_j += originWords.get(j).length();
			
			if ( length_i == length_j ) {
				String oldType = EntityPattern.getType(originWords.get(j));
				if(!words.get(i).getValue().equals(oldType != null ? oldType : "Undefined")) {
					res.add(words.get(i));
				}
				i ++; j ++;
				
			} else {
//				System.out.printf("begin: i=%d, j=%d, length_i=%d, length_j=%d\n", i, j, length_i, length_j);
				if ( length_i > length_j ) res.add(words.get(i));
				
				while ( length_i != length_j ) {
					if ( length_i < length_j ) {
						i ++;
						length_i += words.get(i).getKey().length();
					} else if ( length_i > length_j ) {
						j ++;
						length_j += originWords.get(j).length();
					}
				}
				i ++; j ++;
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
//		List<Entry<String, String>> types = new ArrayList<Entry<String,String>>();
//		types.add(new SimpleEntry<String, String>("库兹涅佐夫号", "Undefined"));
//		types.add(new SimpleEntry<String, String>("装载了", "Undefined"));
//		types.add(new SimpleEntry<String, String>("防空火力", "Undefined"));
//		types.add(new SimpleEntry<String, String>("。", "Undefined"));
//		System.out.println(types.get(1).getKey().length());
//		types.add(new SimpleEntry<String, String>("库兹涅佐夫号", "Undefined"));
//		types.add(new SimpleEntry<String, String>("库兹涅佐夫号", "Undefined"));
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		//System.out.println(Annotate.getInstance().getNewWords(types));
		List<Term> termList = segment.seg("朝鲜");
		System.out.println(termList);
		
	}
}
