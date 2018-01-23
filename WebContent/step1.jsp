<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type='text/javascript' src="js/segment.js"></script>
<title>情报分析——分词</title>
</head>
<body>
	
	<p>以下是分词结果，如果您不满意，可以手动修正，修正完毕请点“下一步”</p>
	<%  String segIntel = (String) session.getAttribute("segIntel"); %>
	<form action='AnnotateEntity' method='post'>
		<p><textarea id="segIntel" class="span12" name='intelligence' rows='10' cols='90'><%= segIntel %></textarea></p>		
	</form>
	
	<button class="btn btn-primary" id="prevBtn">上一步</button>
	<button class="btn btn-primary" id="nextBtn">下一步</button>
	

</body>
</html>