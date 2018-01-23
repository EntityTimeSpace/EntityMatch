package search;

public class VirtGraphLoader {
	//如果Virtuoso安装在本体，这个是默认的地址和端口
	private static final String url = "jdbc:virtuoso://210.28.132.62:1120";
	//private static final String url = "jdbc:virtuoso://localhost:1111";
	private static final String user = "dba";
	private static final String password = "dba";
	public static String getUrl() {
		return url;
	}
	public static String getUser() {
		return user;
	}
	public static String getPassword() {
		return password;
	}
}

