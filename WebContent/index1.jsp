<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>实体关联</title>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen">
	<link rel="stylesheet" type="text/css" href="css/main.css" media="screen">
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
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
		<div class="span6">
			<div class="hero-unit well">
				<form class="form" enctype="multipart/form-data" action="MatchServlet" method="post">
					<div class="form-group">
						<label for="text" class="row-fluid">请选择文件</label>
					</div>
					<div class="form-group">
						<label for="text" class="row-fluid">文件1</label>
						<input type="file" id="inputfile1" name="inputfile1">
  					</div>
  					<div class="form-group">
  						<label for="text" class="row-fluid">文件2</label>
						<input type="file" id="inputfile2" name="inputfile2">
  					</div>
  					<div class="form-group">
  						<label for="text" class="row-fluid">参考结果文件</label>
						<input type="file" id="inputfile3" name="inputfile3">
  					</div>
  						 <input type="submit" id="submitBtn" class="btn btn-default" value="提交" >
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>