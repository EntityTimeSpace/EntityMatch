package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import search.VirtuosoSparql;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/ParamSearchServlet")
public class ParamSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParamSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		
	    //request.removeAttribute("pageNum");
		/*Enumeration<String> params = request.getParameterNames();

	//	Map<String,ArrayList<String>> and = new HashMap<String,ArrayList<String>>();
		StringBuffer and = new StringBuffer();
		StringBuffer or = new StringBuffer();
		and.append("\"与\":[");
		or.append("\"或\":[");
		String s=null;
		while(params.hasMoreElements()) {
			//System.out.println("params====");
			//if(params.nextElement().equals("pageNum"))break;
			String attributeName = request.getParameter(s=params.nextElement());
			if(s.equals("pageNum"))break;
			//System.out.println(s.toString()+attributeName);
			String attributeValue = request.getParameter(params.nextElement());
			//System.out.println(s.toString()+attributeValue);
			String relation = request.getParameter(params.nextElement());
			//System.out.println(s.toString()+relation);
		
			switch(relation) {
			case "none": case "and": 
				String[] avalues = attributeValue.split(" ");
				for(String value:avalues) {
					and.append("{\"属性\":" + attributeName + ",\"值\":" + value + "};"); 
				}
				break;
			case "or": 
				String[] values = attributeValue.split(" ");
				for(String value:values) {
					or.append("{\"属性\":" + attributeName + ",\"值\":" + value + "};"); 
				}
				break;
			}
		}
		String aStr = and.toString();
		if(aStr.lastIndexOf(";") != -1)
			aStr = aStr.substring(0,aStr.lastIndexOf(";"));
		aStr += "];";
		String oStr = or.toString();
		if(oStr.lastIndexOf(";") != -1)
			 oStr = oStr.substring(0,oStr.lastIndexOf(";"));
		oStr += "]";
		or.append("]");
		String queryJson = "{" + aStr + oStr + "}";
		System.out.println(queryJson);*/
		
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		String[] types = request.getParameter("type").split("&");
		String keyword = request.getParameter("keyword");
		//System.out.println("type========"+request.getParameter("type"));
		
		String properties = request.getParameter("properties");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		//System.out.println("param======"+keyword+types[0]+properties);
		try {
			response.getWriter().write( VirtuosoSparql.queryBykeyAndproperty(types, keyword, properties, pageNum));
			//System.out.println("s======="+s);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		try {
			VirtuosoSparql.queryByPropertyValue(queryJson,pageNum));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
