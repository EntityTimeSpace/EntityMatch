package search;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDF;

import virtuoso.jena.driver.*;
 
public class VirtuosoSparql {
	
	private static final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
		    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
		    + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
		    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
	/**
	 * 根据时空关系返回军事新闻
	 * @param startTime
	 * @param endTime
	 * @param timeRel
	 * @param location
	 * @param relation
	 * @param distance
	 * @param keyWord
	 * @param pageNum
	 * @return
	 */
	public static String queryTimespace(String startTime,String endTime,String timeRel,String location,String relation,double distance,String keyWord,int pageNum){
		distance=distance*1.852/111;//转换成度数
		ResultSet results;
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		String locationprefix="PREFIX info: <http://ws.nju.edu.cn/ws/entitymatch/locations#>\n";
		VirtGraph vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/locations",url, usr, psd);
		String keyQuery = "";
		if(keyWord.trim().length()>0){
			if(keyWord.trim().equals("黄岩岛")){
				keyWord="黄岩";
			}
			keyQuery = " FILTER(bif:contains(?desc,'\""+keyWord+"\"'))"+". FILTER(bif:contains(?o,'\""+keyWord+"\"'))";
			
		}
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
		System.out.println("RelatedQuery------------"+RelatedQuery);
		VirtuosoQueryExecution RelExecution=VirtuosoQueryExecutionFactory.create(RelatedQuery, vg);
		results=RelExecution.execSelect();
		while(results.hasNext()){
			result.add(results.nextSolution().get("name").asLiteral().toString());
		}
		System.out.println("result====="+result);
		vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/news",url, usr, psd);
		int sumNum = 0;
		String query = " where { ?s news:startdate ?start. ?s news:enddate ?end. ?s news:content ?desc. ?s news:title ?o. ?s news:originurl ?pageurl.  FILTER(";
		for(String loc:result){
			//System.out.println(loc);
			if(loc.trim().equals("黄岩岛@zh")){
				query+="regex(?o,\""+"黄岩"+"\")||";
				continue;
			}
			query+="regex(?o,\""+loc.substring(0, loc.length()-3)+"\")||";
		}
		query = query.substring(0,query.length()-2);
		query+=")."+keyQuery+"}";
		VirtuosoQueryExecution executor = null;
		if(pageNum ==1){
				String sumQuery = prefix+"PREFIX news: <http://ws.nju.edu.cn/ws/entitymatch/news#>" +" SELECT (COUNT(?s) AS ?sumtotal) "+query;/* where {"
						+ "?s news:title ?label "
						+ "FILTER regex(?label,\"" + keyword + "\")}";*/
				//Query sumql = QueryFactory.create(sumQuery);
				System.out.println(sumQuery);
				executor = VirtuosoQueryExecutionFactory.create (sumQuery, vg);
				sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();		
		}		
		
		query+=" LIMIT 5 OFFSET "+ (pageNum-1)*5; 
		String resultquery = prefix+"  PREFIX news:  <http://ws.nju.edu.cn/ws/entitymatch/news#>"
				+" SELECT ?s ?o ?desc ?start ?end ?pageurl "+query;
		System.out.println(resultquery);
		//Query relQuery = QueryFactory.create(resultquery);
		VirtuosoQueryExecution excutor = VirtuosoQueryExecutionFactory.create(resultquery, vg);
		results=excutor.execSelect();
		QuerySolution relresult =null;
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			relresult = results.nextSolution();
			RDFNode uri = relresult.get("s");
			propertyList.put("uri", uri.toString());
			RDFNode title = relresult.get("o");
			propertyList.put("title", title.toString());
			propertyList.put("content", relresult.get("desc").toString());
			propertyList.put("startdate", relresult.get("start").toString());
			propertyList.put("enddate", relresult.get("end").toString());
			//System.out.println( result.get("pageurl").toString());
			propertyList.put("pageurl", relresult.get("pageurl").toString().substring(0,relresult.get("pageurl").toString().length()-3));
			propertyList.put("sumResult", sumNum+" ");
			answerList.add(propertyList);
		}
		excutor.close();
		Gson gs = new Gson();
		return gs.toJson(answerList);
	}
	/**
	 * 根据空间关系返回军事新闻
	 * @param location
	 * @param relation
	 * @param distance
	 * @param pageNum
	 * @return
	 */
	public static String queryRelatedLocation(String location,String relation,double distance,int pageNum){
		//distance 单位km
		distance=distance/111;//转换成度数
		ResultSet results;
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		String locationprefix="PREFIX info: <http://ws.nju.edu.cn/ws/entitymatch/locations#>\n";
		VirtGraph vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/locations",url, usr, psd);

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
		System.out.println("---------result-----"+result);
		vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/news",url, usr, psd);
		int sumNum = 0;
		String query = " where { ?s news:startdate ?start. ?s news:enddate ?end. ?s news:content ?desc. ?s news:title ?o. ?s news:originurl ?pageurl.  FILTER(";
		for(String loc:result){
			//System.out.println(loc);
			if(loc.trim().equals("黄岩岛@zh")){
				query+="regex(?o,\""+"黄岩"+"\")||";
				continue;
			}
			else{
				query+="regex(?o,\""+loc.substring(0, loc.length()-3)+"\")||";
			}
			
		}
		query = query.substring(0,query.length()-2);
		query+=")}";
		VirtuosoQueryExecution executor = null;
		System.out.println("query=========="+query);
		if(pageNum ==1){
				String sumQuery = prefix+"PREFIX news: <http://ws.nju.edu.cn/ws/entitymatch/news#>" +" SELECT (COUNT(?s) AS ?sumtotal) "+query;/* where {"
						+ "?s news:title ?label "
						+ "FILTER regex(?label,\"" + keyword + "\")}";*/
				Query sumql = QueryFactory.create(sumQuery);
				System.out.println("sumQuery====="+sumQuery);
				executor = VirtuosoQueryExecutionFactory.create (sumql, vg);
				sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();		
		}		
		
		query+=" LIMIT 5 OFFSET "+ (pageNum-1)*5; 
		String resultquery = prefix+"  PREFIX news:  <http://ws.nju.edu.cn/ws/entitymatch/news#>"
				+" SELECT ?s ?o ?desc ?start ?end ?pageurl "+query;
		//System.out.println(resultquery);
		Query relQuery = QueryFactory.create(resultquery);
		VirtuosoQueryExecution excutor = VirtuosoQueryExecutionFactory.create(relQuery, vg);
		results=excutor.execSelect();
		QuerySolution relresult =null;
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			relresult = results.nextSolution();
			RDFNode uri = relresult.get("s");
			propertyList.put("uri", uri.toString());
			RDFNode title = relresult.get("o");
			propertyList.put("title", title.toString());
			propertyList.put("content", relresult.get("desc").toString());
			propertyList.put("startdate", relresult.get("start").toString());
			propertyList.put("enddate", relresult.get("end").toString());
			//System.out.println( result.get("pageurl").toString());
			propertyList.put("pageurl", relresult.get("pageurl").toString().substring(0,relresult.get("pageurl").toString().length()-3));
			propertyList.put("sumResult", sumNum+" ");
			answerList.add(propertyList);
		}
		excutor.close();
		Gson gs = new Gson();
		return gs.toJson(answerList);
		//return result;		
	}
	public static String queryByKeyword(String keyword,int pageNum) throws DocumentException {
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		//连接服务器，两个“dba”分别是账号和密码，是初始状态Virtuoso的默认值
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
		int sumNum = 0;
		if(pageNum ==1){
			String sumQuery = prefix +" SELECT (COUNT(?s) AS ?sumtotal)  where {"
					+ "?s rdfs:label ?label "
					+ "FILTER regex(?label,\"" + keyword + "\")}";
			Query sumql = QueryFactory.create(sumQuery);
			VirtuosoQueryExecution executor = VirtuosoQueryExecutionFactory.create (sumql, vg);
			sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();		
		}
/*		String sumQuery = prefix +" SELECT (COUNT(?s) AS ?sumtotal)  where {"
				+ "?s rdfs:label ?label "
				+ "FILTER regex(?label,\"" + keyword + "\")}";
		Query sumql = QueryFactory.create(sumQuery);
		VirtuosoQueryExecution executor = VirtuosoQueryExecutionFactory.create (sumql, vg);
		int sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();*/
		
		String query = prefix + "SELECT ?s ?label WHERE {"
				+ "?s rdfs:label ?label "
				+ "FILTER regex(?label,\"" + keyword + "\")} LIMIT 5 OFFSET "+ (pageNum-1)*5; 
			//	+ "FILTER (regex(?label,\"" + keyword + "\") || regex(?label,\"战斗机\"))}";
		//System.out.println(query);
		Query sparql = QueryFactory.create(query);

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, vg);

		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			QuerySolution result = results.nextSolution();
		    RDFNode uri = result.get("s");
		    propertyList.put("uri", uri.toString());
		    RDFNode label = result.get("label");
		    propertyList.put("label",label.toString());
		    HashMap<String, String> map = (HashMap<String, String>) VirtuosoSparql.queryByUri(uri.asResource().getURI(),5);
		    for(String key : map.keySet()){ 
		    	propertyList.put(key, map.get(key));
	        }
		    propertyList.put("sumResult", sumNum+"");
		    answerList.add(propertyList);
		}
		
		vqe.close();
		Gson gs = new Gson();
		return gs.toJson(answerList);
	}
	
	
	/**
	 * 根据时间关系返回军事新闻
	 * @param startdate
	 * @param enddate
	 * @param rel
	 * @param keyword
	 * @param pageNum
	 * @return
	 */
	public static String queryByDateRel(String startdate,String enddate,String rel,String keyword,int pageNum){
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		String keyQuery = "";
		if(keyword.trim().length()>0){
			keyQuery = " FILTER(bif:contains(?o,'\""+keyword+"\"'))";
		}
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		VirtGraph vg = new VirtGraph ("http://ws.nju.edu.cn/ws/entitymatch/news",url, usr, psd);
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
 
	/**
	 * 根据uri返回实体属性集合
	 * @param uri
	 * @param resultNum
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String,String> queryByUri(String uri,int resultNum) throws DocumentException {
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
		//需要注意的一点是，获得的uri都是没有被"<>"包含的，所以在写query时，需要在uri的两边分别加上"<"和">"。
		String query;
		if(resultNum==0){
			query = prefix + "SELECT * WHERE { <" + uri + "> ?p ?o}";
		}else{
			query = prefix + "SELECT * WHERE { <" + uri + "> ?p ?o} LIMIT "+resultNum;
		}
		
	//	System.out.println(query);
		Query sparql = QueryFactory.create(query);
		Map<String,String> entityInfo = new HashMap<String,String>();
		//在服务器"vg"上执行查询语句"sparql"
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, vg);
		
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			String property = LabelUriBiMap.getLabelByUri(p.toString());
		//	String object = o.toString();
			String object = LabelUriBiMap.getLabelByUri(o.toString());
			object = (object == null)?o.toString():object;
			entityInfo.put(property, object);
		}
		vqe.close();
		return entityInfo;
	}
	
	
	
	public static String queryBySubject(String subject,int resultNum) throws DocumentException {
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
		//需要注意的一点是，获得的uri都是没有被"<>"包含的，所以在写query时，需要在uri的两边分别加上"<"和">"。
		String query;
		if(resultNum==0){
			query = prefix + "SELECT * WHERE { <" + subject + "> ?p ?o}";
		}else{
			query = prefix + "SELECT * WHERE { <" + subject + "> ?p ?o} LIMIT "+resultNum;
		}
		
	//	System.out.println(query);
		Query sparql = QueryFactory.create(query);
		Map<String,String> entityInfo = new HashMap<String,String>();
		//在服务器"vg"上执行查询语句"sparql"
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, vg);
		
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			String property = LabelUriBiMap.getLabelByUri(p.toString());
		//	String object = o.toString();
			String object = LabelUriBiMap.getLabelByUri(o.toString());
			object = (object == null)?o.toString():object;
			entityInfo.put(property, object);
		}
		vqe.close();
		Gson gs = new Gson();
		return gs.toJson(entityInfo);
	}
	/*private static String generateAndSparql(String property,List<String> value) {
		StringBuffer sparql = new StringBuffer();
		try {
			if("类型".equals(property)) {
				System.out.println(value);
				int i = value.size();
				if(i == 1) {
					sparql.append("?s <" + LabelUriBiMap.getUriByLabel(property) + "> <" + LabelUriBiMap.getUriByLabel(value.get(0)) + ">.");
				}
				else {
					sparql.append("{");
					sparql.append("{ ?s <" + LabelUriBiMap.getUriByLabel(property) + "> <" + LabelUriBiMap.getUriByLabel(value.get(0)) + "> }");
					for(int j = 1; j < i; j++) {
						sparql.append(" UNION ");
						sparql.append("{ ?s <" + LabelUriBiMap.getUriByLabel(property) + "> <" + LabelUriBiMap.getUriByLabel(value.get(j)) + "> }");
					}
					sparql.append("}");
				}
			}
			else {
				sparql.append("?s <" + LabelUriBiMap.getUriByLabel(property) + "> ?o");
				for(String v: value) {
					sparql.append(" FILTER regex(?o,\"" + v + "@zh\") ");
				}
		//				\"" + value + "\"@zh.";
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sparql.toString();
	}*/
	private static String generateAndSparql(String property,String value) {
		String sparql = null;
		try {
			if("类型".equals(property)) {
				//System.out.println(value);
				sparql = "?s <" + LabelUriBiMap.getUriByLabel(property) + "> <" + LabelUriBiMap.getUriByLabel(value) + ">.";
			}
			else {
				sparql = "?s <" + LabelUriBiMap.getUriByLabel(property) + "> \"" + value + "\"@zh.";
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sparql;
	}
	
	/*private static String generateOrSparql(String property,List<String> value) {
		StringBuffer sparql = new StringBuffer();
		try {
				sparql.append("OPTIONAL { ?s <" + LabelUriBiMap.getUriByLabel(property) + "> ?o");
				for(String v: value) {
					sparql.append(" FILTER regex(?o,\"" + v + "@zh\") ");
				}
				sparql.append(" }");
		//				\"" + value + "\"@zh.";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sparql.toString();
	}*/
	private static String generateOrSparql(String property,String value) {
		String sparql = null;
		try {
			sparql = "UNION { ?s <" + LabelUriBiMap.getUriByLabel(property) + "> \"" + value + "\"@zh }";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sparql;
		
	}
	private static String generateSparql(String property , String value){
		String sparql = null;
		try {
			sparql = " { ?s <" + LabelUriBiMap.getUriByLabel(property) + "> \"" + value + "\"@zh }";
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sparql;
	}
	/**
	 * 根据查询条件返回实体集合
	 * @param types
	 * @param keyword
	 * @param properties
	 * @param pageNum
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String queryBykeyAndproperty(String[] types,String keyword,String properties,int pageNum) throws IOException, DocumentException{
		properties = properties.replace(" ", "");
		String keyQuery = "";
		if(keyword.trim().length()>0){
			keyQuery = " FILTER(bif:contains(?label,'\""+keyword+"\"'))";
		}
		StringBuffer query = new  StringBuffer();
		for(String type:types){
			if(!type.equals(""))
			query.append(VirtuosoSparql.generateAndSparql("类型", type));
		}
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		//连接服务器，两个“dba”分别是账号和密码，是初始状态Virtuoso的默认值
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
		String[] strs = properties.replace("(", "*").replace(")", "*").replace("AND", "*").replace("OR", "*").replaceAll("\\*+", "*").split("\\*");
		String[] sparqls = new String[strs.length];
		properties = properties.replaceAll("[\\u4e00-\\u9fa5]+:[0-9\\u4e00-\\u9fa5]+", " * ").replace("(", "").replace(")", "").replace("AND", "  ").replace("OR", "UNION");
		for(String str:strs){
			if(str.length()>0){
				String[] pV = str.split(":");
				
				properties = properties.replaceFirst("\\*", generateSparql(pV[0], pV[1]));
			}
		}
		query.append(properties);
		String resultQuery = prefix + " SELECT ?s ?label WHERE {"+ " {?s rdfs:label ?label "+keyQuery+"} "+query.toString()+"}  LIMIT 5 OFFSET "+(pageNum-1)*5;
		//System.out.println("resultQuery==="+resultQuery);
		int sumNum = 0;
		if(pageNum ==1){
			String sumQuery = prefix +" SELECT (COUNT(?s) AS ?sumtotal)  where {"
					+ "{?s rdfs:label ?label "+keyQuery+"} "+query.toString()
					+ " } ";
			VirtuosoQueryExecution executor = VirtuosoQueryExecutionFactory.create (sumQuery, vg);
			System.out.println("sumQuery===="+sumQuery);
			sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();		
		}
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (resultQuery, vg);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			QuerySolution result = results.nextSolution();
		    RDFNode uri = result.get("s");
		    propertyList.put("uri", uri.toString());
		    RDFNode label = result.get("label");
		    propertyList.put("label",label.toString());
		    propertyList.put("sumResult", ""+sumNum);
		    HashMap<String, String> map = (HashMap<String, String>) VirtuosoSparql.queryByUri(uri.asResource().getURI(),5);
		    for(String key : map.keySet()){ 
		    	propertyList.put(key, map.get(key));
	        }
		    answerList.add(propertyList);
		}
		vqe.close();
		Gson gs = new Gson();
		return gs.toJson(answerList);
	}
	
	public static String queryByPropertyValue(String propertyValue,int pageNum) throws DocumentException {
		JsonParser jp = new JsonParser();
		JsonObject obj = (JsonObject) jp.parse(propertyValue);
		JsonArray andArray = obj.get("与").getAsJsonArray();
		JsonArray orArray = obj.get("或").getAsJsonArray();
		StringBuffer sumQuery = new StringBuffer();
		sumQuery.append(prefix+" SELECT (COUNT(?s) AS ?sumtotal)  where {");
		StringBuffer resultQuery = new StringBuffer();
		StringBuffer query = new StringBuffer();
		
		resultQuery.append(prefix + " SELECT ?s ?label WHERE {");
		for(int i = 0; i < andArray.size();i++) {
			JsonObject propertyValuePairs = andArray.get(i).getAsJsonObject();
			String property = propertyValuePairs.get("属性").getAsString();
		/*	JsonArray valueArray = propertyValuePairs.get("值").getAsJsonArray();
			List<String> value = new ArrayList<String>();
			for(int j = 0; j < valueArray.size(); j++) {
				value.add(valueArray.get(j).getAsString());
			}*/
			String value = propertyValuePairs.get("值").getAsString();
			query.append(VirtuosoSparql.generateAndSparql(property, value));
		}
		for(int i = 0; i < orArray.size();i++) {
			JsonObject propertyValuePairs = orArray.get(i).getAsJsonObject();
			String property = propertyValuePairs.get("属性").getAsString();
		/*	JsonArray valueArray = propertyValuePairs.get("值").getAsJsonArray();
			List<String> value = new ArrayList<String>();
			for(int j = 0; j < valueArray.size(); j++) {
				value.add(valueArray.get(j).getAsString());
			}*/
			String value = propertyValuePairs.get("值").getAsString();
			query.append(VirtuosoSparql.generateOrSparql(property, value));
		}
		query.append("?s rdfs:label ?label }");
		resultQuery.append(query+" LIMIT 5 OFFSET "+(pageNum-1)*5);
		sumQuery.append(query);
		//System.out.println(query.toString());
		List<HashMap<String, String>> answerList = new ArrayList<HashMap<String,String>>();
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
		Query sumspar = QueryFactory.create(sumQuery.toString());
		VirtuosoQueryExecution executor = VirtuosoQueryExecutionFactory.create(sumspar, vg);
		int sumNum = executor.execSelect().nextSolution().get("sumtotal").asLiteral().getInt();
		
		
		
		
		
		
		Query sparql = QueryFactory.create(resultQuery.toString());

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, vg);

		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			HashMap<String,String> propertyList = new HashMap<String,String>();
			QuerySolution result = results.nextSolution();
		    RDFNode uri = result.get("s");
		    propertyList.put("uri", uri.toString());
		    RDFNode label = result.get("label");
		    propertyList.put("label",label.toString());
		    propertyList.put("sumResult", ""+sumNum);
		    HashMap<String, String> map = (HashMap<String, String>) VirtuosoSparql.queryByUri(uri.asResource().getURI(),5);
		    for(String key : map.keySet()){ 
		    	propertyList.put(key, map.get(key));
	        }
		    //answerList.add((HashMap<String, String>) VirtuosoSparql.queryByUri(uri.asResource().getURI(),5));
		    answerList.add(propertyList);
		}
		vqe.close();
		Gson gs = new Gson();
		//System.out.println(gs.toJson(answerList));
		return gs.toJson(answerList);
	}
	
	public static void query(String query) {
		String url = VirtGraphLoader.getUrl(); 
		String usr = VirtGraphLoader.getUser();
		String psd = VirtGraphLoader.getPassword();
		VirtGraph vg = new VirtGraph ("http://caoermei/test",url, usr, psd);
	//	System.out.println(query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, vg);
		
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode s = result.get("s");
			RDFNode p = result.get("p");
			RDFNode o = result.get("o");
			System.out.println(s + ",p:" + p + ",o:" + o);
		}	
		vqe.close();
	}

	
	
	
	/*
	 * Executes a SPARQL query against a virtuoso url and prints results.
	*/
	public static void main(String[] args) throws DocumentException {
		//String query = prefix + 
			//	"SELECT ?s WHERE {"
			//	+ "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> \"武装直升机\"@zh."
			//	+ "?s <http://ws.nju.edu.cn/ws/miliqa/schema#Country_of_Citizenship> \"日本\"@zh."
				//		+ "OPTIONAL { ?s <http://ws.nju.edu.cn/ws/miliqa/schema#Flight_speed> \"亚音速\"@zh }"
						//		+ "}";
		//		 "SELECT ?s ?p ?o WHERE {"
		//		+ "?s a <http://ws.nju.edu.cn/qademo/Weapon/Aircraft/jlj>."
			//	+ "?s j.7:Flight_speed \"亚音速\"@zh."
			//	+ "?s j.7:Country_of_Citizenship \"美国\"@zh"
			//	+ "OPTIONAL { ?s j.7:Country_of_Citizenship \"美国\"@zh }"
			//	+ "?s ?p ?o"
			//	+ "?s rdfs:label ?label"
		//		+ "} ";
	//	VirtuosoSparql.query(query);
	//	VirtuosoSparql.queryBySubject(entry.getValue(),entry.getKey());
		//测试输入实体名搜索（基于单个关键字的搜索）
	//	System.out.println(VirtuosoSparql.queryByKeyword("直升机"));
	//	System.out.println(VirtuosoSparql.queryBySubject("http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/n1k1_j"));
	/*	Iterator<Map.Entry<String,String>> itro = mo.entrySet().iterator();
		while(itro.hasNext()) {
			Entry<String,String> entry= itro.next();
			System.out.println(entry.getKey() +"\t"+ entry.getValue());
		//	List<String> rs = VirtuosoSparql.queryBySubject(entry.getValue(),entry.getKey());
		//	for(String s:rs) {
		//		System.out.println(s);
		//	}
		}*/
/*		String queryJson = "{\"与\":[{\"属性\":\"类型\",\"值\":战斗机};"
				+ "{\"属性\":\"国籍\",\"值\":德国}];"
				+ "\"或\":[{\"属性\":\"飞行速度\",\"值\":亚音速}]}";
		//System.out.println(VirtuosoSparql.queryByPropertyValue(queryJson,0));
		
		Pattern pattern =  Pattern.compile("^[\u0391-\uFFE5]+$");
		Matcher matcher = pattern.matcher("国籍{国籍:.国籍:}OPTIONAL{国籍:OPTIONAL国籍:}truefalse国籍");
		System.out.println(matcher.matches());*/
		//System.out.println(VirtuosoSparql.queryRelatedLocation("南海", "附近", 18888888.00).toString());
	}
}
