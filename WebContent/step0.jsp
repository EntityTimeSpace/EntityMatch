<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>情报解析——输入</title>
</head>
<body>
	
	
	<p>请输入相关情报：</p>
	<%
		String rawIntel = (String) session.getAttribute("rawIntel");
		System.out.println("rawIntel--------------"+rawIntel);
		boolean back = Boolean.parseBoolean(request.getParameter("back"));
	%>
	<p><textarea class="span12" id="rawIntel" name='intelligence' rows='10' cols='90'
		><%= (rawIntel == null || !back) ? "" : rawIntel %></textarea></p>
		
	<button class="btn" id="resetBtn">重置</button>
	<button class="btn btn-primary" id="nextBtn">下一步</button>	
</body>
</html>