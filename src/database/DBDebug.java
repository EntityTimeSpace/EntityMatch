package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import storage.IntelStorage;

/**
 * 
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class DBDebug {

	private static String[][] nameTypes = {
		{"库兹涅佐夫号", "航空母舰"},
		{"su-27", "舰载机"},
		{"苏-27", "舰载机"},
		{"cads-n-1喀什塔", "导弹"},
		{"cads-n-1嘎什坦", "导弹"},
		{"sa-n-9", "导弹"},
		{"sa-n-11", "导弹"},
		{"瓦良格号", "航空母舰"},
		{"基洛夫号", "航空母舰"},
		{"AK－630型6管30mm炮", "火炮"},
		{"“天空哨兵”相控阵雷达", "雷达"},
		{"MR－710“顶板”三座标对空/对海雷达", "雷达"},
		{"MR－320M“双支撑”对海雷达", "雷达"},
		{"MR－360“十字剑”火控雷达", "雷达"},
		{"3P37“热闪”火控雷达", "雷达"},
		{"“蛋糕台”战术空中导航雷达", "雷达"},
		{"“艾森豪威尔”号航空母舰", "航空母舰"},
		{"八联装“海麻雀”中程对空导弹发射装置", "导弹"},
		{"20毫米6管“火神-密集阵”近程武器系统", "火炮"},
		{"水密横舱壁", "墙"},
		{"防火舱壁", "墙"},
		{"辽宁号", "航空母舰"},
		{"卡尔·文森号航空母舰", "航空母舰"},
		{"林肯号", "航空母舰"},
		{"杜鲁门号航母", "航空母舰"},
		{"F-14战斗机", "舰载机"},
		{"大黄蜂战斗攻击机", "舰载机"},
		{"EA―6B徘徊者号电子战机", "舰载机"},
		{"S-3B北欧海盗反潜机", "舰载机"},
		{"E-2C鹰眼预警机", "舰载机"},
		{"S-60海鹰反潜直升机", "舰载机"},
		{"C-2快轮运输机", "舰载机"},
		{"“尼米兹”级航母", "航空母舰"},
		{"AV－8B“鹞II”式垂直起降飞机", "舰载机"},
		{"SH－3D“海王”直升机", "舰载机"},
		{"“朱塞佩·加里波第”号航母", "航空母舰"},
		{"加里波底号", "航空母舰"},
		{"双连装40L70标枪式（DARDO）", "火炮"},
		{"阿斯派德飞弹（Aspide）", "导弹"},
		{"卓越号航空母舰", "航空母舰"},
		{"“卓越”号航母", "航空母舰"},
		{"维拉特号航空母舰", "航空母舰"},
		{"FRSMK-51型海鹞式垂直/短距起降飞机", "舰载机"},
		{"MK2型反潜直升机", "舰载机"},
		{"圣保罗号航空母舰", "舰载机"},
		{"F-18", "舰载机"},
		{"20毫米机炮", "火炮"},
		{"AIM-9L“响尾蛇”空对空导弹", "导弹"},
		{"A-6E“入侵者”", "舰载机"},
		{"S-3B反潜机", "舰载机"},
		{"“长滩”级", "巡洋舰"},
		{"“战斧”巡航导弹", "导弹"},
		{"AGM-86A", "导弹"},
		{"AGM-86B", "导弹"},
		{"“布拉莫斯”导弹", "导弹"},
		{"“爱国者”防空导弹", "导弹"},
		{"“飞鱼”导弹", "导弹"},
		{"“响尾蛇”", "导弹"},
		{"“三叉戟”I型", "导弹"},
		{"“三叉戟”II型", "导弹"},
		{"“飞毛腿B”导弹", "导弹"},
		{"“鱼叉”AGM-84", "导弹"},
		{"“洛杉矶”级", "潜艇"},
		{"海狼级潜艇", "潜艇"},
		{"“华盛顿”级", "潜艇"},
		{"“北极星A1”弹道导弹", "导弹"},
		{"弗吉尼亚级核动力攻击潜", "导弹"},
		{"SS-N-19反舰导弹", "导弹"},
		{"钢材坟场级长波超视距雷达", "雷达"},
		{"达利亚尔级", "雷达"},
		{"65型", "鱼雷"},
		{"RGB-60", "炸弹"},
		{"730型30毫米近防炮系统", "火炮"},
		{"AO-18K式机关炮", "火炮"},
		{"“巨浪-2”导弹", "导弹"},
	};
	
	private static String[][] entities = {
		{"1", "库兹涅佐夫号", "航空母舰"},
		{"2", "SU-27", "舰载机"},
		{"3", "喀什塔", "导弹"},
		{"4", "sa-n-9", "导弹"},
		{"5", "瓦良格号", "航空母舰"},
		{"6", "基洛夫号", "航空母舰"},
	};

	private static String[][] synonyms = {
		{"库兹涅佐夫号", "1"},
		{"su-27", "2"},
		{"苏-27", "2"},
		{"喀什塔", "3"},
		{"cads-n-1“嘎什坦”", "3"},
		{"sa-n-9", "4"},
		{"瓦良格号", "5"},
		{"基洛夫号", "6"},
	};
	
	public static void main(String[] args) {
//		clearData();
//		addNewData();
//		addThemeData();
//		addData();
//		debugException();
		addIntels();
	}
	
	
	public static void addThemeData() {
		final String PATH = "军用主题词 - 副本";
		File dir = new File(PATH);
		File[] files = dir.listFiles();
//		int count = 0;
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `dict`(`name`, `type`) " +
					"values(?, 'Undefined')");

			for (File file : files) {
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(file)));
					String line;
					while ( (line = br.readLine()) != null ) {
						if ( line.length() > 0 ) {
							pstmt.setString(1, line);
							pstmt.addBatch();
//							System.out.println(count ++ + " : " + line);
						}
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				pstmt.executeBatch();
			} catch (SQLException e) {}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void clearData() {
		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
//			stmt.executeUpdate("DELETE FROM `entities`");
//			stmt.executeUpdate("DELETE FROM `synonyms`");
			stmt.executeUpdate("DELETE FROM `dict`");
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void addNewData() {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `dict`(`name`, `type`) " +
					"values(?, ?)");
			for (String[] nameType : nameTypes) {
				pstmt.setString(1, nameType[0]);
				pstmt.setString(2, nameType[1]);
				pstmt.addBatch();
			}
			try {
				pstmt.executeBatch();
			} catch (SQLException e) {}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void addData() {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `entities`(`id`, `name`, `type`) " +
					"values(?, ?, ?)");
			for (String[] entity : entities) {
				pstmt.setString(1, entity[0]);
				pstmt.setString(2, entity[1]);
				pstmt.setString(3, entity[2]);
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			pstmt = conn.prepareStatement(
					"INSERT INTO `synonyms`(`name`, `id`) " +
					"values(?, ?)");
			for (String[] synonym : synonyms) {
				pstmt.setString(1, synonym[0]);
				pstmt.setString(2, synonym[1]);
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void debugException() {
		try {
			try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					"INSERT INTO `entities`(`id`, `name`, `type`) " +
					"values(10, 'a', 'b')");
			stmt.close();
			conn.close();} catch (Exception e) {}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Hello");
	}
	
	public static void addIntels() {
		try {
			Scanner input = new Scanner(new File("cases.txt"));
			while(input.hasNext()) {
				String line = input.nextLine();
				String[] splited = line.split(" ");
				List<String> trunks = new ArrayList(Arrays.asList(splited));
				System.out.println(trunks);
				IntelStorage.getInstance().storeTrunk(trunks, null);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
