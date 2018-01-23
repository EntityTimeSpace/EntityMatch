<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>时间关联</title>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen">
	<link rel="stylesheet" type="text/css" href="css/main.css" media="screen">
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
</head>
<body>
<header class="navbar navbar-static-top mili-nav" id="top">
	<div class="container">
		<nav id="bs-navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li>
					<a href="../EntityMatch/">实体关联系统</a>
				</li>
				<li class="active">
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
					时间关联系统
				</h2>
				<p>
					军事领域相关的时间关联系统。
				</p>
			</div>
		</div>
		<div class="span6">
			<div class="hero-unit well">
				<form id="temporal-reasoner-form" class="form" enctype="multipart/form-data" action="TemporalReasonerServlet" method="post">
					<div class="form-group">
						<label for="eventFile">事件读取</label>
						<input type="file" id="eventFile" name="eventFile">
					</div>
					<div class="form-group">
						<label for="eventText">用户输入</label>
						<textarea id="eventText" class="form-control" rows="3" name="eventText"></textarea>
					</div>
					<button type="submit" class="btn btn-default">执行事件关联</button>
				</form>
			</div>
			
			<div id="result" class="well">
				<div class="form-group">
					<label>结果输出</label>
					<textarea class="form-control" rows="12" readonly="readonly"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
"use strict";
//jQuery(document).ready(function () {
	console.log('hre')
	var $form = jQuery('#temporal-reasoner-form');
	var $result = $('#result'), $resultArea = $("textarea", $result);

	jQuery('input', $form).change(function (e) {
		$result.hide();
	});
	
	jQuery('textarea', $form).change(function (e) {
		$result.hide();
	})
	
	$result.hide();
	$form.submit(function (e) {
		e.preventDefault();
		var formData = new FormData($form[0]);

		jQuery.ajax({
			url: 'TemporalReasonerServlet',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			statusCode: {
				200: function (data) {
					$resultArea.val(data);
					$result.show();
				},
				400: function (data) {
					console.log(data);
				}
			}
		});
	});
//})
</script>
</body>
</html>