<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="./css/bootstrap-theme.min.css"/>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="./css/main.css"/>
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<script type="text/javascript" src="./js/lodash.js"></script>
<script type="text/javascript" src="./js/layer/layer.js"></script>
</head>
<body>
<header class="navbar navbar-static-top mili-nav" id="top">
	<div class="container">
		<nav id="bs-navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li class="active">
					<a href="../EntityMatch/">实体关联系统</a>
				</li>
				<li>
					<a href="../EntityMatch/TemporalReasonerServlet">时间关联系统</a>
				</li>
				<li>
					<a href="../EntityMatch/search.jsp">关联搜索系统</a>
				</li>
			</ul>
		</nav>
	</div>
</header>


<div class="container">
<div class="row-fluid">
	<div class="span8"><br></div>
	<div class="span4"></div>
</div>
<div class="row-fluid">
<div class="span6">
			<div class="hero-unit well">
				<h2>
					实体关联系统
				</h2>
				<p>
					军事领域相关的实体关联系统。
				</p>
			</div>
</div>
<h4 id="tip">实体正在匹配中,请稍候...</h4>
<div id = "result">
	<span id="pre" style="font-size:15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
	<span id="rec" style="font-size:15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
	<span id="f" style="font-size:15px"></span>
</div>
<table class="table table-bordered table-hover" id="resultTable">
</table>
<ul id="pagination-clean">
    <li class="previous-off">&laquo; Previous</li>
    <li class="next">Next &raquo;</li>
  </ul>
  <input type="hidden" id="currentPage" name="currentPage"/>
  <input type="hidden" id="totalPage" name="totalPage"/>
