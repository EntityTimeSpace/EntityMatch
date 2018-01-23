<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>资讯详情</title>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap-theme.min.css"/>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="./css/main.css"/>
	<link rel="stylesheet" type="text/css" href="css/default.css" />
	<link rel="stylesheet" type="text/css" href="css/component.css" />
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.min.js"></script>
	<script src="js/modernizr.custom.js"></script>
	<script type="text/javascript" src="./js/lodash.js"></script>
	<link href="css/app.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/animate.css" />
</head>
<body >
	<div class="container" >
	
		<img id="loading" src='./images/loading.gif' style='display:none'/>
<!-- 		<div id ="left" class = "col-md-4" style="display:none;">
			<div id="imgPane" style="display:block;overflow:auto;background-color:rgb(255, 255, 255);">
				<img id="instanceImg" src="http://arsenal.chinaiiss.com/attachment/image/201209/06/145756_359.gif">
			</div>
		</div> -->
			<div id="newsInfoResult" class="middle" style="font-size:16px;display:none">
	<!-- 			<h3 id="entityName"></h3>
				<ul id="attributes"></ul> -->
			</div>
	</div>
</body>
<script>
$(function(){
	var queryUrl=window.location.href.split("?")[1];
	//此处为接口3
	$.ajax({
		url:"newsDetail",
		type:'get',
		data:{queryUrl:queryUrl},
		success:function(info){
			
			var result = $('#newsInfoResult');
			result.empty();
			result.hide();
            if(info.length<=0){
                //console.log('data-------'+jsonData.length);
                result.append('<div class="animated fadeInLeft"><div class="not-found-content"><div class="not-found"><img src="css/images/404.png" alt="" /><div class=""><h1>404</h1><p>对不起！您要访问的页面不存在！</p></div></div></div></div>');
                result.show();
                return;
            }
			
			result.html('<div class="animated fadeInLeft">'+info+"</div>");
			
			$("#loading").hide();

			result.fadeIn("slow");
		},
        error:function(request,textStatus,errorThrown){
            $('#loading').hide();
            var entityResult = $('#newsInfoResult');
            entityResult.append('<div class="animated fadeInLeft"><div class="not-found-content"><div class="not-found"><img src="css/images/404.png" alt="" /><div class=""><h1>404</h1><p>对不起！您要访问的页面不存在！</p></div></div></div></div>');

        }
		
	})
})
</script>
</html>