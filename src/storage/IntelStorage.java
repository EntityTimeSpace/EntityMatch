/**
 * 
 */
package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import database.DBConnection;

/**
 * @author "Cunxin Jia"
 *
 */
public class IntelStorage {
	private static IntelStorage instance = null;
	public static IntelStorage getInstance() {
		if(instance == null) {
			instance = new IntelStorage();
		}
		return instance;
	}
	
	private IntelStorage() {
		
	}
	
	private Timestamp getRandomTimestamp() {
		long offset = Timestamp.valueOf("2005-01-01 00:00:00").getTime();
		long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
		long diff = end - offset + 1;
		Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
		return rand;
	}
	
	public int storeIntel(String rawIntel, String source) {
		int iid = 0;
		try {
			Connection conn = DBConnection.getConnection();
			long intelHash = rawIntel.hashCode();
			
			String sql = "select iid from intel where hash = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, intelHash);			
			ResultSet rs = stmt.executeQuery();
			//existed intelligence
			if(rs.next()) {
				iid = rs.getInt(1);	
				sql = "update intel set time = ? where iid = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				stmt.setInt(2, iid);
				stmt.executeUpdate();
			}
			//new intelligence
			else {
				sql = "insert into intel(content,hash,time,source) values(?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, rawIntel);
				stmt.setLong(2, rawIntel.hashCode());
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, source);
				stmt.executeUpdate();
				sql = "select last_insert_id()";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if(rs.next()) {
					iid = rs.getInt(1);
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iid;
	}
	
	public int storeTrunk(List<String> trunks, String source) {
		String rawIntel = getRawIntelFromTrunks(trunks);
		int iid = storeIntel(rawIntel, source);
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "delete from trunk where iid = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, iid);
			stmt.executeUpdate();
			
			sql = "insert into trunk(iid,tid,content) values (?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, iid);
			int tid = 0;
			for(String trunk : trunks) {
				if(trunk.trim().equals("")) {
					continue;
				}
				stmt.setInt(2, tid);
				stmt.setString(3, trunk);
				stmt.executeUpdate();
				tid ++;
			}
			stmt.close();
			conn.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iid;
	}
	
	public String getRawIntelFromTrunks(List<String> trunks) {
		String rawIntel = "";
		for(String trunk : trunks) {
			rawIntel += trunk;
		}
		return rawIntel;
	}
	
	public static void main(String[] args) {
		IntelStorage.getInstance().storeIntel("这是一条测试情报。", null);
	}
}
