var g_currentPageNum = 0;

$(document).ready(init);

function init() {
	requestByParam();
	
}

function requestByParam() {
	var pageNum = getParam('page');
	if(pageNum == null) {
		g_currentPageNum = 1;
	}
	else {
		g_currentPageNum = pageNum;
	}
	listIntel(g_currentPageNum);
	
}

function listIntel(page) {
	$.getJSON('ListIntel', {page: page}, parseResult);	
}

function parseResult(data) {
	var intels = data['intels'];
	var tbody = $('#tableBody');
	$.each(intels, function(index, val) {
		var content = val['content'];
		var time = val['time'];
		var source = val['source'];
		var iid = val['iid'];
		tbody.append(constructRowElement(val));
	});
	var sumPageNum = data['sumPageNum'];
	if(g_currentPageNum < 1 || g_currentPageNum > sumPageNum) {
		g_currentPageNum = 1;
	}
	constructPagination(g_currentPageNum, sumPageNum);
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

function constructRowElement(data) {
	var rowElement = $('<tr>');	
	rowElement.append($('<td>').html(data['time']));
	var aElement = $('<a>', {
		href: 'view.html?iid=' + data['iid'],
		text: data['content']
	});
	rowElement.append($('<td>').html(aElement));
	rowElement.append($('<td>').html(data['source']));
	return rowElement;
}

function constructPagination(curPageNum, sumPageNum) {
	$('#paginate').paginate({
				count 		: sumPageNum,
				start 		: curPageNum,
				display     : 20,
				border					: true,
				border_color			: '#DDDDDD',
				border_hover_color		: '#DDDDDD',
				text_color  			: '#0088CC',
				text_hover_color		: '#999',
				background_color    	: 'none',	
				background_hover_color	: 'none', 
				images		: false,
				rotate		: false,
				mouse		: 'press',
                onChange    : function(page) {
                				location.href='list.html?page=' + page;
                            }
    });
	
}
