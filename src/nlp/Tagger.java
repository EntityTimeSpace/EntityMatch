package nlp;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * 词性标注类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class Tagger {
	
	
	private MaxentTagger tagger;
	static private Tagger instance = null;
	
	public static void main(String[] args) {
//		String testString = "薄熙来自从担任商务部长以来，一直兢兢业业。";
//		String testString = "奥巴马10月20日来加州考察。";
//		String testString = "奥巴马 10月20日 来 加州 考察 。";
		//String testString = "库兹涅佐夫号 将 于 晴天  在 东海  装载 SU-33 。";
		//String testString = "库兹涅佐夫号 装载 了 4 台 sa-n-9 ， 主力 为 27 架 苏33 。";
//		String testString = "苏33 凌晨 3点 在 南京 起飞 ， 于 次日 下午 18点 在 北京 降落 。";
		String testString = "苏33 凌晨 3点 在 南京 起飞 ， 于 次日 下午 18点 在 北京 降落 上海 南京 武汉 浙江 江西 湖南 南京大学 南京火车站 。";
//		List<TaggedWord> words = new Tagger().tagRawText(testString);
//		System.out.println(Sentence.listToString(words, false));
		
		//System.out.println(Tagger.getInstance().tag(testString));
//		List<String> segmentStringList = Segmenter.getInstance().segment(testString);
//		List<TaggedWord> taggedWords = Tagger.getInstance().tag(Segmenter.getInstance().segment(testString));
//		System.out.println(taggedWords);
//		System.out.println(taggedWords.get(1).value() + " " + taggedWords.get(1).tag());
//		Parser.getInstance().parse(Segmenter.getInstance().segment(testString)).pennPrint();
//		System.out.println(segmentString.toString().trim());
		
//		Tagger tagger = new Tagger();
		
//		for (List<HasWord> sentence : sentences) {
//			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
////			System.out.println(tSentence.get(1).tag());
//			System.out.println(Sentence.listToString(tSentence, false));
//		}
		
		
		List<String> segWords = new ArrayList<String> (Arrays.asList(testString.split(" ")));//分词后各项词的list
		/* 保证以句号结尾 */
		if ( !segWords.get(segWords.size() - 1).equals("。") )
			segWords.add("。");
		
		List<TaggedWord> tagWords = Tagger.getInstance().tag(segWords);
		System.out.println(tagWords+"value="+tagWords.get(0).tag());
		//List<Entry<String, String>> trunks = Annotate.getInstance().annotateEntity(tagWords);
		//System.out.println(trunks);
		
		
	}
	
	public MaxentTagger getTagger() {
		return tagger;
	}
	
	private Tagger() {		
		try {
			String taggerPath = Tagger.class.getClassLoader().getResource("models/chinese.tagger").getPath();
			taggerPath = URLDecoder.decode(taggerPath);
			tagger = new MaxentTagger(taggerPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static public Tagger getInstance() {
		return (instance == null) ? (instance = new Tagger()) : instance;
	}
	
	/**
	 * 对wordStrings进行词性标注
	 * @param wordStrings 已经分好词的词语集
	 * @return 标注结果
	 */
	public List<TaggedWord> tag(List<String> wordStrings) {
		List<HasWord> words = Sentence.toWordList(wordStrings);
		return tagger.tagSentence(words);
	}
	
	/**
	 * @deprecated
	 * 对toTag进行词性标注（有重大安全隐患）
	 * @param toTag 已经分好词的词语集，词与词之间由空格隔开
	 * @return 标注结果
	 */
	public List<TaggedWord> tag(String toTag) {
		List<TaggedWord> res = new ArrayList<TaggedWord>();
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(toTag));
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			res.addAll(tSentence);
		}
		return res;
	}
	
	/**
	 * 对rawText进行词性标注
	 * @param rawText 未进行分词的字符串
	 * @return 标注结果
	 */
	public List<TaggedWord> tagRawText(String rawText) {
		List<String> sentences = Segmenter.getInstance().segment(rawText);
		return tag(sentences);
	}
}
