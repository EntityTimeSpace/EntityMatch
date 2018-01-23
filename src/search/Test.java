package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import java.util.Scanner;
public class Test {
	private static final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
		    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
		    + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
		    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
	private static final String url = "jdbc:virtuoso://localhost:1111";
	private static final String user = "dba";
	private static final String password = "dba";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("输入地点\t关系\t距离值：");
		Scanner sc = new Scanner(System.in);
		String location = sc.next();
		String relation = sc.next();
		double distance = Double.parseDouble(sc.next());
		distance=distance/111;//转换成度数
		ResultSet results;
		String locationprefix="PREFIX info: <http://ws.nju.edu.cn/ws/entitymatch/locations#>\n";
		VirtGraph vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/locations",url, user, password);

		ArrayList<String> result=new ArrayList<String>();
		//relation={“东、西、南、北、东南、东北、西南、西北、附近”}
		String LocQueryStr=locationprefix 
				+"select * where {\n"
				+"?y info:location \""+location+"\"@zh.\n"
				+"?y info:latitude_S ?ls.\n"
				+"?y info:longitude_W ?lw.\n"
				+"?y info:longitude_E ?le.\n"
				+"?y info:latitude_N ?ln.\n}";
		//System.out.println();
		Query LocQuery=QueryFactory.create(LocQueryStr);
		VirtuosoQueryExecution RelatedExecution=VirtuosoQueryExecutionFactory.create(LocQuery, vg);
		results=RelatedExecution.execSelect();
		//if(!results.hasNext()){return result;}
		
		QuerySolution locationsolution = results.nextSolution();
		//if(locationsolution==null){return result;}

		String LocationLongitude_E=locationsolution.get("le").asLiteral().toString().split("\\^")[0];
		String LocationLongitude_W=locationsolution.get("lw").asLiteral().toString().split("\\^")[0];
		String LocationLatitude_S=locationsolution.get("ls").asLiteral().toString().split("\\^")[0];
		String LocationLatitude_N=locationsolution.get("ln").asLiteral().toString().split("\\^")[0];
		
