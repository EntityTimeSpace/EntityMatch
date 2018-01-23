/**
 * 
 */
package servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nlp.Segmenter;

import org.json.simple.JSONArray;

/**
 * @author "Cunxin Jia"
 *
 */
public class SegmentService extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String rawString = req.getParameter("raw");
		if(rawString == null) {
			rawString = "";
		}
		System.out.println("------------SegmentService--------------");
		rawString =  new String(rawString.getBytes("ISO8859-1"), "UTF-8");
		List<String> segmented = Segmenter.getInstance().segment(rawString);
		JSONArray json = new JSONArray();
		json.addAll(segmented);
		String encodedJSON = URLEncoder.encode(json.toString(), "UTF-8");
		resp.setContentType("text/json; charset=UTF-8");
		resp.getWriter().print(encodedJSON);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
