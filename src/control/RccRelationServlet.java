package control;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import search.VirtuosoSparql;
import triple.mytemp;

@WebServlet("/RccRelationServlet")
public class RccRelationServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String firstEntity = req.getParameter("location").trim();
		String secondEntity = req.getParameter("rccRel").trim();
		double rccRel = Double.parseDouble(req.getParameter("distance").trim());
		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
		//System.out.println("rcc====="+firstEntity+secondEntity+rccRel);
/*		String[] results = null;
		try {
			results = mytemp.GetResults(firstEntity, rccRel,secondEntity );
		} catch (InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/	
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(VirtuosoSparql.queryRelatedLocation(firstEntity, secondEntity, rccRel, pageNum));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	
}
