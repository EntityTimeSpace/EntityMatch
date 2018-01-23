/**
 * 
 */
package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import classTree.ClassTree;
import classTree.ClassTreeGenerator;
import riotcmd.json;

/**获取本体的类的树状列表
 * @author "Cunxin Jia"
 *
 */
public class GetClassTree extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", "时间");
		jsonObject.put("uri", "http://ws.nju.edu.cn/nju28/Time");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		jsonObject = new JSONObject();
		jsonObject.put("label", "空间");
		jsonObject.put("uri", "http://ws.nju.edu.cn/nju28/Space");
		jsonArray.add(jsonObject);
		//ClassTree classTree = ClassTreeGenerator.getInstance().getClassTree();
		JSONObject json = new JSONObject();
		json.put("classTree", jsonArray);
		
		String jsonString = json.toString();
		System.out.println("jsonString====="+jsonString);
		resp.setContentType("text/json; charset=UTF-8");
		resp.getWriter().print(jsonString);
//		System.out.println(jsonString);
	}

	@Override
	public void destroy() {
		super.destroy();
	}


	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("label", "时间");
		jsonObject.put("uri", "http://ws.nju.edu.cn/nju28/Time");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		jsonObject = new JSONObject();
		jsonObject.put("label", "空间");
		jsonObject.put("uri", "http://ws.nju.edu.cn/nju28/Space");
		jsonArray.add(jsonObject);
		//ClassTree classTree = ClassTreeGenerator.getInstance().getClassTree();
		JSONObject json = new JSONObject();
		json.put("classTree", jsonArray);
		String jsonString = json.toString();
		System.out.println("---------------"+jsonString);
	}
}