		String RelatedQueryStr=prefix + locationprefix 
				+"SELECT * WHERE{"
				+"?y info:location ?name.";
		String condition="";
		
	
		switch (relation) {
		case "东":
			condition+="?y info:longitude_E ?le."
					  +"FILTER ("
					  +"xsd:decimal(?le)<=180"
					  +"&& xsd:decimal(?le)>=-180"
					  +"&& xsd:decimal(?le)<="+LocationLongitude_E
					  +"&& xsd:decimal(?le)>="+(Double.parseDouble(LocationLongitude_E)-distance)+" )}";
			break;
		case "西":
			condition+="?y info:longitude_W ?lw."
					  +"FILTER ("
					  +"xsd:decimal(?lw)<=180"
					  +"&& xsd:decimal(?lw)>=-180"
					  +"&& xsd:decimal(?lw)>="+LocationLongitude_W
			          +"&& xsd:decimal(?lw)<="+(Double.parseDouble(LocationLongitude_W)+distance)+" )}";
			break;
		case "南":
			condition+="?y info:latitude_S ?ls."
					  +"FILTER ("
					  +"xsd:decimal(?ls)<=180"
					  +"&& xsd:decimal(?ls)>=-180"
					  +"&& xsd:decimal(?ls)<="+LocationLatitude_S
					  +"&& xsd:decimal(?ls)>="+(Double.parseDouble(LocationLatitude_S)-distance)+" )}";
			break;
		case "北":
			condition+="?y info:latitude_N ?ln."
					  +"FILTER ("
					  +"xsd:decimal(?ln)<=180"
					  +"&& xsd:decimal(?ln)>=-180"
					  +"&& xsd:decimal(?ln)>="+LocationLatitude_N
					  +"&& xsd:decimal(?ln)<="+(Double.parseDouble(LocationLatitude_N)+distance)+" )}";
			break;
		case "东南":
			condition+="?y info:longitude_E ?le."
					  +"?y info:latitude_S ?ls."
					  +"FILTER ("
					  +"xsd:decimal(?le)<=180"
					  +"&& xsd:decimal(?le)>=-180"
					  +"&& xsd:decimal(?ls)<=180"
					  +"&& xsd:decimal(?ls)>=-180"
					  +"&& xsd:decimal(?le)<="+LocationLongitude_E
					  +"&& xsd:decimal(?le)>="+(Double.parseDouble(LocationLongitude_E)-distance)
					  +"&& xsd:decimal(?ls)>="+(Double.parseDouble(LocationLatitude_S)-distance)
					  +"&& xsd:decimal(?ls)<="+LocationLatitude_S+")}";
			break;
		case "东北":
			condition+="?y info:longitude_E ?le."
					  +"?y info:latitude_N ?ln."
					  +"FILTER ("
					  +"xsd:decimal(?le)<=180"
					  +"&& xsd:decimal(?le)>=-180"
					  +"&& xsd:decimal(?ln)<=180"
					  +"&& xsd:decimal(?ln)>=-180"
					  +"&& xsd:decimal(?le)<="+LocationLongitude_E
					  +"&& xsd:decimal(?le)>="+(Double.parseDouble(LocationLongitude_E)-distance)
					  +"&& xsd:decimal(?ln)<="+(Double.parseDouble(LocationLatitude_N)+distance)
					  +"&& xsd:decimal(?ln)>="+LocationLatitude_N+")}";
			break;
		case "西南":
			condition+="?y info:longitude_W ?lw."
					  +"?y info:latitude_S ?ls."
					  +"FILTER ("
					  +"xsd:decimal(?lw)<=180"
					  +"&& xsd:decimal(?lw)>=-180"
					  +"&& xsd:decimal(?ls)<=180"
					  +"&& xsd:decimal(?ls)>=-180"
					  +"&& xsd:decimal(?lw)>="+LocationLongitude_W
					  +"&& xsd:decimal(?lw)<="+(Double.parseDouble(LocationLongitude_W)+distance)
					  +"&& xsd:decimal(?ls)>="+(Double.parseDouble(LocationLatitude_S)-distance)
					  +"&& xsd:decimal(?ls)<="+LocationLatitude_S+")}";
			break;
		case "西北":
			condition+="?y info:longitude_W ?lw."
					  +"?y info:latitude_N ?ln."
					  +"FILTER ("
					  +"xsd:decimal(?lw)<=180"
					  +"&& xsd:decimal(?lw)>=-180"
					  +"&& xsd:decimal(?ln)<=180"
					  +"&& xsd:decimal(?ln)>=-180"
					  +"&& xsd:decimal(?lw)>="+LocationLongitude_W
					  +"&& xsd:decimal(?lw)<="+(Double.parseDouble(LocationLongitude_W)+distance)
					  +"&& xsd:decimal(?ln)<="+(Double.parseDouble(LocationLatitude_N)+distance)
					  +"&& xsd:decimal(?ln)>="+LocationLatitude_N+")}";
			break;
		case "附近":
			condition+="?y info:longitude_W ?lw."
					  +"?y info:latitude_N ?ln."
					  +"?y info:longitude_E ?le."
					  +"?y info:latitude_S ?ls."
					  +"FILTER ("
					  +"xsd:decimal(?lw)<=180"
					  +"&& xsd:decimal(?lw)>=-180"
					  +"&& xsd:decimal(?ls)<=180"
					  +"&& xsd:decimal(?ls)>=-180"
					  +"&& xsd:decimal(?ln)<=180"
					  +"&& xsd:decimal(?ln)>=-180"
					  +"&& xsd:decimal(?le)<=180"
					  +"&& xsd:decimal(?le)>=-180"
					  +"&& xsd:decimal(?lw)<="+(Double.parseDouble(LocationLongitude_W)+distance)
					  +"&& xsd:decimal(?lw)>="+(Double.parseDouble(LocationLongitude_W)-distance)
					  +"&& xsd:decimal(?ln)<="+(Double.parseDouble(LocationLatitude_N)+distance)
					  +"&& xsd:decimal(?ln)>="+(Double.parseDouble(LocationLatitude_N)-distance)
					  +"&& xsd:decimal(?le)<="+(Double.parseDouble(LocationLongitude_E)+distance)
					  +"&& xsd:decimal(?le)>="+(Double.parseDouble(LocationLongitude_E)-distance)
					  +"&& xsd:decimal(?ls)<="+(Double.parseDouble(LocationLatitude_S)+distance)
					  +"&& xsd:decimal(?ls)>="+(Double.parseDouble(LocationLatitude_S)-distance)+")}";
			break;
		default:
			break;
		}
		
