package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import search.VirtuosoSparql;

@WebServlet("/timespaceSearch")
public class TimeSpaceServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String timeRel = req.getParameter("timeRel");
		String loc = req.getParameter("loc");
		String spaceRel = req.getParameter("spaceRel");
		String locValue = req.getParameter("locValue");
		String pageNum = req.getParameter("pageNum");
		String keyWord = req.getParameter("keyword");
		System.out.println("keyWord===="+keyWord);
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(VirtuosoSparql.queryTimespace(startTime, endTime, timeRel, loc, spaceRel, Double.parseDouble(locValue.trim()), keyWord,Integer.parseInt(pageNum.trim())));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	
}
