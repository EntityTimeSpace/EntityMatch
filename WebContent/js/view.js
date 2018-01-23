$(document).ready(init);

function init() {
	requestByParam();
}

function requestByParam() {
	var iid = getParam('iid');
	if(iid == null) {
		iid = 1;
	}
	viewIntel(iid);
}

function viewIntel(iid) {
	$.getJSON('ViewIntel', {iid: iid}, parseResult);	
}

function parseResult(data) {
	var intels = data['intels'];
	console.log(data);
	$.each(intels, function(index, val) {
		constructIntelPane(val, index);
		var content = val['content'];
		var time = val['time'];
		var source = val['source'];
		var iid = val['iid'];
	});
	if(intels.length === 1) {
		$('#collapse0').collapse('show');
	}
}

function getParam(paramName) {
	var hrefStr = location.href;
	if(hrefStr.lastIndexOf('?') === -1 || hrefStr.lastIndexOf('?') === (hrefStr.length - 1)) {
		return null;
	}
	var params = hrefStr.split('?')[1];
	var paramsArray = params.split('&');
	for(var i = 0; i < paramsArray.length; i++) {
		var curParam = paramsArray[i];
		if(curParam.split('=')[0] == paramName) {
			return curParam.substring(curParam.indexOf('=') + 1);
		}
	}
	return null;
}

function constructIntelPane(intel, index) {
	var accordion = $('#accordion');
	var accordionGroupDiv = $('<div class="accordion-group">');
	accordionGroupDiv.append(constructAccordionHeading(intel['content'], index));
	accordionGroupDiv.append(constructAccordionBody(intel['iid'], intel['triples'], index));
	accordion.append(accordionGroupDiv);
}

function constructAccordionHeading(content, index) {
	var accordionHeadingDiv = $('<div class="accordion-heading">');
	var aElement = $('<a>', {
		class: 'accordion-toggle', 
		'data-toggle': 'collapse',	
		'data-parent': '#accordion',
		href: '#collapse' + index
	});
	aElement.html(content);
	accordionHeadingDiv.append(aElement);
	return accordionHeadingDiv;
}

function constructAccordionBody(iid, triples, index) {
	var accordionBodyDiv = $('<div>', {
		class: 'accordion-body collapse',
		id: 'collapse' + index
	});	
	var accordionInnerDiv = $('<div class="accordion-inner">');
	var btn = $('<a>', {
		class: 'btn btn-danger',
		text: '重新标注',
		href: 'Reannotate?iid=' + iid
	});
	accordionInnerDiv.append(btn);
	accordionInnerDiv.append(constructAccordionInnerTable(triples));
	accordionBodyDiv.append(accordionInnerDiv);
	return accordionBodyDiv;
}

function constructAccordionInnerTable(triples) {
	if(!triples) {
		return null;
	}
	var tableElement = $('<table>', {
		class: 'table table-bordered table-condensed',
		style: 'margin-top: 10px;'
	});
	var thead = $('<thead><tr><th>主语</th><th>谓语</th><th>宾语</th></tr></thead>');
	var tbody = $('<tbody>');
	$.each(triples, function(index, val) {
		var tr = $('<tr>');	
		tr.append($('<td>').html(val['subject']));
		tr.append($('<td>').html(val['predicate']));
		tr.append($('<td>').html(val['object']));
		tbody.append(tr);
	});
	tableElement.append(thead);
	tableElement.append(tbody);
	return tableElement;
}