		RelatedQueryStr+=condition;
		//System.out.println(RelatedQueryStr);
		Query RelatedQuery=QueryFactory.create(RelatedQueryStr);
		VirtuosoQueryExecution RelExecution=VirtuosoQueryExecutionFactory.create(RelatedQuery, vg);
		results=RelExecution.execSelect();
		while(results.hasNext()){
			result.add(results.nextSolution().get("name").asLiteral().toString());
		}
		System.out.println("result："+result);
	}
	
	public static String queryByDateRel(String startdate,String enddate,String rel,String keyword,int pageNum){
		String keyQuery = "";
		if(keyword.trim().length()>0){
			keyQuery = " FILTER(bif:contains(?o,'\""+keyword+"\"'))";
		}
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		VirtGraph vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/news",url, user, password);
		startdate = "xsd:date(\""+startdate+"\")";
		enddate = "xsd:date(\""+enddate+"\")";
		
		String resultquery = prefix+"  PREFIX news:  <http://ws.nju.edu.cn/ws/entitymatch/news#> "
				+" SELECT ?s ?o ?desc ?start ?end ?pageurl where{";
		String query = "?s news:startdate ?start. ?s news:enddate ?end. ?s news:content ?desc. ?s news:title ?o. ?s news:originurl ?pageurl. ";//?o  bif:contains \"'"+keyword+"'\".
		switch(rel){
		case "Before":query = query+"FILTER(xsd:date(?end)<"+startdate+") ";	
			break;
		case "Meets":query = query +"FILTER(xsd:date(?end)="+startdate+") ";
			break;
		case "Overlaps":query = query +"FILTER(xsd:date(?start)<"+startdate+") "+"FILTER(xsd:date(?end)<"+enddate+") "+"FILTER(xsd:date(?end)>"+startdate+") ";
			break;
		case "Starts":query = query +"FILTER(xsd:date(?start)="+startdate+") "+"FILTER(xsd:date(?end)<"+enddate+") ";
			break;
		case "During":query = query +"FILTER(xsd:date(?start)>"+startdate+") "+"FILTER(xsd:date(?end)<"+enddate+") ";
			break;
		case "Finishes":query = query+"FILTER(xsd:date(?start)>"+startdate+") "+"FILTER(xsd:date(?end)="+enddate+") ";
			break;
		case "Equals":query = query+"FILTER(xsd:date(?start)="+startdate+") "+"FILTER(xsd:date(?end)="+enddate+") ";
			break;
		case "After":query = query +"FILTER(xsd:date(?start)>"+enddate+") ";
			break;
		case "MetBy":query = query+"FILTER(xsd:date(?start)="+enddate+") ";
			break;
		case "OverlappedBy":query = query +"FILTER(xsd:date(?start)>"+startdate+") "+"FILTER(xsd:date(?end)>"+enddate+") "+"FILTER(xsd:date(?end)<"+startdate+") ";
			break;
		case "StartedBy":query =query+"FILTER(xsd:date(?start)="+startdate+") "+"FILTER(xsd:date(?end)>"+enddate+") ";
			break;
		case "Contains":query = query+"FILTER(xsd:date(?start)<"+startdate+") "+"FILTER(xsd:date(?end)>"+enddate+") ";
			break;
		case "FinishedBy":query= query +"FILTER(xsd:date(?start)<"+startdate+") "+"FILTER(xsd:date(?end)="+enddate+") ";
			break;
		default:
			break;
		}
		query = query+keyQuery+" } ";
		//System.out.println("query===="+query);
		String sumResults =null;
		if(pageNum == 1){
			String sumquery = prefix+ " PREFIX news:  <http://ws.nju.edu.cn/ws/entitymatch/news#>"+" SELECT (COUNT(?s) AS ?sumtotal)  where {";
			sumquery += query;
			
			//Query sumQuery = QueryFactory.create(sumquery);
			System.out.println("sum==="+sumquery);
			VirtuosoQueryExecution sumExcutor = VirtuosoQueryExecutionFactory.create(sumquery, vg);
			
			sumResults = sumExcutor.execSelect().nextSolution().get("sumtotal").asLiteral().getString();
			
		}
		query = query+"LIMIT 5 OFFSET "+ (pageNum-1)*5;
		resultquery += query;
		
		System.out.println("query===="+resultquery);
		
		//Query relQuery = QueryFactory.create(resultquery);
		VirtuosoQueryExecution excutor = VirtuosoQueryExecutionFactory.create(resultquery, vg);
		ResultSet results=excutor.execSelect();
		QuerySolution result =null;
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			result = results.nextSolution();
			RDFNode uri = result.get("s");
			propertyList.put("uri", uri.toString());
			RDFNode title = result.get("o");
			propertyList.put("title", title.toString());
			propertyList.put("content", result.get("desc").toString());
			propertyList.put("startdate", result.get("start").toString());
			propertyList.put("enddate", result.get("end").toString());
			//System.out.println( result.get("pageurl").toString());
			propertyList.put("pageurl", result.get("pageurl").toString().substring(0,result.get("pageurl").toString().length()-3));
			propertyList.put("sumResult", sumResults+" ");
			answerList.add(propertyList);
		}
		excutor.close();
		Gson gs = new Gson();
		return gs.toJson(answerList);
	}

}
