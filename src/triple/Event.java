package triple;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Event {
	public String EventName;
	public Date StartTime;
	public Date EndTime;
	public Event(String a,String b,String c) throws ParseException{
		EventName=a;
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		StartTime= sdf1.parse(b);
		EndTime= sdf1.parse(c);
	}
}
