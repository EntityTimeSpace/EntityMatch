package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nlp.Annotate;
import nlp.Tagger;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * 标注实体servlet
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class AnnotateEntity extends HttpServlet {

	private static final long serialVersionUID = 5436501449694635011L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		String segIntel = req.getParameter("intelligence");//segment分词的结果
		//new String(req.getParameter("intelligence").getBytes("ISO8859-1"), "UTF-8");
		//System.out.println("segIntel"+segIntel);
		String sentence = (String)session.getAttribute("rawIntel");
		System.out.println(sentence);
		List<String> segWords = new ArrayList<String> (Arrays.asList(segIntel.split(" ")));//分词后各项词的list
		/* 保证以句号结尾 */
		if ( !segWords.get(segWords.size() - 1).equals("。") )
			segWords.add("。");
		session.setAttribute("segWords", segWords);
		//System.out.println("seg====="+segWords);
		List<TaggedWord> tagWords = Tagger.getInstance().tag(segWords);//词性标注
		
		
		session.setAttribute("tagWords", tagWords);//分词后标注结果list
		
		System.out.println("tagWords======="+tagWords);
		List<Entry<String, String>> trunks = Annotate.getInstance().annotateEntity(tagWords,sentence);//标注实体
		System.out.println("trunks===="+trunks);
		session.setAttribute("segIntel", segIntel);
		session.setAttribute("trunks", trunks);
		
//		req.getRequestDispatcher("index3.jsp").forward(req, resp);
		req.getRequestDispatcher("step2.jsp").forward(req, resp);
	}
}
