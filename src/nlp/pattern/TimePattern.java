package nlp.pattern;

/**
 * 时间模式识别类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class TimePattern {
	static private String figure = "[0-9零一二三四五六七八九十百千万亿兆某元初廿卅]";
	static private String number = figure + "*";
	static private String year = number + "年的?";
	static private String month = number + "月的?";
	static private String seasonOrWeek = "(第?" + figure + "|[上下])个?(季度|周|星期)";
	static private String scope = "(之|以)?(前|后|时分|(之?时))";
	static private String days = "([上中下]旬)的?[初头中末尾底]";
	static private String day = "(" + number + "|昨|今|明|后|前|大前|大后)[日天号]的?";
	static private String timeLiteral = "(凌晨|拂晓|早上|晚上|下午|午后|上午|中午|傍晚|夜间|夜里|午夜)的?";
	static private String time = number + "[点|:|：](" + number + "[分|:|：])?(" + number + "秒?)?";
	static private String pattern = "最?近?(" + year + ")?" +
									"(" + month + ")?" +
									"(" + seasonOrWeek + ")?" +
									"(" + days + ")?" +
									"(" + seasonOrWeek + ")?" +
									"(" + day + ")?" +
									"(" + timeLiteral + ")?" +
									"(" + time + ")?" /*+
									"(" + scope + ")?"*/;
	
	/**
	 * 判断是否匹配时间
	 * @param string 可能为时间的字符串
	 * @return 如果匹配时间，返回true；否则，返回false
	 */
	public static boolean matches(String string) {
		return string.matches(pattern);
	}
	
	public static void main(String[] args) {
		System.out.println(matches("五月的三十号上午三点十五秒时分的"));
	}
}
