package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import search.VirtuosoSparql;

@WebServlet("/DateRelationServlet")
public class DateRelationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		String startDate = req.getParameter("startDate").trim();
		String endDate = req.getParameter("endDate").trim();
		String relation = req.getParameter("rel").trim();
		String keyword = req.getParameter("keyword");
		//System.out.println(keyword+"keyword");
		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write( VirtuosoSparql.queryByDateRel(startDate,endDate,relation,keyword,pageNum));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
