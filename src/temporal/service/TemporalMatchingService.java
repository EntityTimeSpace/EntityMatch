package temporal.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import temporal.triple.Event;
import temporal.triple.mytriple;

public class TemporalMatchingService {
	static public String matching(BufferedReader reader, String userInput) {
		try {String fileString = "hello";
		String StartTime="";
		String EndTime="";
		String Title="";
		int i=1;
		ArrayList<Event> news_events=new ArrayList<Event>();
		while ((fileString = reader.readLine()) != null) {
			System.out.println(fileString);
			if(i%7==1){
				System.out.println(fileString);
				if(fileString.startsWith("标题")){
					Title=fileString;
					}
				}
			else if(i%7==4){
				System.out.println(fileString);
				if(fileString.startsWith("起始时间"))
					StartTime=fileString.split("：")[1].trim();
			}
			else if(i%7==5){
				System.out.println(fileString);
				if(fileString.startsWith("终止时间"))
					EndTime=fileString.split("：")[1].trim();
			}
			i++;
			if(i%7==0)
				news_events.add(new Event(Title, StartTime, EndTime));
		}
		
		//Event news_event=new Event();
		
        Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([时|点])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);  
        Matcher m = p.matcher(userInput);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
        	strs.add(m.group());         
        }
        Event user_event=new Event("用户输入", strs.get(0), strs.get(1));
        ArrayList<Event> events=new ArrayList<>();
        String resultStr="";
        
        
        //System.out.println(news_events.size());
        for(Event et: news_events){
        	events.add(et);	
        	events.add(user_event);
        	ArrayList<mytriple> result= temporal.triple.mytemp.GetContrainNetWork(events);
	        for(mytriple r:result){
	        	System.out.println(r.pred);
	        	switch(r.pred){
	        	case "Before":resultStr=resultStr+r.obj+"发生在用户事件之前\n新闻事件-Before-用户输入情报事件\n";break;
	        	case "After":resultStr=resultStr+r.obj+"发生在用户事件之后\n新闻事件-After-用户输入情报事件\n";break;
	        	case "Meets":resultStr=resultStr+r.obj+"与用户事件相遇\n新闻事件-Meets-用户输入情报事件\n";break;
	        	case "MetBy":resultStr=resultStr+r.obj+"与用户事件相遇\n新闻事件-MetBy-用户输入情报事件\n";break;
	        	case "Overlaps":resultStr=resultStr+r.obj+"被用户事件覆盖\n新闻事件-Overlaps-用户输入情报事件\n";break;
	        	case "OverlappedBy":resultStr=resultStr+r.obj+"覆盖用户事件\n新闻事件-OverlappedBy-用户输入情报事件\n";break;
	        	case "Starts":resultStr=resultStr+r.obj+"事件期间发生用户事件。（两个事件起始时间相同）\n新闻事件-Starts-用户输入情报事件\n\n";break;
	        	case "StartedBy":resultStr=resultStr+"用户事件在"+r.obj+"事件期间发生。（两个事件起始时间相同）\n新闻事件-StartedBy-用户输入情报事件\n\n";break;
	        	case "During":resultStr=resultStr+r.obj+"发生在用户事件期间。\n新闻事件-During-用户输入情报事件\n";break;
	        	case "Contains":resultStr=resultStr+r.obj+"包含了用户事件。\n新闻事件-Contains-用户输入情报事件\n";break;
	        	case "Finishes":resultStr=resultStr+r.obj+"先发生与用户事件同时结束。\n新闻事件-Finishes-用户输入情报事件\n";break;
	        	case "FinishedBy":resultStr=resultStr+r.obj+"后发生与用户事件同时结束。\n新闻事件-FinishedBy-用户输入情报事件\n";break;
	        	case "Equals":resultStr=resultStr+r.obj+"与用户事件同时发生并且同时结束。\n新闻事件-Equals-用户输入情报事件\n";break;
	        	}
	        }
	        events.clear();
        }
		    return resultStr;
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
	}
}
