$(document).ready(initPage);
var g_currentStepIndex = 0;
var g_url = ['Segment','AnnotateEntity','AnnotateTriple','Finish','AddDict'];
var g_loader;
var g_classes;

function initPage() {	
	$.ajaxSetup ({
		cache: false
	});
	initTree();
	showLoader();
	changeStep(0);
}

function showLoader() {
	var options = {
		bgColor 		: '#fff',
		duration		: 50,
		opacity		    : 0.7,
		classOveride 	: false
	}	
	g_loader = new ajaxLoader($("#content"), options);	
}

function hideLoader() {
	if(g_loader) {
		g_loader.remove();
	}
}

function initTree() {
	$.get('GetClassTree', function(resp) {
		console.log("classTree----"+resp.toString());
		parseClassTree(resp);		
		$("#red").treeview({
			animated: "fast",
			collapsed: true,
			unique: true,		
			toggle: function() {
				//window.console && console.log("%o was toggled", this);
			}
		});
	});
	
}

function parseClassTree(data) {
	$("#red").html(constructTree(data['classTree']));
	g_classes = traversalTree(data['classTree']);
	var undefinedObj = {};
	undefinedObj['Undefined'] = {
		label: '未定义',
		colors: 'white',
		checked: true,
		counts: 0
	};
	g_classes = $.extend(g_classes, undefinedObj);
	console.log("g_classes==============="+g_classes);
}

function traversalTree(nodes) {
	var classes = {};
	$.each(nodes, function(index, val) {
		var curClasses = traversalNode(val);
		classes = $.extend(curClasses, classes);
	});
	return classes;
}

function traversalNode(node) {	
	var classObj = {
		label: node['label'],
		colors: getRandomColor(),
		checked: true,
		counts: 0
	};
	var classes = {};
	classes[node['uri']] = classObj;
	if(node['subTree']) {
		var subClasses = traversalTree(node['subTree']);
		classes = $.extend(subClasses, classes);
	}
	return classes;
}

function constructTree(nodes) {
	var ul = $('<ul>');
	$.each(nodes, function(index, val) {
		var li = constructTreeNode(val);
		ul.append(li);
	});
	return ul;
}

function constructTreeNode(node) {
	var li = $('<li>');
	var span = $('<span>', {
					text: node['label'],
					class: 'classTreeNode'
				});
	li.append(span);
	span.data('uri', node['uri']);
	span.click(function() {
		var type = $(this).data('uri');
		changePointerType(type);
		console.log(type);
	});
	if(node['subTree']) {
		var ul = constructTree(node['subTree']);
		li.append(ul);
	}	
	return li;
}

function initTabs() {
	$('.ideal-tabs-tab').unbind();
	$('.ideal-tabs-tab:not(.ideal-tabs-tab-disabled):not(.ideal-tabs-tab-active)').bind('click', function() {
		var index = this.id.substr(3);
		changeStep(index);		
	});
}

function btnListener() {
	var btnId = this.id;
	switch(btnId) {
		case 'nextBtn':
			changeStep(g_currentStepIndex + 1);
		break;
		case 'finish':
			location.href='/EntityMatch/index.jsp'
			break;
		case 'prevBtn':
			changeStep(g_currentStepIndex - 1);
		break;
		case 'resetBtn':
			reset();
		break;
		case 'addNewWordsBtn':
			$('#myModal').modal('hide');
			$('#modalBtn').hide();
			submitRequest(4);
		break;
	}
}

function reset() {
	var stepFilePath = 'step0.jsp';
	$('#content').load(stepFilePath, function callback() {
		$('.btn').bind('click', btnListener);
	});
	g_currentStepIndex = 0;
	setActiveTab(g_currentStepIndex);
}

function changeStep(index) {
	if(index == 0) {
		//load dict
		$.ajax({
			url: 'SegmentService',
			type:'get',
			data: {raw: ''},
			statusCode: {
				200: function() {				
					changeContent(0);
					setActiveTab(0);
					g_currentStepIndex = 0;
				}
			}
		});	
	}
	else if(g_currentStepIndex < index) {
		//forward
		submitRequest(g_currentStepIndex);
	}
	else {
		//backward
		g_currentStepIndex--;
		changeContent(g_currentStepIndex);
		setActiveTab(g_currentStepIndex);
	}
}

function changeContent(index) {
	var stepFilePath = 'step' + index + '.jsp?back=true';
	$('#content').load(stepFilePath, function callback() {
		$('.btn').bind('click', btnListener);		
	});
}

