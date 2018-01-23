package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import temporal.service.TemporalMatchingService;

/**
 * Servlet implementation class TemporalReasonerServlet
 */
@WebServlet("/TemporalReasonerServlet")
public class TemporalReasonerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TemporalReasonerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("temporal.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
        // 解决上传文件名的中文乱码问题
        upload.setHeaderEncoding("UTF-8");
        try {
			List<FileItem> list = (List<FileItem>)upload.parseRequest(new ServletRequestContext(request));
			FileItem eventFile = null, textFile = null;
			for (FileItem item : list) {
				if (item.getFieldName().equals("eventFile")) {
					eventFile = item;
				} else if (item.getFieldName().equals("eventText")) {
					textFile = item;
				}
			}
			if (eventFile != null) {
				if (textFile != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(eventFile.getInputStream()));
					String text = textFile.getString();
					System.out.println("hhhh"+text);
					String result = TemporalMatchingService.matching(reader, text);
					response.setStatus(200);
					response.setContentType("text/plain");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(result);
				} else {
					response.setStatus(400);
					response.getWriter().write("text");
				}
			} else {
				response.setStatus(400);
				response.getWriter().write("file");
			}
		} catch (FileUploadException e) {
			response.setStatus(400);
			response.getWriter().write("upload");
			e.printStackTrace();
		}
	}

}
