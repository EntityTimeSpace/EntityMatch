<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>实体查询</title>

	<!-- page css -->
	    <link href="css/entity/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" href="css/bootstrap-datetimepicker.css">
	<link rel="stylesheet" href="css/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="css/page/normalize.css" />
	<link rel="stylesheet" type="text/css" href="css/page/paginate.css">
	<link rel="stylesheet" type="text/css" href="css/page/style.css">
	<link rel="stylesheet" type="text/css" href="css/entity/bootstrap.css" media="screen">
	<link rel="stylesheet" type="text/css" href="css/main.css" media="screen">
	<link rel="stylesheet" type="text/css" href="css/default.css" />
	<link rel="stylesheet" type="text/css" href="css/component.css" />
	<link rel="stylesheet" type="text/css" href="css/dtree.css" />
	

</head>
<body>
<!-- <header class="navbar navbar-static-top mili-nav" id="top">
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
</header> -->
<div class="container" style='margin-top:15px'>
	<div class="row-fluid">
		<div class="span6">
			<div class="hero-unit well">
				<h2>
					关联搜索系统
				</h2>
				<p>
					军事领域相关的关联搜索系统。
				</p>
			</div>
		</div>
	<div class="span6">
		<ul class="nav nav-tabs">
		  <li id="nameQuery" >
		    <a >基于时空关联的搜索</a>
		  </li>
		  <li id="paramQuery"><a >基于参数约束的实体搜索</a></li>
		  <button type="submit" id="Anno" style="float:right" class="btn btn-default">实体标注</button>
		  <!-- <li id="rccQuery"><a >基于空间关联的搜索</a></li> -->
		</ul>
		<div id="nameCon" >
			<h3 style="margin:20px auto 10px auto">选择搜索类型</h3>
			<div id="timeOrSpace" style="float:left">
				 <ul class="nav nav-tab nav-stacked" data-spy="affix" data-offset-top="125" style="float:left">
	                <li id="Atime" ><a >基于时间</a></li>
	                <li id="Aspace"><a >基于空间</a></li>
	                <li id="Atimespace"><a >基于时空</a></li>
	            </ul>