function removeRow() {
	var index = this.id.replace('removeRowBtn-','');
	$(this).parent().parent().remove();
	newWords.splice(index,1);
	if(newWords.length == 0) {
		$('#myModal').modal('hide');
		$('#modalBtn').hide();
	}
}

function setActiveTab(index) {
	$('.ideal-tabs-tab').removeClass('ideal-tabs-tab-active');
	$('.ideal-tabs-tab').removeClass('ideal-tabs-tab-disabled');
	if(index == 4) {
		$('.ideal-tabs-tab').unbind();
		$('.ideal-tabs-tab').eq(0).click(reset);
		$('.ideal-tabs-tab').eq(1).addClass('ideal-tabs-tab-disabled');
		$('.ideal-tabs-tab').eq(2).addClass('ideal-tabs-tab-disabled');
		$('.ideal-tabs-tab').eq(3).addClass('ideal-tabs-tab-disabled');
		$('.ideal-tabs-tab').eq(4).addClass('ideal-tabs-tab-active');		
	}
	else {
		var nextIndex = parseInt(index) + 1;		
		$('.ideal-tabs-tab').eq(index).addClass('ideal-tabs-tab-active');
		$('.ideal-tabs-tab:gt(' + nextIndex + ')').addClass('ideal-tabs-tab-disabled');
		initTabs();
	}
}

function submitRequest(index) {
	var url = g_url[index];	
	var params = constructParams(index);
	if(params) {
		showLoader();
		$.post(url, params, function callback(resp) {
			if(index < 4) {
				$('#content').html(resp);
				$('.btn').bind('click', btnListener);
				$('body').tooltip({selector:'[rel=tooltip]'});
				$('.removeRowBtn').click(removeRow);
				g_currentStepIndex++;
				hideLoader();
				setActiveTab(g_currentStepIndex);
			}
			else {
				hideLoader();
				$('#addWordsInfo').html(resp);				
				$('#addWordsAlert').slideDown();
			}
		});
	}
}

function constructParams(index) {
	var params;
	switch(index) {
		case 0:
			params = constructParams0();
		break;
		case 1:
			params = constructParams1();
		break;
		case 2:
			params = constructParams2();
		break;
		case 3:
			params = constructParams3();
		break;
		case 4:
			params = constructParams4();
		break;
	}
	return params;
}

function constructParams0() {
	var rawIntel = $('#rawIntel').val();
	if(rawIntel.trim() == '') {
		return null;
	}
	var params = {intelligence:rawIntel};
	return params;
}

function constructParams1() {
	var segIntel = $('#segIntel').val();
	var params = {intelligence:segIntel};
	return params;
}

function constructParams2() {
	var params = {};
	for (var i = 0; i < segIntel.length; i ++) {
		params['trunk-' + i] = segIntel[i].type;//词性uri
	}
	return params;
}

function constructParams3() {
	var params = {};
	if ( !checkValidate() ) {
		alert('请把三元组补充完整');
		return null;
	}
	var count = 0;	
	for (var i = 0; i < triples.length; i ++) {
		if ( triples[i] && triples[i].subject ) {
			params['triple-' + count + '-subject-type'] = triples[i].subject.type;
			params['triple-' + count + '-subject-entryIndex'] = triples[i].subject.entryIndex;
			params['triple-' + count + '-subject-alias'] = triples[i].subject.alias;
			params['triple-' + count + '-predicate-type'] = triples[i].predicate.type;
			params['triple-' + count + '-predicate-entryIndex'] = triples[i].predicate.entryIndex;
			params['triple-' + count + '-predicate-alias'] = triples[i].predicate.alias;
			params['triple-' + count + '-object-type'] = triples[i].object.type;
			params['triple-' + count + '-object-entryIndex'] = triples[i].object.entryIndex;
			params['triple-' + count + '-object-alias'] = triples[i].object.alias;			
			count++;
		}	
	}
	params['numOfTriples'] = count;
	return params;
}

function constructParams4() {
	var params = {};
	var count = 0;
	
	for (var i = 0; i < newWords.length; i ++) {
		if ( newWords[i] ) {
			params['new-word-' + count + '-name'] = newWords[i].name;
			params['new-word-' + count + '-type'] = newWords[i].type;
			count ++;
		}
	}
	params['numOfNewWords'] = count;
	return params;
}



