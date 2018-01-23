function getRandomColor() {
	var a = Math.round(Math.random()*0x1000000);
	var c = "000000".concat(a.toString(16));
	return '#' + c.substr(c.length-6,6);
}

function stopBubble(e) {
    if (e && e.stopPropagation)
        e.stopPropagation();
    else
        window.event.cancelBubble = true;
}

var tree;
function getTreeHTML(action) {
	var html = "<div style='position: absolute; width: 200px; top: 0px; right: 0px; " +
				"height: 100%; padding: 5px; overflow: auto;'>" +
				"<a href='javascript: tree.expandChildren()'>expand all</a>|" + 
				"<a href='javascript: tree.collapseChildren()'>collapse all</a>";
	
	tree = new WebFXTree('Root','','classic');
			
	var floder1 = new WebFXTreeItem('航空母舰',action,tree,'images/foldericon.png','images/openfoldericon.png');
	var floder2 = new WebFXTreeItem('雷达',action,tree,'images/foldericon.png','images/openfoldericon.png');
	var floder3 = new WebFXTreeItem('导弹',action,tree,'images/foldericon.png','images/openfoldericon.png');
	var floder4 = new WebFXTreeItem('舰载机',action,tree,'images/foldericon.png','images/openfoldericon.png');
	var floder5 = new WebFXTreeItem('火炮',action,tree,'images/foldericon.png','images/openfoldericon.png');
	var floder0 = new WebFXTreeItem('Undefined',action,tree,'images/foldericon.png','images/openfoldericon.png');

//	new WebFXTreeItem('库兹涅佐夫号','javascript: change()',floder1);
//	new WebFXTreeItem('喀什塔','javascript: change()',floder3);
//	new WebFXTreeItem('SU-37','javascript: change()',floder4);
	
	new WebFXTreeItem('库兹涅佐夫号','javascript: void(0)',floder1);
	new WebFXTreeItem('喀什塔','javascript: void(0)',floder3);
	new WebFXTreeItem('SU-37','javascript: void(0)',floder4);
	
//	floder1.add(new WebFXTreeItem('库兹涅佐夫号'));
//	floder2.add(new WebFXTreeItem('鸡笼级'));
//	floder3.add(new WebFXTreeItem('喀什塔'));
//	floder4.add(new WebFXTreeItem('SU-37'));
	
	html += tree + "</div>";
	return html;
}