<!-- 				<a style="float:left"  id="Atime" class="active">基于时间关联的搜索</a>
				<a style="float:left"  id="Aspace">基于空间关联的搜索</a> -->
			</div>
			<div id="tSArea">

					<div id="timeResult" style="display:none;">
						<div  class="searchPlane">
							<span>开始时间：</span>
							<input  type="text" id="startDate" name="startDate"   class="date"/>
						
							<span style="margin-left:10px">结束时间：</span>
							
							<!-- <input style="margin-left:10px;margin-top:10px" type="text" id="endDate" name="endDate" onclick="return showCalendar('endDate', 'y-mm-dd');"  /> -->
							<input style="margin-left:10px;margin-top:10px" type="text" id="endDate" name="endDate"  class="date"/>
							
							<span style="margin-left:10px">时间关系:</span>
							<select class="sel-search" id="dateSelect" >
					 	    </select>
					 	    <input class="search-input" style="margin-left:10px;margin-top:10px" type="text" id="keyword" name="keyword" placeholder="关键词" />
					 	    <button type="button" class="btn btn-date"  data-toggle="button" id="searchDateBtn">搜索</button>
						</div>	
						<img id="loading" src='./images/loading.gif' style='display:none'/>
			<!-- 		<ul id="entityResult" class="main" style="background-color: #FFF;display:none">
						</ul> -->
						<div class="resultAndpage">
							<div style="background-color: #FFF;display:none;" id="entityResult">
							</div>
							<div id="paginatekey" style="background-color:#FFF;">
							</div>
						</div>
					</div>
					<div id="spaceResult" style="display:none;">
					   	
						<div  class="searchPlane">
							<input class="search-input" type="text" id="firstEntity"  placeholder="输入地点"  />
							<select class="sel-search" id="rccSelect" style="padding:5px;">
							</select>
							<input class="search-input" style="margin-left:10px;margin-top:10px" type="text" id="secondEntity"  placeholder="距离值"  />
							<button type="button" class="btn btn-date"  data-toggle="button" id="searchRccBtn">搜索</button>
						</div>
						<img id="rccloading" src='./images/loading.gif' style='display:none'/>
				<!-- 			<ul id="entityResult" class="main" style="background-color: #FFF;display:none">
							</ul> -->
						<div class="resultAndpage">
							<div style="background-color: #FFF;display:none" id="rccResult">
							
							</div>
							<div id="paginatercc" style="background-color:#FFF;">
							</div>
						</div>				
					</div>
					<div id="timespaceResult" style="display:none;">
						<div  class="searchPlane">
							<div >
								<!-- <span>开始时间：</span> -->
								<input  type="text" id="startTime" name="startDate"   class="date" placeholder="开始时间"/>

								<!-- <span style="margin-left:10px">结束时间：</span> -->

								<!-- <input style="margin-left:10px;margin-top:10px" type="text" id="endDate" name="endDate" onclick="return showCalendar('endDate', 'y-mm-dd');"  /> -->
								<input style="margin-left:10px;margin-top:10px" type="text" id="endTime" name="endDate"  class="date" placeholder="结束时间"/>

								<span style="margin-left:10px">时间关系:</span>
								<select class="sel-search" id="timespace-dateSelect">
								</select>
								<!-- <input style="margin-left:10px;margin-top:10px" type="text" id="ST-keyword" name="keyword" placeholder="关键词"  class="search-input" /> -->
								<!-- <button type="button" class="btn btn-date"  data-toggle="button" id="timespace-search">搜索</button> -->
								<%--<button type="button" class="btn btn-date"  data-toggle="button" id="ST-search">搜索</button>--%>
							</div>
							<div style="float:left">
								<input  type="text" id="loc"  placeholder="输入地点"  class="search-input"/>
								<select class="sel-search" id="space-rel" style="padding:5px;" >
								</select>
								<input style="margin-left:10px;margin-top:10px" type="text" id="value"  placeholder="距离值( 海里 )"  class="search-input"/>
								<%--<button type="button" class="btn btn-date"  data-toggle="button" id="ST-search">搜索</button>--%>
							</div>
							<div align="center" style="float:left">
								<input style="margin-left:10px;margin-top:10px;float:left" type="text" id="ST-keyword" name="keyword" placeholder="关键词"  class="search-input" />
								<button   class="btn btn-date"  data-toggle="button" id="timespace-search">搜索</button>
							</div>
						</div>
						<div class="resultAndpage">
							<div style="background-color: #FFF;display:none" id="timespacelist">
							
							</div>
							<div id="paginatetimespace" style="background-color:#FFF;">
							</div>
						</div>						
					</div>			
			</div>

			
		</div>
		<div id="paramCon" style="display:none" class="">
			<h2>高级检索</h2>
			<div id= "tree"></div>
			<div id="serachArea">
				<div class="entitySearch">
					<!-- Optional columns for small components -->
					<div class="column">
						<div id="sb-search" class="sb-search sb-search-open">
							<form>
								<input class="sb-search-input" placeholder="输入要搜索的实体名称" type="text" value="" name="search" id="search">
		<!-- 						<input class="sb-search-submit" type="button" value="" id="searchSubmit">
								<span class="sb-icon-search"></span> -->
								
							</form>
						</div>
					</div>				
				</div>
				
				<div id = "props" >
					<div id="conditionInput">
						<div class="paramInputCon">
		<!-- 					<img id="add"  src='./images/plus.gif'/>
							<img id="delete" src='./images/minus.gif'/> -->
	<!-- 						<select class="proper">
								<option id="country">国籍</option>
								<option>乘员</option>
								<option id="firstTime">首飞时间</option>
								<option>研发单位</option>
								<option>气动布局</option>
								<option>飞行速度</option>
								<option>发动机数量</option>
							</select> -->
							<!--  placeholder="属性值" -->
							<textarea id="propsInput" rows="1" placeholder=" 添加属性" style="overflow-y:hidden;" onpropertychange="this.style.height=this.scrollHeight + 'px'" oninput="this.style.height=this.scrollHeight + 'px'"></textarea>
							
						</div>
					</div>
					
					
					<div id="prophit">
					 	<select class="proper" id="propSelect">
					 	</select>
						<ul style="float:left;margin-top:2px">
							<li><a class="label" id="addProp">添加属性</a></li>
							<li><a class="label" id="round">添加括号</a></li>
							<li><a class="label" id="and">AND</a></li>
							<li><a class="label" id="or">OR</a></li>
	
						</ul>
					</div>
				</div>
				
				
				<button type="button" class="btn btn-primary"  data-toggle="button" id="searchBtn">搜索</button>
			</div>
			
			<img id="loading1" src='./images/loading.gif' style='display:none'/>
			<div id="showlist">
				<div style="background-color: #FFF;display:none;" id="entityResult1">	
				</div>
				<div id="paginate" style="background-color:#FFF;">
				</div>	
			</div>
	   </div>
<!-- 	   	<div id="rccCon" >
						<div style="margin-left:10px;margin-top:20px">
							<input  type="text" id="firstEntity"  placeholder="实体一名称"  />
							<select class="rccRel" id="rccSelect">
					 	    </select>
							<input style="margin-left:10px;margin-top:10px" type="text" id="secondEntity"  placeholder="另一实体名称"  />
					 	    <button type="button" class="btn btn-date"  data-toggle="button" id="searchRccBtn">搜索</button>
						</div>	
			<img id="rccloading" src='./images/loading.gif' style='display:none'/>
			<ul id="entityResult" class="main" style="background-color: #FFF;display:none">
			</ul>
			<div style="background-color: #FFF;display:none;margin-left:10px" id="rccResult">
			
			</div>
			<div id="paginatercc" style="background-color:#FFF;">
			</div>
		</div> -->



	</div>
	</div>
</div>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/date/bootstrap-datetimepicker.js" charset="UTF-8"></script>
	<script type="text/javascript" src="js/date/bootstrap-datetimepicker.fr.js" charset="UTF-8"></script>
	<script type="text/javascript" src="js/date//bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript" src="js/modernizr.custom.js"></script>
	<script type="text/javascript" src="./js/lodash.js"></script>
	<script type="text/javascript" src="./js/dtree.js"></script>
	<script type = "text/javascript" src="js/resources/search.js"></script>
    <script type="text/javascript" src="js/textext.js"></script>
</body>
</html>