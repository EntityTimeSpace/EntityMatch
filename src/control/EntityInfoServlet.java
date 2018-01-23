package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xerces.util.SoftReferenceSymbolTable;
import org.dom4j.DocumentException;

import search.VirtuosoSparql;

/**
 * Servlet implementation class NewServlet
 */
@WebServlet("/EntityInfoServlet")
public class EntityInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EntityInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String queryUrl = request.getParameter("queryUrl");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		try {
			System.out.println("info"+VirtuosoSparql.queryBySubject(queryUrl,0));
			response.getWriter().write(VirtuosoSparql.queryBySubject(queryUrl,0));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(queryUrl);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
