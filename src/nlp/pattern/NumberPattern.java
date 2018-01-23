package nlp.pattern;

/**
 * 数模式识别类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class NumberPattern {
	static private String figure = "[0-9零一二三四五六七八九十百千万亿兆.,~-—～]";
	static private String aNumber = figure + figure + "*";
	static private String pattern = aNumber + "多?";
	
	/**
	 * 判断是否匹配数
	 * @param string 可能为时间的字符串
	 * @return 如果匹配数，返回true；否则，返回false
	 */
	public static boolean matches(String string) {
		return string.matches(pattern);
	}
	
	public static void main(String[] args) {
		System.out.println(matches("3"));
	}
}
