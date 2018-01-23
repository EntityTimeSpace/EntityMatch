package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import edu.stanford.nlp.ling.Sentence;
import nlp.Segmenter;

/**
 * 分词servlet
 * @author Da Huang (dhuang.cn@gmail.com)
 *
 */
public class SegmentServlet extends HttpServlet {

	private static final long serialVersionUID = -7983747477650244285L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String rawIntel = req.getParameter("intelligence");// new String(req.getParameter("intelligence").getBytes("ISO8859-1"), "UTF-8");
		HttpSession session = req.getSession();
		session.setAttribute("rawIntel", rawIntel);
		System.out.println("rawIntel"+rawIntel);
		List<Term> termList = HanLP.segment(rawIntel);
		List<String> segs = new ArrayList();
		for(Term term:termList){
			segs.add(term.word.toString());
		}
		System.out.println("termList"+segs);
		String segIntel = Sentence.listToString(Segmenter.getInstance().amend(segs));
		
		System.out.println("segIntel===="+segIntel);
		session.setAttribute("segIntel", segIntel);
		
//		req.getRequestDispatcher("index2.jsp").forward(req, resp);
		req.getRequestDispatcher("step1.jsp").forward(req, resp);
	}

	@Override
	public void init() throws ServletException {
		Segmenter.getInstance();
	}
	public static void main(String[] args) {
		String str = "装备:美国海军“林肯”号核动力航母战斗群(第9航母战斗群)、“小鹰”号航母战斗群、“林肯”号“尼米兹”级核动力航空母舰(CVN72)、第2航母舰载机联队、第9驱逐舰中队、"
				+ "“莫比尔湾”号“提康德罗加”级导弹巡洋舰(CG53)、“拉塞尔”号“伯克”级导弹驱逐舰(DDG59)、“肖普”号“伯克”级导弹驱逐舰(DDG86)"
			+"兵力:2万多名美军、美韩两国陆、海、空、海军陆战队、70余艘各型战舰、80余架各型战机"+
			"目的/意义:主要目的是，让美军在朝鲜半岛发生异常突发事件时可以更加快速有效地展开增援行动，体现美方对于协防韩国的决心，进而更好地履行其对于朝鲜半岛安全的承诺。"+ 
		"comment:“阿尔索伊”(RSOI)演习，又名“联合战时增援”演习。“阿尔索伊”一般是每年的3月到4月进行，演习内容是，一旦朝鲜半岛发生战争，美军对韩国军队进行“接收、集结、前运和整合”（RSOI）"
		+ "这一演习是美韩两军自1994年起每年举行一次的例行性演习。2002年，“阿尔索伊”军事演习成为朝鲜半岛最大规模的军事行动，当时有1万多名驻太平洋地区美军和3万多名韩军官兵参加了这一演习。"
		+ "“鹞鹰”演习，是美韩两国军队之间的年度性三军联合演习，演习以野外机动训练和特种作战为主，一般在每年10月进行。“鹞鹰”演习始于1961年，2002年并入“阿尔索伊”。美国军方称，“鹞鹰”军事演习将检验美军在该地区的威慑能力。";
			
//		String str = "2002年10月12日";
//		System.out.println( Sentence.listToString(Segmenter.getInstance().segment(str)));
		List<Term> termList = HanLP.segment("土耳其总统埃尔多安,“一带一路”倡议");
		for(Term term:termList){
			
			System.out.print(term+"     ");
			//segs.add(term.word.toString());
		}
	}
}
