package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import search.VirtuosoSparql;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/KeywordSearchServlet")
public class KeywordSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KeywordSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String search = request.getParameter("searchValue");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
/*		try {
			System.out.println("search======="+VirtuosoSparql.queryByKeyword(search));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			String resultJson;
			response.getWriter().write(resultJson = VirtuosoSparql.queryByKeyword(search,Integer.parseInt(request.getParameter("pageNum"))));
			//System.out.println("search======="+resultJson);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
