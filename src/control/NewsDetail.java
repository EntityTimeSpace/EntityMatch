package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.DBConnection;

@WebServlet("/newsDetail")
public class NewsDetail extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		String pageurl = req.getParameter("queryUrl");
		
		
/*		Document doc = Jsoup.connect(pageurl).timeout(5000).get();
		//System.out.println(doc.toString());
		Element div;
		Elements ps;
		Element h1;
		Element p = null;
		if(pageurl.contains("ifeng")){
			div = doc.getElementById("artical");
			h1 = div.getElementsByTag("h1").get(0);
			ps = doc.getElementsByTag("p");
			resp.getWriter().write(h1.toString());
			//System.out.println(ps.toString());
			for(int i=0;i<ps.size();i++){
				if(i==1)continue;
				p = ps.get(i);
				if(p.toString().contains("用微信扫描二维码"))continue;
				//p.toString().contains("http://y2.ifengimg.com/a/2015/0708/icon_logo.gif")
				if(p.toString().contains("责任编辑")||p.toString().contains("标签")){
					resp.getWriter().write(p.text());
					break;
				}else if(p.toString().contains("http://y2.ifengimg.com/a/2015/0708/icon_logo.gif")){
					resp.getWriter().write(p.text());
					break;
				}else{
					resp.getWriter().write(p.toString());
				}
			}
		}else{
			 div = doc.getElementById("text");
			 ps = div.getElementsByTag("p");
			 h1 = doc.getElementsByClass("context").get(0).getElementsByTag("h1").get(0);
			 resp.getWriter().write(h1.toString()+doc.getElementsByClass("timeSummary").get(0)+ps.toString());
		}*/
		
		
		resp.getWriter().write(DBConnection.getPage(pageurl));
		//System.out.println(ps.toString());

		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
