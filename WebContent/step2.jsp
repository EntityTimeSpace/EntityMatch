<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page errorPage="index.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>情报解析——实体标注</title>
</head>
<style type='text/css'>
a {
	text-decoration: none;
}
</style>
<%
	@SuppressWarnings("unchecked")
	List<Entry<String, String>> trunks = (List<Entry<String, String>>) session.getAttribute("trunks");
	System.out.println("trunks=========="+trunks);
%>
<body>
	<p>以下是实体标注结果，如果您不满意，可以手动修正，修正完毕请点“完成”</p>
	<p id='intelligence'></p>
	
<script type="text/javascript">


//标注输入词中出现的标签
<%
	for (Entry<String, String> entry : trunks) {
		out.println("g_classes['" + entry.getValue() + "']['counts'] ++;");
	}
%>

console.log("g_classes=========="+g_classes.toString());

/* 按不同颜色显示分好词的文本 */
function createSegTrunk(string,type) {
	var segTrunk = new Object;
	segTrunk.string = string;
	segTrunk.type = type;
	segTrunk.toString = function() {
		var s = this.string + '/' + this.type;//type对应uri,string对应分词的文本
		return s;
	};
	segTrunk.toHTML = function() {
		var html = "<strong class='segIntel' style='background:" + 
						((g_classes[this.type]['checked']) ? g_classes[this.type]['colors'] : 'white') + "'>" + 
						this.string + "</strong>";
		return html;
	};
	return segTrunk;
}

var pointer;

//点击后显示红框选中
function setPointer(element) {
	if ( pointer ) {
		pointer.html.innerHTML = pointer.html.firstChild.innerHTML;
	} else {
		pointer = new Object;
	}
	pointer.html = element;
	pointer.id = element.id.replace('segIntel-','');//使用空字符替换segIntel-n中的segIntel得到n,所以pointer.id值就是n
	pointer.html.innerHTML = "<span style='outline: red solid 2px'>" + pointer.html.innerHTML + "</span>";
}

var segIntel = [];

//使用out.println()输出内容到html页面，只不过这在<script>之间变成了js代码

<%
	for (int i = 0; i < trunks.size(); i ++) {
		out.println("segIntel[" + i + "] = createSegTrunk('" + 
				trunks.get(i).getKey() + "','" + trunks.get(i).getValue() + "')");
	}
%>

/* a标签显示分好词的文本 */
function segIntelId2HTML(id) {
	var html = "<a id='segIntel-" + id + "' href='javascript: void(0)' onclick='setPointer(this)'>" +
				segIntel[id].toHTML() + "</a> ";
	return html;
}

function segIntel2HTML() {
	var html = '<p>';
	console.log("segIntel=========="+segIntel);
	for (var id in segIntel) {
		html = html + segIntelId2HTML(id);
	}
	return html + '</p>';
}

reDrawIntelligence();

function changePointerType(type) { 	
	if ( pointer ) {
		g_classes[segIntel[pointer.id].type]['counts'] --;
		segIntel[pointer.id].type = type;
		g_classes[segIntel[pointer.id].type]['counts'] ++;
		g_classes[segIntel[pointer.id].type]['checked'] = true;
		pointer.html.firstChild.innerHTML = segIntel[pointer.id].toHTML();
		reDrawIntelligence();
	}
}

//点击复选按钮不选中
function checkboxConvert(element) {
	var type = element.id.replace('typeChecked-','');
	g_classes[type]['checked'] = !g_classes[type]['checked'];
	reDrawIntelligence();
}

//对词性显示标注结果
function typeId2HTML(id) {
	var html = "<div><input id='typeChecked-" + id + "' type='checkbox' " +
				((g_classes[id]['checked']) ? "checked=''" : "") + 
				" name='" + id + "' onclick='checkboxConvert(this)'>" +
				"<span class='colorBlock' style='background:" + g_classes[id]['colors'] + "'>&nbsp&nbsp</span>" +
				g_classes[id]['label'] + "</div>";
	return html;
}

function types2HTML() {
	var html = '<p>';
	for (var id in g_classes) {
		if ( id != 'Undefined' && g_classes[id]['counts'] > 0 ) {
			html += typeId2HTML(id);
		}
	}
	return html + '</p>';
}

//生成intelligence里面的内容
function reDrawIntelligence() {
	document.getElementById('intelligence').innerHTML = 
		segIntel2HTML() + types2HTML();
	
	if ( pointer ) {
		pointer.html = document.getElementById('segIntel-' + pointer.id);
		pointer.html.innerHTML = "<span style='outline: red solid 2px'>" + pointer.html.innerHTML + "</span>";
	}
}


//没调用
function submit() {
	var postForm = document.createElement('form');
	postForm.method = 'post';
	postForm.action = 'AnnotateTriple';
	
	for (var i = 0; i < segIntel.length; i ++) {
		var input = document.createElement('input');
		input.setAttribute('name', 'trunk-' + i);
		input.setAttribute('value', segIntel[i].type);
		postForm.appendChild(input);
	}
	postForm.submit();
}

</script>	
	<button class="btn btn-primary" id="prevBtn">上一步</button>
	<button class="btn btn-primary" id="finish">完成</button>
</body>
</html>