<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>实体详情</title>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap-theme.min.css"/>
	<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="./css/main.css"/>
	<link rel="stylesheet" type="text/css" href="css/default.css" />
	<link rel="stylesheet" type="text/css" href="css/component.css" />
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.min.js"></script>
	<script src="js/modernizr.custom.js"></script>
	<script type="text/javascript" src="./js/lodash.js"></script>
	
</head>
<body>
	<div class="container" >
	
		<img id="loading" src='./images/loading.gif' style='display:none'/>
		<div id ="left" class = "col-md-4" style="display:none;">
			<div id="imgPane" style="display:block;overflow:auto;background-color:rgb(255, 255, 255);">
				<img id="instanceImg" width="370px" height="250" src="img/timg.jpg">
			</div>	
		</div>
		<div id="entityInforResult" class="middle" style="float:left;margin:0px auto;display:none">
			
			<h3 id="entityName"></h3>
			<ul id="attributes"></ul>
			
		</div>
	</div>
</body>
<script>
$(function(){
	var queryUrl=window.location.href.split("?")[1];
	//此处为接口3
	console.log("接口3:",{queryUrl:queryUrl})
	$.ajax({
		url:"EntityInfoServlet",
		data:{queryUrl:queryUrl},
		success:function(info){
			var left = $('#left');
			var entityName = $('#entityName');
			var attributes = $('#attributes');
			var entityInforResult = $('#entityInforResult');
			left.hide();
			
			$("#loading").hide();
		//	console.log(info);
			var obj = info;
			/*var obj={label:"YF-12战斗机@zh",
					url:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/yf_12",
					author:"china",
					price:"$8900",
					city:"nanjing",
					density:"east",
					isPeace:"peace",
					isImportant:"is",
					o:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/yf_12"
					};*/
			for(var p in obj){
				if(p==="名称"){
					entityName.text(obj[p]);
				}else{
					if(obj[p].indexOf("http")>-1){
						var queryStr="entityInformation.jsp?"+obj[p];
						obj[p]="<a href="+queryStr+" target='_blank'>"+obj[p]+"</a>";
					}
					var str="<span>"+p+"：</span><span>"+obj[p]+"</span>";
					attributes.append('<li>'+str+'</li>');
				}
			}
			left.fadeIn(2000);		
			entityInforResult.fadeIn(2000);	
		}
		
	})
})
</script>
</html>