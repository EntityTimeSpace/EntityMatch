package nlp.pattern;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DBConnection;

/**
 * 实体模式识别类
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class EntityPattern {

	
	public static void main(String[] args) {
//		char c = '“';
//		String s = "“”()（）*&……%￥#@！d我’？，。‘-——";
//		String s = "f32鬼怪";
		String s = "你好**么";
//		String s = "cads-n-1“嘎什坦”";
//		String s = "，";
//		System.out.println(s.length());
//		System.out.println(stringToPattern(s));
		System.out.println(matches(s));
//		System.out.println(getType(s));
//		System.out.println(matches(stringToPattern("cads-n-1“")));
//		for (int i = 0; i < s.length(); i ++) {
//			char c = s.charAt(i);
//			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//			System.out.println(c + " = " + ub);
//			
//		}
	}

	/**
	 * 获取string的实体类型
	 * @param string 可能为实体的字符串
	 * @return 实体类型；识别失败则为null
	 */
	public static String getType(String string) {
		String type = null;
		// 不含中英文字符的字串肯定是标点符号
		if ( isAllPunctuation(string) ) return type;
		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(String.format(
//					"SELECT DISTINCT `entities`.`name`, `entities`.`type` " +
//					"FROM `entities`, `synonyms` " +
//					"WHERE `entities`.`id` = `synonyms`.`id` and `synonyms`.`name` REGEXP '%s$'",
//					stringToPattern(string)));
//			ResultSet rs = stmt.executeQuery(String.format(
//					"SELECT `name`, `type` FROM `dict` " +
//					"WHERE `name` REGEXP '%s$'", stringToPattern(string)));
			ResultSet rs = stmt.executeQuery(String.format(
					"SELECT `name`, `type` FROM `dict` " +
					"WHERE `name` LIKE '%s'", "%" + string + "%"));
			if ( rs.next() ) {
				type = rs.getString("type");
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return type;
	}
	
	
	/**
	 * 判断是否匹配实体
	 * @param string 可能为实体的字符串
	 * @return 如果匹配实体，返回true；否则，返回false
	 */
	public static boolean matches(String string) {
		boolean res = false;
		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(String.format(
//					"SELECT DISTINCT `entities`.`name`, `entities`.`type` " +
//					"FROM `entities`, `synonyms` " +
//					"WHERE `entities`.`id` = `synonyms`.`id` and `synonyms`.`name` REGEXP '.*%s.*'",
//					stringToPattern(string)));
			ResultSet rs = stmt.executeQuery(String.format(
					"SELECT `name`, `type` FROM `dict` " +
					"WHERE `name` LIKE '%s'", "%" + string + "%"));
//			ResultSet rs = stmt.executeQuery(String.format(
//					"SELECT `name`, `type` FROM `dict` " +
//					"WHERE `name` REGEXP '.*%s.*'", stringToPattern(string)));
			res = rs.next();
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 判断字符串s是否为右边半标点符号。
	 * @param s 待判断的字符串
	 * @return 如果是，返回true；否则，返回false
	 */
	public static boolean isRightHalfPunctuation(String s) {
		return s.equals("”") || s.equals("'") || s.equals("\"") ||
				s.equals("}") || s.equals("]") || s.equals("’") ||
				s.equals(")") || s.equals("）") || s.equals("】") ||
				s.equals("｝");
	}

	/**
	 * 判断字符串s是否为左边半标点符号。
	 * @param s 待判断的字符串
	 * @return 如果是，返回true；否则，返回false
	 */
	public static boolean isLeftHalfPunctuation(String s) {
		return s.equals("“") || s.equals("'") || s.equals("\"") ||
				s.equals("{") || s.equals("[") || s.equals("‘") ||
				s.equals("(") || s.equals("（") || s.equals("【") ||
				s.equals("｛");
	}
	
	/**
	 * 判断字符串s是否全为标点符号。
	 * @param s 待判断的字符串
	 * @return 如果是，返回true；否则，返回false
	 */
	public static boolean isAllPunctuation(String s) {
		for (int i = 0; i < s.length(); i ++)
			if ( !isPunctuation(s.charAt(i)) ) {
				return false;
			}
		return true;
	}
	
	/**
	 * 把字符串string转换为正则表达式模式串。
	 * @param string 待转换的字符串
	 * @return 正则表达式模式串
	 */
	private static String stringToPattern(String string) {
		StringBuffer pattern = new StringBuffer();
		int type = 1;
		for (int i = 0; i < string.length(); i ++) {
			char c = string.charAt(i);
			/* 中文、数字、英文的交界处可能隐含一个字符，
			 * 在数据库里，一个中文字符相当于三个英文字符。
			 */
			if ( isChinese(c) ) {
				if ( type != 1 ) pattern.append(".?.?.?");
				pattern.append(c);
				type = 1;
			} else if ( isEnglish(c) ) {
				if ( type != 2 ) pattern.append(".?.?.?");
				pattern.append(c);
				type = 2;
			} else if ( isDigit(c) ) {
				if ( type != 3 ) pattern.append(".?.?.?");
				pattern.append(c);
				type = 3;
			} else {
				pattern.append(".?.?.?");
				type = 0;
			}
		}
		return pattern.toString();
	}
	
	/**
	 * 判断c是否属于ubs所给出的字符类型
	 * @param c 待判断的字符
	 * @param ubs 字符类型集合
	 * @return 如果是，返回true；否则，返回false
	 */
	private static boolean isUnicodeBlock(char c, Character.UnicodeBlock ... ubs) {
		Character.UnicodeBlock ubc = Character.UnicodeBlock.of(c);
		for (Character.UnicodeBlock ub : ubs) {
			if ( ub == ubc )
				return true;
		}
		return false;
	}
	
	/**
	 * 判断c是否为英语
	 * @param c 待判断的字符
	 * @return 如果是，返回true；否则，返回false
	 */
	private static boolean isEnglish(char c) {
		return Character.isLetter(c);
	}
	
	/**
	 * 判断c是否为数字
	 * @param c 待判断的字符
	 * @return 如果是，返回true；否则，返回false
	 */
	private static boolean isDigit(char c) {
		return Character.isDigit(c);
	}
	
	/**
	 * 判断c是否为标点符号
	 * @param c 待判断的字符
	 * @return 如果是，返回true；否则，返回false
	 */
	private static boolean isPunctuation(char c) {
		return !isEnglish(c) && !isChinese(c) && !isDigit(c);
	}
	
	/**
	 * 判断c是否为中文
	 * @param c 待判断的字符
	 * @return 如果是，返回true；否则，返回false
	 */
	private static boolean isChinese(char c) {
		return isUnicodeBlock(c, 
				Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,
				Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,
				Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,
				Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS,
				Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
	}
}
