package nlp;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nlp.pattern.EntityPattern;
import nlp.pattern.NumberPattern;
import nlp.pattern.TimePattern;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;


/**
 * 分词类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class Segmenter {
	
	public static void main(String[] args) throws IOException {
//		Segmenter segmenter = Segmenter.getInstance();
//		segmenter.getClassifier().classifyAndWriteAnswers(PATH_PREFIX + "input.txt");
		System.out.println(Segmenter.getInstance().segment("库兹涅佐夫号航空母舰将于明天凌晨装载20架苏27。"));
	}
	
//	static final private String PATH_PREFIX =
//			;
	
	private CRFClassifier<CoreLabel> classifier;
	static private Segmenter instance = null;
	
	private Segmenter() {
		
		Properties props = new Properties();
		String sighanCorporaDictPath = Segmenter.class.getClassLoader().getResource("").getPath() + "data";
		sighanCorporaDictPath = URLDecoder.decode(sighanCorporaDictPath);
		props.setProperty("sighanCorporaDict", sighanCorporaDictPath);
		String serDictionaryPath = Segmenter.class.getClassLoader().getResource("data/dict-chris6.ser.gz").getPath() + "," +
				Segmenter.class.getClassLoader().getResource("usr.dict.txt").getPath();
		serDictionaryPath = URLDecoder.decode(serDictionaryPath);
		props.setProperty("serDictionary", serDictionaryPath);
		props.setProperty("inputEncoding", "UTF-8");
		props.setProperty("sighanPostProcessing", "true");

//		System.out.println(Segmenter.class.getClassLoader().getResourceAsStream("usr.dict.txt"));
		classifier = new CRFClassifier<CoreLabel>(props);
		String ctbPath = Segmenter.class.getClassLoader().getResource("data/ctb.gz").getPath();
		ctbPath = URLDecoder.decode(ctbPath);
		classifier.loadClassifierNoExceptions(ctbPath , props);
//		flags must be re-set after data is loaded
		
		classifier.flags.setProperties(props);
		
		// 用来把结果写到文件中
//		classifier.classifyAndWriteAnswers(args[0]);
	}
	
	static public Segmenter getInstance() {
		return (instance == null) ? (instance = new Segmenter()) : instance;
	}
	
	public CRFClassifier<CoreLabel> getClassifier() {
		return classifier;
	}
	
	/**
	 * 对segmentString进行分词，并修正分词结果
	 * @param segmentString 待分词的字符串
	 * @return 修正后的分词结果
	 */
	public List<String> segment(String segmentString) {
		return segment(segmentString, true);
	}
	
	/**
	 * 对segmentString进行分词，并根据doAmend决定是否修正
	 * @param segmentString 待分词的字符串
	 * @param doAmend 是否进行修正
	 * @return 分词结果
	 */
	public List<String> segment(String segmentString, boolean doAmend) {
		List<String> trunks = classifier.segmentString(segmentString);
		System.out.println("amend"+trunks);
		return doAmend ? amend(trunks) : trunks;
	}
	
	/**
	 * 修正分词结果
	 * @param trunks 已经分好词的词语集合
	 * @return 修正后的分词结果
	 */
	public List<String> amend(List<String> trunks) {
		List<String> res = new ArrayList<String>();
		int i = 0;
		while ( i < trunks.size() ) {
			int j = i;
			
			/* 如果当前词是非左边的标点符号，就把当前词作为独立的词，不进行合并。 */
			if ( EntityPattern.isAllPunctuation(trunks.get(i)) &&
					!EntityPattern.isLeftHalfPunctuation(trunks.get(i)) ) {
				res.add(trunks.get(i));
				i ++;
				continue;
			}
			
			/* 利用EntityPattern、TimePattern和NumberPattern判断相邻若干词
			 * 是否能合并，能合并的就合并。
			 */
			StringBuffer sb = new StringBuffer();
			while ( j < trunks.size() &&
					(EntityPattern.matches(sb.toString() + trunks.get(j)) ||
					TimePattern.matches(sb.toString() + trunks.get(j)) ||
					NumberPattern.matches(sb.toString() + trunks.get(j))) ) {
				sb.append(trunks.get(j));
				j ++;
			}
			
			if ( sb.length() == 0 ) {
				res.add(trunks.get(i));
				i ++;
			
			} else {
				/* 需要把尾部的非右边的标点符号砍掉 */
				while ( j - 1 >= 0 &&
						EntityPattern.isAllPunctuation(trunks.get(j - 1)) &&
						!EntityPattern.isRightHalfPunctuation(trunks.get(j - 1)) )
					j --;
				sb = new StringBuffer();
				for (int k = i; k < j; k ++) {
					sb.append(trunks.get(k));
				}
				if ( sb.length() == 0 ) {
					res.add(trunks.get(i));
					i ++;
				} else {
					res.add(sb.toString());
					i = j;
				}
			}
		}
		return res;
	}
}