</div>
</div>
</body>
<table class="table table-bordered table-hover" id="propertyTable" style="display:none">
</table>
<script>
$(function(){
	var dataArray=[];
	$.ajax({
		url:"MatchServlet",
		dataType:'text',
		data:{isAjax:1},
		success:function(data){
			$("#tip").html("匹配完成");
		//	var datas=data.split("&&&");
		//	var resData=JSON.parse(datas[0]);
		//	var jsonData=JSON.parse(datas[1]);
		//	$("#pre").html("Precision:"+resData.pre);
		//	$("#rec").html("Recall:"+resData.rec);
		//	$("#f").html("F1:"+resData.f);
			var jsonData=JSON.parse(data);
			$("#currentPage").val(1);
			$("#totalPage").val(jsonData.length/10+1);
			dataArray=_.chunk(jsonData,10);
			$("#resultTable").append("<caption>详细结果</caption><thead><tr><th>实体1</th><th>实体2</th><th>相似度</th></tr></thead><tbody>")
			_.each(dataArray[0],function(e){
				var appendStr="<tr><td>"+e.entity1.subject+"</td><td>"+e.entity2.subject+"</td><td>"+e.sim.toFixed(4)+"</td></tr>";
				$("#resultTable").append(appendStr);
			})
			$("#resultTable").append("</tbody>")
			$("#pagination-clean").show();
			if($("#totalPage").val()=="1"){
				$(".next").removeClass("next").addClass("next-off")
			}
		}
	});
	
	
	$(document).on("click",".next",function(){
		$(".previous-off").removeClass("previous-off").addClass("previous");
		var nextPage=parseInt($("#currentPage").val());
		if(nextPage==parseInt($("#totalPage").val())-1){
			$(".next").removeClass("next").addClass("next-off")
		}
		$("#resultTable").empty();
		$("#resultTable").append("<caption>详细结果</caption><thead><tr><th>实体1</th><th>实体2</th><th>相似度</th></tr></thead><tbody>")
		_.each(dataArray[nextPage],function(e){
			var appendStr="<tr><td>"+e.entity1.subject+"</td><td>"+e.entity2.subject+"</td><td>"+e.sim.toFixed(4)+"</td></tr>";
			$("#resultTable").append(appendStr);
		})
		$("#resultTable").append("</tbody>")
		$("#currentPage").val(nextPage+1);
	})
	
	$(document).on("click",".previous",function(){
		$(".next-off").removeClass("next-off").addClass("next");
		var prevPage=parseInt($("#currentPage").val())-1;
		if(prevPage==1){
			$(".previous").removeClass("previous").addClass("previous-off")
		}
		$("#resultTable").empty();
		$("#resultTable").append("<caption>详细结果</caption><thead><tr><th>实体1</th><th>实体2</th><th>相似度</th></tr></thead><tbody>")
		_.each(dataArray[prevPage-1],function(e){
			var appendStr="<tr><td>"+e.entity1.subject+"</td><td>"+e.entity2.subject+"</td><td>"+e.sim.toFixed(4)+"</td></tr>";
			$("#resultTable").append(appendStr);
		})
		$("#resultTable").append("</tbody>")
		$("#currentPage").val(prevPage);
	})
/*	$(document).on("click","td",function(){
		var row=$(this).parent().index();
		var curPage=parseInt($("#currentPage").val());
		var Array=dataArray[curPage-1];
		$("#entity1propertyTable").empty();
		$("#entity1propertyTable").append("<thead><tr><th>属性</th><th>取值</th></tr></thead><tbody>");
		for(var key in Array[row].entity1.predicate_object){
			var appendStr = "<tr><td>"+key+"</td><td>"+Array[row].entity1.predicate_object[key]+"</td></tr>";
			$("#entity1propertyTable").append(appendStr);	
		}
		$("#entity1propertyTable").append("</tbody></table>");
		layer.open({
  		type: 1, 
  		area: '600px',
  		offset: ['5%','5%'],
  		shadeClose:true,
  		maxmin: true,
  		content: $("layui-layer1")
  	//	content: $("#entity1propertyTable")
		});
		$("#entity2propertyTable").empty();
		$("#entity2propertyTable").append("<thead><tr><th>属性</th><th>取值</th></tr></thead><tbody>");
		for(var key in Array[row].entity2.predicate_object){
			var appendStr = "<tr><td>"+key+"</td><td>"+Array[row].entity2.predicate_object[key][0]+"</td></tr>";
			$("#entity2propertyTable").append(appendStr);	
		}
		$("#entity2propertyTable").append("</tbody></table>");
		layer.open({
  		type: 1, 
  		area: '600px',
  		offset: ['5%','50%'],
  		maxmin:true,
  		content: $("layui-layer2")
  	//	content: $("#entity2propertyTable")
		});
	})
})*/
	$(document).on("click","td:first-child",function(){
		var row=$(this).parent().index();
		var curPage=parseInt($("#currentPage").val());
		var Array=dataArray[curPage-1];
		$("#propertyTable").empty();
		$("#propertyTable").append("<thead><tr><th>属性</th><th>取值</th></tr></thead><tbody>");
		for(var key in Array[row].entity1.predicate_object){
			var appendStr = "<tr><td>"+key+"</td><td>"+Array[row].entity1.predicate_object[key]+"</td></tr>";
			$("#propertyTable").append(appendStr);	
		}
		$("#propertyTable").append("</tbody>");
		layer.open({
  		type: 1, 
  		area: '800px',
  		maxmin: true,
  		content: $("#propertyTable")
		});
	})
	
	$(document).on("click","td:nth-child(2)",function(){
		var row=$(this).parent().index();
		var curPage=parseInt($("#currentPage").val());
		var Array=dataArray[curPage-1];
		$("#propertyTable").empty();
		$("#propertyTable").append("<thead><tr><th>属性</th><th>取值</th></tr></thead><tbody>");
		for(var key in Array[row].entity2.predicate_object){
			var appendStr = "<tr><td>"+key+"</td><td>"+Array[row].entity2.predicate_object[key][0]+"</td></tr>";
			$("#propertyTable").append(appendStr);	
		}
		$("#propertyTable").append("</tbody>");
		layer.open({
  		type: 1, 
  		area: '800px',
  		maxmin:true,
  		content: $("#propertyTable")
		});
	})
})
</script>
</html>