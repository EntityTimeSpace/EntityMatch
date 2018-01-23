package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import match.RWMatch;


/**
 * Servlet implementation class Match
 */
@WebServlet("/MatchServlet")
public class MatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<String> files;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String a=request.getParameter("isAjax");
		try{
			int i=Integer.parseInt(a);
			if(i==1){
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=utf-8");
				try {
					RWMatch qadb = new RWMatch();
					String s1 = qadb.match(files.get(0), files.get(1));
					String s2 = qadb.eval(files.get(2));
					StringBuilder sb = new StringBuilder();
				//	sb.append(s2);
				//	sb.append("&&&");
					sb.append(s1);
					response.getWriter().write(sb.toString());	//{"pre":"0.9000","rec":"0.8000","f1":"0.8500"} 鍚庨潰鏄箣鍓嶇殑json	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					response.getWriter().write("failed");
					e.printStackTrace();
				}
			}
		}
		catch(Exception e){
			files = saveFile(request);
			response.sendRedirect("showResult.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private List<String> saveFile(HttpServletRequest request) {
		List<String> files = new ArrayList<String>();
		 String Ext_Name = "owl,rdf,xml";
		 String savePath = this.getServletContext().getRealPath("WEB-INF/upload");
	        File saveFileDir = new File(savePath);
	        if (!saveFileDir.exists()) {
	            saveFileDir.mkdirs();
	        }
	        String tmpPath = this.getServletContext().getRealPath("WEB-INF/tem");
	        File tmpFile = new File(tmpPath);
	        if (!tmpFile.exists()) {
	            tmpFile.mkdirs();
	        }

	        String message = "";
	        try {
	            DiskFileItemFactory factory = new DiskFileItemFactory();
	            factory.setSizeThreshold(1024 * 10);
	            factory.setRepository(tmpFile);
	            ServletFileUpload upload = new ServletFileUpload(factory);
	            upload.setHeaderEncoding("UTF-8");
	            List<FileItem> list = (List<FileItem>)upload.parseRequest(new ServletRequestContext(request));
	            for(FileItem item:list){
	                if (item.isFormField()) {
	                } else
	                {
	                    String fileName = item.getName();
	                    if (fileName == null || fileName.trim().length() == 0) {
	                        continue;
	                    }
	                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
	                    
	                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	                    if(!Ext_Name.contains(fileExt)){
	                        break;
	                    }

	                    if(item.getSize() == 0) continue;
	                    	                    
	                    InputStream is = item.getInputStream();
	                    String s = savePath + "\\" + fileName;
	                    files.add(s);
	                    FileOutputStream out = new FileOutputStream(s);
	                    byte buffer[] = new byte[1024];
	                    int len = 0;
	                    while((len = is.read(buffer)) > 0){
	                        out.write(buffer, 0, len);
	                    }
	                    out.close();
	                    is.close();
	                //    item.delete();
	                    
	                    System.out.println(message);
	                    
	                }

	            }
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } finally {
	            request.setAttribute("message", message);
	        }
			return files;
	}
}
