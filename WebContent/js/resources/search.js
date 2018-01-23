$(function(){
   // var shortToReal = {'DC':'相离','EC':'外切','PO':'部分重合','TPP':'内切','TPPi':'内切的逆关系','EQ':'相等','NTPP':'真包含','NTPPi':'真包含的逆关系'};
    var tabIndex = 0;
    var timeOrspace = 0;
    var currentPage=1;
    var currentPages = [1,1,1,1];
    var sumResultNum = 0;
    var sumResultNums = [0,0,0,0];
    var sumPageNum = 0;
    var jsonDatas = [];
    var showMinPageIndex = 1;
    var showMaxPageIndex = 12;
    var showMinPageIndexs = [1,1,1,1];
    var showMaxPageIndexs = [12,12,12,12];
    var jsonData;
    var img = ['img/timg.jpg',
    	'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=759257335,3022941357&fm=27&gp=0.jpg',
    	   		'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2832236805,1665833787&fm=200&gp=0.jpg',
    		'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd1jtvs5cMCFcL7vS-MLXTOwxxtndT-GHVxvHPfKEJnncShrYD',
    		'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2597211181,3496160343&fm=27&gp=0.jpg'] 
    d = new dTree('d');
    d.add(0,-1,'<span>分类信息</span>');
    d.add(1,0,'authority','飞机','飞机');
    d.add(2,1,'leaf','侦察机','侦察机');
    d.add(3,1,'leaf','战斗机','战斗机');
    d.add(4,1,'leaf','攻击机','攻击机');
    d.add(5,1,'leaf','轰炸机','轰炸机');
    d.add(6,1,'leaf','预警机','预警机');
    d.add(15,1,'leaf','多用途直升机','多用途直升机');
    d.add(7,0,'authority','舰船舰艇','舰船舰艇');
    d.add(8,7,'leaf','巡洋舰','巡洋舰');
    d.add(9,7,'leaf','护卫舰','护卫舰');
    d.add(10,7,'leaf','核潜艇','核潜艇');
    d.add(11,7,'leaf','驱逐舰','驱逐舰');
    d.add(12,0,'authority','导弹','导弹');
    d.add(13,12,'leaf','地空导弹','地空导弹');
    d.add(14,0,'authority','鱼雷','鱼雷');
// dTree实例属性以此为：  节点ID，父类ID，chechbox的名称，chechbox的值，chechbox的显示名称，chechbox是否被选中--默认是不选，chechbox是否可用：默认是可用，节点链接：默认是虚链接

    document.getElementById("tree").innerHTML=d;
    d.openAll();

    function init(){
        $.getJSON("js/rcc.json",function(data){
            var select = $('#rccSelect');
            _.each(data,function(d){
                var option = $('<option>',{
                    class:'rcc_item'
                    /*title:d['rel']*/
                });
                option.html(d['desc']);
                select.append(option);
                //alert(d['property']);
            });

        });
        $("#Anno").bind("click",function(){
        	location.href="/EntityMatch/tagger.html"
        });
        $.getJSON("js/dateRelation.json",function(data){
            var select = $('#dateSelect');
            _.each(data,function(d){
                var option = $('<option>',{
                    class:'rel_item',
                    title:d['desc']
                });
                option.html(d['rel']);
                select.append(option);
                //alert(d['property']);
            });

        });
        $.getJSON("js/dateRelation.json",function(data){
            var select = $('#timespace-dateSelect');
            _.each(data,function(d){
                var option = $('<option>',{
                    class:'rel_item',
                    title:d['desc']
                });
                option.html(d['rel']);
                select.append(option);
                //alert(d['property']);
            });

        });
        $.getJSON("js/rcc.json",function(data){
            var select = $('#space-rel');
            _.each(data,function(d){
                var option = $('<option>',{
                    class:'rcc_item'
                    /*title:d['rel']*/
                });
                option.html(d['desc']);
                select.append(option);
                //alert(d['property']);
            });

        });

        checkCookie('tabIndex=');
        checkCookie('timeOrspace=');
        if(tabIndex === 0){
            $('#nameQuery').addClass("active");
            $('#nameQuery').next().removeClass("active");
            $("#paramCon").hide();
            // $("#rccCon").hide();
            $("#nameCon").show();
            var Atime = $('#Atime');
            var Aspace = $('#Aspace');
            var Atimespace = $('#Atimespace');
            if(timeOrspace ===0){
                Atime.addClass('active');
                Aspace.removeClass();
                Atimespace.removeClass();
                $('#spaceResult').hide();
                $('#timespaceResult').hide();
                $('#timeResult').fadeIn();

            }else if(timeOrspace ===1){
                Aspace.addClass('active');
                Atimespace.removeClass();
                Atime.removeClass();
                $('#spaceResult').fadeIn();
                $('#timespaceResult').hide();
                $('#timeResult').hide();
            }else{
                Atimespace.addClass('active');
                Aspace.removeClass();
                Atime.removeClass();
                $('#spaceResult').hide();
                $('#timespaceResult').fadeIn();
                $('#timeResult').hide();
            }

        }else if(tabIndex === 1){
            $('#paramQuery').addClass("active");
            $('#paramQuery').prev().removeClass("active");
            $("#nameCon").hide();
            //$("#rccCon").hide();
            $("#paramCon").show();
        }

        initLabel();
    }
    $('#Atime').bind('click',function(){
        document.cookie = 'timeOrspace = 0';
        tabIndex = 0;
        showMinPageIndex = showMinPageIndexs[0];
        showMaxPageIndex = showMaxPageIndexs[0];
        if(jsonDatas[0]!==undefined){
            jsonData = jsonDatas[0];

            currentPage = currentPages[0];
            sumResultNum = sumResultNums[0];
            constructPagination();
            bindLi();
        }
        $(this).addClass('active');
        $(this).next().removeClass();
        $(this).next().next().removeClass();
        $('#spaceResult').hide();
        $('#timespaceResult').hide();
        $('#timeResult').fadeIn();
    });
    $('#Aspace').bind('click',function(){
        document.cookie = 'timeOrspace = 1';
        tabIndex = 0;
        showMinPageIndex = showMinPageIndexs[2];
        showMaxPageIndex = showMaxPageIndexs[2];
        if(jsonDatas[2]!==undefined){
            jsonData = jsonDatas[2];
            currentPage = currentPages[2];
            sumResultNum = sumResultNums[2];
            constructPagination();
            bindLi();
        }
        $(this).addClass('active');
        $(this).prev().removeClass();
        $(this).next().removeClass();
        $('#timeResult').hide();
        $('#timespaceResult').hide();
        $('#spaceResult').fadeIn();

    });
    $('#Atimespace').bind('click',function(){
        document.cookie = 'timeOrspace = 2';
        tabIndex = 2;
        showMinPageIndex = showMinPageIndexs[3];
        showMaxPageIndex = showMaxPageIndexs[3];
        if(jsonDatas[3]!==undefined){
            jsonData = jsonDatas[3];
            currentPage = currentPages[3];
            sumResultNum = sumResultNums[3];
            constructPagination();
            bindLi();
        }
        $(this).addClass('active');
        $(this).prev().removeClass();
        $(this).prev().prev().removeClass();
        $('#timeResult').hide();
        $('#spaceResult').hide();
        $('#timespaceResult').fadeIn();
    });
    init();
    function checkCookie(param){
        if(document.cookie.length>0){
        	console.log('-----------'+document.cookie.length)
            var start = document.cookie.indexOf(param);
            var end;
            if(start !==-1){
                start = start +(param).length;
                end = document.cookie.indexOf(';',start);
                if(end === -1)end = document.cookie.length;
                if(param==='timeOrspace='){
                	timeOrspace=parseInt(document.cookie.substring(start,end));
                }else if(param==='tabIndex=')tabIndex = parseInt(document.cookie.substring(start,end));
            }
        }else{
            var tabIndex = 0;
            var timeOrspace = 0;
        }
    }


    function bindLi(){
        if(tabIndex === 0){
            if(timeOrspace ===0){
                $('#paginatercc').empty();
                $('#paginatetimespace').empty();
                $('#paginate').empty();
            }else if(timeOrspace ===1){
                $('#paginatetimespace').empty();
                $('#paginate').empty();
                $('#paginatekey').empty();
            }else{
                $('#paginatercc').empty();
                $('#paginate').empty();
                $('#paginatekey').empty();
            }
        }else if (tabIndex === 1){
            $('#paginatercc').empty();
            $('#paginatetimespace').empty();
            $('#paginatekey').empty();

        }
        var lis = document.querySelectorAll('#page li');
        $('#confirm').bind('click',confirmListener);
        $('#pre').bind('click',preListener);
        $('.inputpage').keydown(function(e){
            if(e.keyCode == 13){
                confirmListener();
            }
        });
        $('#itemMax').bind('click',nextListener);
        $('#next').bind('click',nextListener);
        for(var i=1;i<lis.length-1;i++){
            if(i!==lis.length-2||!(lis[i].id.toString()==='itemMax')){
                lis[i].onclick=liListener;
            }
        }
    }

    function confirmListener(){
        var skipNum = parseInt($('.inputpage').val().trim());

        var selectedElement = $('#selected');
        if(skipNum<1||!skipNum||skipNum>sumPageNum){
            alert("Invalid PageNum!");
            return;
        }
        if(skipNum >= showMinPageIndex && skipNum <= showMaxPageIndex){
            if(skipNum !== parseInt(selectedElement.text())){
                document.getElementById('item'+skipNum).firstChild.id = 'selected';
                selectedElement.attr('id','item'+selectedElement.text());
            }

        }
        else{
            showMaxPageIndex = (skipNum+5)>sumPageNum?sumPageNum:skipNum+5;
            showMaxPageIndex = showMaxPageIndex<12?12:showMaxPageIndex;
            showMinPageIndex = showMaxPageIndex - 11;
            constructPagination();
            bindLi();
            selectedElement = $('#selected');
            document.getElementById('item'+skipNum).firstChild.id = 'selected';
            selectedElement.attr('id','item'+selectedElement.text());
            $('.inputpage').val(skipNum);
        }
        if(tabIndex === 0){
            if(timeOrspace === 0){
                currentPages[0] = currentPage = skipNum;
                timeRelSearch(currentPage);
            }else if(timeOrspace === 1){
                currentPages[2] = currentPage = skipNum;
                rccSearch(currentPage);
            }else{
                currentPages[3] = currentPage = skipNum;
                timespaceSearch(currentPage);
            }
        }else if(tabIndex === 1){
            currentPages[1] = currentPage = skipNum;
            paramSearch(currentPage);
        }
    }

    function liListener(){
        var pageNum = parseInt(this.firstChild.innerHTML);
        if(tabIndex === 0){
            if(timeOrspace === 0){
                currentPages[0] = currentPage = pageNum;
                timeRelSearch(currentPage);
            }else if(timeOrspace === 1){
                currentPages[2] = currentPage = pageNum;
                rccSearch(currentPage);
            }else{
                currentPages[3] = currentPage = pageNum;
                timespaceSearch(currentPage);
            }
        }else if(tabIndex === 1){
            currentPages[1] = currentPage = pageNum;
            paramSearch(currentPage);
        }
        var aElement = $('#selected');

        aElement.attr('id','item'+aElement.text());
        this.firstChild.id = 'selected';
        $('.inputpage').val(pageNum);
    }
    function preListener(){
        var selectedElement = $('#selected');
        var pageNow = parseInt(selectedElement.text());
        if(pageNow>1){
            var preLi;

            if(showMinPageIndex>1){

                var aElement = $('<li>',{
                    class:'pagination__item',
                    id:'item'+(showMinPageIndex-1)

                });

                //aElement.bind('click',liListener(this));

                aElement.html('<a class="pagination__number">'+(showMinPageIndex-1)+'</a>');
                $('#item'+showMinPageIndex).before(aElement);
                preLi = document.getElementById('item'+(showMinPageIndex-1));
                preLi.onclick=liListener;

                $('#item'+showMaxPageIndex).remove();
                $('#item'+(showMinPageIndex-1)).show();
                showMinPageIndex = showMinPageIndex-1;
                showMaxPageIndex = showMaxPageIndex-1;
                $('#itemMax').show();
            }


            preLi = document.getElementById('item'+(pageNow-1));
            preLi.firstChild.id = 'selected';

            selectedElement.attr('id','item'+selectedElement.text());
            upadtePageNum(pageNow-1,showMinPageIndex,showMaxPageIndex);

            $('.inputpage').val(currentPage);
        }
    }
    function upadtePageNum(pageNum,showMinPageIndex,showMaxPageIndex) {
        if(tabIndex === 0){
            if(timeOrspace === 0){
                currentPages[0] = currentPage = pageNum;
                showMaxPageIndexs[0] = showMaxPageIndex;
                showMinPageIndexs[0] = showMinPageIndex;
                timeRelSearch(currentPage);
            }else if(timeOrspace ===1){
                currentPages[2] = currentPage = pageNum;
                showMaxPageIndexs[2] = showMaxPageIndex;
                showMinPageIndexs[2] = showMinPageIndex;
                rccSearch(currentPage);
            }else if(timeOrspace ===2){
                currentPages[3] = currentPage = pageNum;
                showMaxPageIndexs[3] = showMaxPageIndex;
                showMinPageIndexs[3] = showMinPageIndex;
                timespaceSearch(currentPage);
            }

        }else if(tabIndex === 1){
            currentPages[1] = currentPage = pageNum;
            showMaxPageIndexs[1] = showMaxPageIndex;
            showMinPageIndexs[1] = showMinPageIndex;
            paramSearch(currentPage);
        }
    }
    function nextListener(){
        var selectedElement = $('#selected');
        var pageNow = parseInt(selectedElement.text());
        //console.log("pageNow"+pageNow);
        var nextLi;
        if(pageNow<sumPageNum){


            selectedElement.attr('id','item'+selectedElement.text());
            if(showMaxPageIndex<sumPageNum){

                $('#item'+showMinPageIndex).remove();
                showMinPageIndex = showMinPageIndex+1;

                var maxItem =$('#itemMax');
                //console.log("存在-------"+document.getElementById('#item'+(showMaxPageIndex+1)));
                var aElement = $('<li>',{
                    class:'pagination__item',
                    id:'item'+(showMaxPageIndex+1)

                });

                //aElement.bind('click',liListener(this));

                aElement.html('<a class="pagination__number">'+(showMaxPageIndex+1)+'</a>');
                maxItem.before(aElement);
                nextLi = document.getElementById('item'+(showMaxPageIndex+1));
                nextLi.onclick=liListener;
                showMaxPageIndex = showMaxPageIndex+1;

            }
            if(showMaxPageIndex === sumPageNum){
                $('#itemMax').hide();
            }
            upadtePageNum(pageNow+1,showMinPageIndex,showMaxPageIndex);
            $('.inputpage').val(currentPage);
            //console.log("pageNow==="+pageNow);
            var nextLi = document.getElementById('item'+(pageNow+1));
            nextLi.firstChild.id = 'selected';
        }
    }

    /*    function displayPage(pageNum){
            var startIndex = (pageNum-1)*5;
            var endIndex = startIndex+5;
            showList(jsonData.slice(startIndex,endIndex));
        }*/



    function constructPagination() {//进行分页显示
        var paginatediv;
        if(tabIndex === 0){
            if(timeOrspace ===0){
                paginatediv = $('#paginatekey');
            }else if(timeOrspace ===1){
                paginatediv = $('#paginatercc');
            }else{
                paginatediv = $('#paginatetimespace');
            }


        }else if (tabIndex === 1){

            paginatediv = $('#paginate');
        }
        paginatediv.empty();

        if(sumResultNum % 5 === 0)
            sumPageNum = Math.floor(sumResultNum / 5);
        else
            sumPageNum = Math.floor(sumResultNum / 5) + 1;

        if(sumPageNum>1){

            var ulElement;
            ulElement = $('<ul>',{
                class:"pagination page",
                id:"page"
            });
            var preliElement = $('<li>',{
                class:'pagination__item',
                id:'pre'

            });

            preliElement.html('<a class="pagination__number">←<span class="pagination__control pagination__control_prev"  >prev</span></a>');
            ulElement.append(preliElement);
            //console.log("currentPage===="+currentPage);
            showMaxPageIndex = showMaxPageIndex<sumPageNum?showMaxPageIndex:sumPageNum;
            var eliElement;
            for(var i=showMinPageIndex;i<=showMaxPageIndex;i++){
                eliElement = $('<li>',{
                    class:'pagination__item',
                    id:'item'+i

                });
                if(i === currentPage){
                    eliElement.html('<a class="pagination__number"  id="selected">'+i+'</a>');
                }else{
                    eliElement.html('<a class="pagination__number">'+i+'</a>');
                }

                ulElement.append(eliElement);
            }
            eliElement = $('<li>',{
                class:'pagination__item',
                id:'itemMax'
            });
            eliElement.html('<a class="pagination__number">'+'...'+'</a>');
            ulElement.append(eliElement);
            eliElement.hide();
            if(sumPageNum>showMaxPageIndex){
                eliElement.show();
            }


            var nextliElement = $('<li>',{
                class:"pagination__item",
                id:"next"
            });
            nextliElement.html('<a class="pagination__number"><span class="pagination__control pagination__control_next">next</span>→</a>');
            var span = $('<span>',{
                class:'skip-page'
            });
            var em = $('<em>');
            em.html('共'+'<b>'+sumPageNum+'</b>页&nbsp;&nbsp;到第');
            span.append(em);
            var input = $('<input class="inputpage" type="text" value ="1" >');
            span.append(input);

            span.append("<em>页</em>");
            /*            var confirm = $('<li>',{
                            class:"pagination__item",
                            id:"confirm"
                        });*/
            var confirm =$('<a id="confirm" >'+'确定'+'</a>');
            span.append(confirm);

            ulElement.append(nextliElement);
            //ulElement.append(span);
            paginatediv.append(ulElement);
            paginatediv.append(span);

        }
    }

    function showList(){

        var result;
        if(tabIndex === 1){
            result =$("#entityResult1");
            result.empty();
        }
        else {
            result =$("#entityResult");
            result.empty();
        }

        if(jsonData.length<=0){
            //console.log('data-------'+jsonData.length);
            if(tabIndex === 1){
                $("#loading1").hide();
                result.append($('<span>').html('Nothing found!'));
            }else{
                $("#loading").hide();
                result.append($('<span>').html('Nothing found!'));
            }
            result.fadeIn();
        }
        var i = 0;
        result.hide();
        var paged = $('#paginate').hide();
        _.each(jsonData,function(d){


            var queryStr = "entityInformation.jsp?" + d.uri;
            var str = "<a href="+queryStr+" target='_blank' class='resultLabel'>"+ d.label+"</a>";
            for(var p in d){
                if(p !=="label"&&p !== "sumResult"){
                    d[p]=d[p].length>61?(d[p].substring(0,61)+"......"):d[p];
                    str += "</br><span>"+ p +'：</span><span>'+d[p]+"</span>"
                }
            }
            var liElement = $('<li>',{
                class:'prop'
            });
            liElement.css('background-image','url('+img[i]+')');
            i++;
            liElement.html(str);
            if(tabIndex===1){
                result.append(liElement);
                //$("#entityResult1").append('<li class = "prop" style="background-image: url("http://arsenal.chinaiiss.com/attachment/image/201209/06/145756_359.gif");">'+str+'</li>');
            }else{
                result.append(liElement);
                //$("#entityResult").append('<li class = "prop" style="background-image: url("http://arsenal.chinaiiss.com/attachment/image/201209/06/145756_359.gif");">'+str+'</li>');
            }
        });

        if(tabIndex === 1){
            result.fadeIn('slow');
            paged.fadeIn('slow');
        }else{
            //result.fadeIn("slow");
            result.fadeIn('slow');
            paged.fadeIn('slow');
        }

    }

    $("#paramQuery").on("click",function(){
        document.cookie = 'tabIndex = 1';
        tabIndex = 1;
        showMinPageIndex = showMinPageIndexs[1];
        showMaxPageIndex = showMaxPageIndexs[1];
        if(jsonDatas[1]!==undefined){
            jsonData = jsonDatas[1];

            currentPage = currentPages[1];
            sumResultNum = sumResultNums[1];
            constructPagination();
            bindLi();
        }
        $(this).addClass("active");
        $(this).prev().removeClass("active");
        $(this).next().removeClass("active");
        $("#nameCon").hide();
        //$("#rccCon").hide();
        $("#paramCon").fadeIn();


    });
    $("#nameQuery").on("click",function(){
        document.cookie = 'tabIndex = 0';
        tabIndex = 0;
        showMinPageIndex = showMinPageIndexs[0];
        showMaxPageIndex = showMaxPageIndexs[0];
        if(jsonDatas[0]!==undefined){

            jsonData = jsonDatas[0];
            currentPage = currentPages[0];
            sumResultNum = sumResultNums[0];
            constructPagination();
            bindLi();
        }

        $(this).addClass("active");
        $(this).next().removeClass("active");
        $(this).next().next().removeClass("active");
        $("#paramCon").hide();
        //$("#rccCon").hide();
        $("#nameCon").fadeIn();

    });
    $("#add").on("click",function(){
        var newNode=$($(".paramInputCon")[0]).clone();
        newNode.find("img").hide();
        var str="<select class='relation'><option>与</option><option>或</option></select>";
        $("#conditionInput").append(newNode);
        $(str).insertBefore(".paramInputCon:last-of-type select");
    });
    $("#delete").on("click",function(){
        if($(".paramInputCon").length>1){
            $(".paramInputCon:last-child").remove();
        }

    });



    function keywordSearch(pageNum){
        $("#loading").show();
        $.ajax({
            url:"KeywordSearchServlet",
            dataType:'text',
            type:'get',
            data:{searchValue:$("#search").val(),pageNum:pageNum},
            success:function(data){
                $('#loading').hide();
                $('#entityResult').empty();
                jsonData = JSON.parse(data);
                jsonDatas[0] = jsonData;
                showList();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading').hide();
                var entityResult = $('#entityResult');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));
            }
        });
    }
    function paramSearch(pageNum){
        //$("#loading1").show();
        var selectedNodes=getLeafNodes();
        console.log("------"+selectedNodes.join("&"));
        $.ajax({
            url:"ParamSearchServlet",
            dataType:'text',
            type:'get',
            data:{'type':selectedNodes.join("&"),'keyword':$('.sb-search-input').val(),'properties':$('#propsInput').val(),pageNum:pageNum},
            success:function(data){
                //$("#loading1").hide();
                $('#entityResult1').empty();
                jsonData = JSON.parse(data);
                jsonDatas[1] = jsonData;
                showList();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading1').hide();
                var entityResult1 = $('#entityResult1');
                entityResult1.empty();
                entityResult1.append($('<span>').html(textStatus));
            }
        });
    }
    function timeRelSearch(num){
        //$("#loading").show();
        $.ajax({
            url:"DateRelationServlet",
            dataType:'text',
            type:'get',
            data:{'startDate':$('#startDate').val(),'endDate':$('#endDate').val(),'rel':$('#dateSelect option:selected').val(),'keyword':$('#keyword').val(),'pageNum':num+''},
            //data:{startDate:$("#search").val(),endDate:'1'},
            success:function(data){
                //$("#loading").hide();
                var result = $("#entityResult");
                result.empty();
                //console.log("data====="+data);
                jsonData=JSON.parse(data);
                jsonDatas[0] = jsonData;

                if(jsonData.length<=0){
                    //console.log('data-------'+jsonData.length);
                    result.append($('<span>').html('Nothing found!'));
                    result.fadeIn();

                }
                var i=0;
                //var paginatekey = $('#paginatekey');
                result.hide();
                //paginatekey.hide();
                _.each(jsonData,function(d){
                    i++;
                    var liElement = $('<li>',{
                        class:'news_content'
                    });
//                    liElement.html('<span style="width:200px">'+d['content']+"</span>");
                    liElement.html('<a href='+"newsDetail.jsp?"+d['pageurl']+' style="width:200px" target="_blank">'+d['title']+"</a>");
                    result.append(liElement);
                    var descdiv = $('<div>',{
                        class:'news_desc'
                    });
                    descdiv.html(d['content']);
                    var startdiv = $('<div>',{
                        class:'post-meta'
                    });
                    startdiv.html(d['startdate'].substring(0,10));
                    var enddiv = $('<div>',{
                        class:'post-meta'
                    });
                    enddiv.html(d['enddate'].substring(0,10));
                    //$('#entityResult').append($('</br>'));
                    result.append(descdiv);
                    result.append(startdiv);
                    result.append(enddiv);
                    result.append($('</br>'));
                    if(i<jsonData.length)
                        result.append($('</br>'));

                });
                result.fadeIn();
               // paginatekey.fadeIn();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading').hide();
                var entityResult = $('#entityResult');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));
                entityResult.fadeIn();
            }
        });

    }
    function rccSearch(num){
    	console.log($('#firstEntity').val());
        $.ajax({
            url:'RccRelationServlet',
            dataType:'text',
            type:'get',
            data:{'location':$('#firstEntity').val(),'distance':$('#secondEntity').val(),'rccRel':$('#rccSelect option:selected').val(),'pageNum':num},
            success:function(data){
                // $("#rccloading").hide();

                var result = $("#rccResult");
                result.empty();
                //console.log("data====="+data);
                jsonData=JSON.parse(data);

                if(jsonData.length<=0){
                    //console.log('data-------'+jsonData.length);
                    result.append($('<span>').html('Nothing found!'));
                    result.show();
                    return;
                }


                var i=0;

                result.hide();

                _.each(jsonData,function(d){
                    i++;
                    var liElement = $('<li>',{
                        class:'news_content'
                    });
                    liElement.html('<a href='+"newsDetail.jsp?"+d['pageurl']+' style="width:200px" target="_blank">'+d['title']+"</a>");
                    var descdiv = $('<div>',{
                        class:'news_desc'
                    });
                    descdiv.html(d['content']);
                    result.append(liElement);
                    var startdiv = $('<div>',{
                        class:'post-meta'
                    });
                    startdiv.html(d['startdate'].substring(0,10));
                    var enddiv = $('<div>',{
                        class:'post-meta'
                    });
                    enddiv.html(d['enddate'].substring(0,10));
                    //$('#entityResult').append($('</br>'));
                    result.append(descdiv);
                    result.append(startdiv);
                    result.append(enddiv);
                    result.append($('</br>'));
                    if(i<jsonData.length)
                        result.append($('</br>'));

                });
                result.fadeIn();

            },
            error:function(request,textStatus,errorThrown){
                $('#loading').hide();
                var entityResult = $('#rccResult');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));

            }

        });
    }
    function timespaceSearch(pageNum){
    	console.log('========='+$('#ST-keyword').val())
        $.ajax({
            url: 'timespaceSearch',
            dataType: 'text',
            type: 'get',
            data: {
                'startTime': $('#startTime').val(),
                'endTime': $('#endTime').val(),
                'timeRel': $('#timespace-dateSelect option:selected').val(),
                'loc': $('#loc').val(),
                'spaceRel': $('#space-rel option:selected').val(),
                'locValue': $('#value').val(),
                'keyword':$('#ST-keyword').val(),
                'pageNum':pageNum+''
            },
            success:function(data){
                tabIndex = 0;
                timeOrspace = 2;
                var  result = $('#timespacelist');
                result.empty();
                //console.log("data====="+data);
                jsonData=JSON.parse(data);

                if(jsonData.length<=0){
                    //console.log('data-------'+jsonData.length);
                    result.append($('<span>').html('Nothing found!'));
                    result.show();
                    return;
                }
                if(pageNum === 1){
                    showMinPageIndexs[3] = 1;
                    showMaxPageIndexs[3] = 12;
                    showMinPageIndex = 1;
                    showMaxPageIndex = 12;
                    sumResultNum = parseInt((jsonData.length === 0)?'0':jsonData[0]['sumResult']);
                    sumResultNums[3] = sumResultNum;
                    currentPage=1;
                    currentPages[3] = currentPage;
                    constructPagination();
                    bindLi();
                }
                var i=0;
               // var paginate = $('#paginatetimespace');
                result.hide();
                _.each(jsonData,function(d){
                    i++;
                    var liElement = $('<li>',{
                        class:'news_content'
                    });
//                    liElement.html('<span style="width:200px">'+d['content']+"</span>");
                    liElement.html('<a href='+"newsDetail.jsp?"+d['pageurl']+' style="width:200px" target="_blank">'+d['title']+"</a>");
                    result.append(liElement);
                    var descdiv = $('<div>',{
                        class:'news_desc'
                    });
                    descdiv.html(d['content']);
                    var startdiv = $('<div>',{
                        class:'post-meta'
                    });
                    startdiv.html(d['startdate'].substring(0,10));
                    var enddiv = $('<div>',{
                        class:'post-meta'
                    });
                    enddiv.html(d['enddate'].substring(0,10));
                    //$('#entityResult').append($('</br>'));
                    result.append(descdiv);
                    result.append(startdiv);
                    result.append(enddiv);
                    result.append($('</br>'));
                    if(i<jsonData.length)
                        result.append($('</br>'));

                });
                result.fadeIn();
                //paginate.fadeIn();

            }
        })
    }
    $('#timespace-search').bind('click',function(){
        if($('#startTime').val()===''||$('#endTime').val()===''||$('#loc').val()===''||$('#value').val()===''){
            var entityResult = $('#timespacelist');
            entityResult.empty();
            entityResult.append($('<span>').html('请补全搜索值'));
            entityResult.fadeIn();
            return;
        }
        console.log("timespace");

        timespaceSearch(1);
    });

    $('#searchRccBtn').bind('click',function(){
        if($('#firstEntity').val()==='' || $('#secondEntity').val()===''){
            var entityResult = $('#rccResult');
            entityResult.empty();
            entityResult.append($('<span>').html('请补全搜索值'));
            entityResult.fadeIn();
            return;
        }
        //$('#rccloading').show();
        $.ajax({
            url:'RccRelationServlet',
            dataType:'text',
            type:'get',
            data:{'location':$('#firstEntity').val(),'distance':$('#secondEntity').val(),'rccRel':$('#rccSelect option:selected').val(),'pageNum':'1'},
            success:function(data){
                tabIndex = 0;
                timeOrspace = 1;
                // $("#rccloading").hide();
                showMinPageIndexs[2] = 1;
                showMaxPageIndexs[2] = 12;
                showMinPageIndex = 1;
                showMaxPageIndex = 12;
                var result = $("#rccResult");
                result.empty();
                //console.log("data====="+data);
                jsonData=JSON.parse(data);
                jsonDatas[2] = jsonData;
                if(jsonData.length<=0){
                    //console.log('data-------'+jsonData.length);
                    result.append($('<span>').html('Nothing found!'));
                    result.show();
                    return;
                }
                sumResultNum = parseInt((jsonData.length === 0)?'0':jsonData[0]['sumResult']);
                sumResultNums[2] = sumResultNum;
                currentPage=1;
                currentPages[2] = currentPage;
                constructPagination();
                bindLi();
                var i=0;
                var paginatekey = $('#paginatekey');
                result.hide();
                paginatekey.hide();
                _.each(jsonData,function(d){
                    i++;
                    var liElement = $('<li>',{
                        class:'news_content'
                    });
                    liElement.html('<a href='+"newsDetail.jsp?"+d['pageurl']+' style="width:200px" target="_blank">'+d['title']+"</a>");
                    var descdiv = $('<div>',{
                        class:'news_desc'
                    });
                    descdiv.html(d['content']);
                    result.append(liElement);
                    var startdiv = $('<div>',{
                        class:'post-meta'
                    });
                    startdiv.html(d['startdate'].substring(0,10));
                    var enddiv = $('<div>',{
                        class:'post-meta'
                    });
                    enddiv.html(d['enddate'].substring(0,10));
                    //$('#entityResult').append($('</br>'));
                    result.append(descdiv);
                    result.append(startdiv);
                    result.append(enddiv);
                    result.append($('</br>'));
                    if(i<jsonData.length)
                        result.append($('</br>'));

                });
                result.fadeIn();
                paginatekey.fadeIn();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading').hide();
                var entityResult = $('#rccResult');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));

            }

        });
    });
    $('#searchDateBtn').bind('click',function(){
        //$("#loading").show();
        $.ajax({
            url:"DateRelationServlet",
            dataType:'text',
            type:'get',
            data:{'startDate':$('#startDate').val(),'endDate':$('#endDate').val(),'rel':$('#dateSelect option:selected').val(),'keyword':$('#keyword').val(),'pageNum':'1'},
            success:function(data){
                tabIndex  = 0;
                timeOrspace = 0;
                showMinPageIndexs[0] = 1;
                showMaxPageIndexs[0] = 12;
                showMinPageIndex = 1;
                showMaxPageIndex = 12;
                // $("#loading").hide();
                var result = $("#entityResult");
                result.empty();
                //console.log("data====="+data);
                jsonData=JSON.parse(data);
                jsonDatas[0] = jsonData;
                if(jsonData.length<=0){
                    //console.log('data-------'+jsonData.length);
                    result.append($('<span>').html('Nothing found!'));
                    result.show();
                    return;
                }
                sumResultNum = parseInt((jsonData.length === 0)?'0':jsonData[0]['sumResult']);
                sumResultNums[0] = sumResultNum;
                currentPage=1;
                currentPages[0] = currentPage;
                constructPagination();
                bindLi();
                var i=0;
                var paginatekey = $('#paginatekey');
                result.hide();
                paginatekey.hide();
                _.each(jsonData,function(d){
                    i++;
                    var liElement = $('<li>',{
                        class:'news_content'
                    });
                    liElement.html('<a href='+"newsDetail.jsp?"+d['pageurl']+' style="width:200px" target="_blank">'+d['title']+"</a>");
                    var descdiv = $('<div>',{
                        class:'news_desc'
                    });
                    descdiv.html(d['content']);
                    result.append(liElement);
                    var startdiv = $('<div>',{
                        class:'post-meta'
                    });
                    startdiv.html(d['startdate'].substring(0,10));
                    var enddiv = $('<div>',{
                        class:'post-meta'
                    });
                    enddiv.html(d['enddate'].substring(0,10));
                    //$('#entityResult').append($('</br>'));
                    result.append(descdiv);
                    result.append(startdiv);
                    result.append(enddiv);
                    result.append($('</br>'));
                    if(i<jsonData.length)
                        result.append($('</br>'));

                });
                result.fadeIn();
                paginatekey.fadeIn();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading').hide();
                var entityResult = $('#entityResult');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));

            }
        });
    });
    /* $("#searchSubmit").on("click",function(){
         $("#loading").show();
         //此处为接口1
         $.ajax({
             url:"KeywordSearchServlet",
             dataType:'text',
             type:'get',
             data:{searchValue:$("#search").val(),pageNum:'1'},
             success:function(data){
                 console.log("接口1:",{searchValue:$("#search").val()});
                 $("#loading").hide();
                 $("#entityResult").empty();
                 jsonData=JSON.parse(data);
                 jsonDatas[0] = jsonData;
                 //	var data =[{label:"YF-12战斗机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/yf_12",author:"china",price:"$8900"},
                 //	{label:"VS-300直升机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/dytzsj/vs_300",price:"y08838"}];
                 tabIndex  = 0;
                 showMinPageIndexs[0] = 1;
                 showMaxPageIndexs[0] = 12;
                 showMinPageIndex = 1;
                    showMaxPageIndex = 12;
                 sumResultNum = parseInt((jsonData.length === 0)?0:jsonData[0]['sumResult']+"");
                 sumResultNums[0] = sumResultNum;
                 currentPage=1;
                 currentPages[0] = currentPage;
                 showList();
                 //console.log("sumResultNum===="+jsonData[0]['sumResult']+sumResultNum)
                 constructPagination();
                 bindLi();
             },
             error:function(request,textStatus,errorThrown){
                 $('#loading').hide();
                 var entityResult = $('#entityResult');
                 entityResult.empty();
                 entityResult.append($('<span>').html(textStatus));

             }
         });
     });*/

    $('#searchBtn').bind('click',function(){
        //$("#loading1").show();
        var selectedNodes=getLeafNodes();
        console.log("------"+selectedNodes.join("&"));
        $.ajax({
            url:"ParamSearchServlet",
            dataType:'text',
            type:'get',
            data:{'type':selectedNodes.join("&"),'keyword':$('.sb-search-input').val(),'properties':$('#propsInput').val(),pageNum:'1'},
            success:function(data){
                //$("#loading1").hide();

                jsonData = JSON.parse(data);
                jsonDatas[1] = jsonData;
                //	var data=[{name:"YF-12战斗机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/yf_12",author:"china",price:"$8900"},
                //		{name:"VS-300直升机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/dytzsj/vs_300",price:"y08838"}];
                tabIndex = 1;
                showMinPageIndexs[1] = 1;
                showMaxPageIndexs[1] = 12;
                showMinPageIndex = 1;
                showMaxPageIndex = 12;
                sumResultNum = parseInt((jsonData.length === 0)?0:jsonData[0]['sumResult']+"");
                sumResultNums[1] = sumResultNum;
                currentPage = 1;
                currentPages[1] = currentPage;
                showList();
                constructPagination();
                bindLi();
            },
            error:function(request,textStatus,errorThrown){
                $('#loading1').hide();
                var entityResult = $('#entityResult1');
                entityResult.empty();
                entityResult.append($('<span>').html(textStatus));

            }
        })

    });

    /*  $("#searchBtn").on("click",function(){
          var datas=[];
          $("#loading1").show();
          $(".paramInputCon input").each(function(i,item){
              if($(item).val().trim()!=""){
                  var obj={};
                  obj.attributeName=$(item).prev().val();
                  obj.attributeVal=$(item).val();
                  obj.relation="none";
                  if($(item).prev().prev().hasClass("relation")){
                      if($(item).prev().prev().val()=="与"){
                          obj.relation="and";
                      }else{
                          obj.relation="or";
                      }
                  }
                  datas.push(obj);
              }
          });
          //左侧树处理
          var selectedNodes=getLeafNodes();
          datas.push({attributeName:"类型",attributeVal:selectedNodes.join(" "),relation:"and"});
          //此处为接口2
          //console.log("接口2:",{queryParams:datas});
          $.ajax({
              url:"ParamSearchServlet",
              dataType:'text',
              type:'get',
              data:{queryParams:datas,pageNum:'1'},
              success:function(data){
                  $("#loading1").hide();

                  jsonData = JSON.parse(data);
                  jsonDatas[1] = jsonData;
                  //	var data=[{name:"YF-12战斗机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/zdj/yf_12",author:"china",price:"$8900"},
                  //		{name:"VS-300直升机@zh",uri:"http://ws.nju.edu.cn/qademo/Weapon/Aircraft/dytzsj/vs_300",price:"y08838"}];
                  tabIndex = 1;
                  showMinPageIndexs[1] = 1;
                  showMaxPageIndexs[1] = 12;
                  showMinPageIndex = 1;
                  showMaxPageIndex = 12;
                  sumResultNum = parseInt((jsonData.length === 0)?0:jsonData[0]['sumResult']+"");
                  sumResultNums[1] = sumResultNum;
                  currentPage = 1;
                  currentPages[1] = currentPage;
                  showList();
                  constructPagination();
                  bindLi();
              },
              error:function(request,textStatus,errorThrown){
                  $('#loading1').hide();
                  var entityResult = $('#entityResult1');
                  entityResult.empty();
                  entityResult.append($('<span>').html(textStatus));

              }
          })
      });*/
    function getLeafNodes(){
        var obj = document.all.leaf;
        var result=[];
        for(var i=0;i<obj.length;i++){
            if(obj[i].checked){
                result.push(obj[i].value)
            }
        }
        return result;
    }
    /*    function bindSelect(){
            $('.proper').change(function(){

            });
        }*/
    function initLabel(){
        /*    	$('#propsInput').textext({
                    plugins:'tags prompt',
                    prompt: ' Add one...'
                });*/
        $.getJSON("js/prop.json",function(data){
            var select = $('.proper');
            _.each(data,function(d){
                var option = $('<option>',{
                    class:'prop_item'
                });
                option.html(d['property']);
                select.append(option);
                //alert(d['property']);
            });

        });

        $('a.label').bind('click',function(){
            console.log("selected====="+$('.proper option:selected').val());
            var inputprop  = document.getElementById('propsInput');
            inputprop.focus();
            var start =inputprop.selectionStart;
            var end = inputprop.selectionEnd;
            if('round'==this.id.toString()){
                // $('#propsInput').val ( "( )");
                inputprop.value = inputprop.value.substring(0,start)+"( )"+inputprop.value.substring(end,inputprop.value.length);
                inputprop.setSelectionRange(start+2,start+2);
                return;
            }else if('and'==this.id.toString()||'or'==this.id.toString()){
                inputprop.value = inputprop.value.substring(0,start)+" "+this.text+" "+inputprop.value.substring(end,inputprop.value.length);
                inputprop.setSelectionRange(start+5,start+5);
                return;
            }else if('addProp'==this.id.toString()){
                var select = document.getElementById('propSelect');
                var index = select.selectedIndex;
                inputprop.value = inputprop.value.substring(0,start)+select.options[index].value+" : "+inputprop.value.substring(end,inputprop.value.length);
                var lastStart = inputprop.value.toString().lastIndexOf(":");
                inputprop.setSelectionRange(lastStart+2,lastStart+2);
                return;
            }
            inputprop.value = inputprop.value.substring(0,start)+this.text+" : "+inputprop.value.substring(end,inputprop.value.length);
            var lastStart = inputprop.value.toString().lastIndexOf(":");
            inputprop.setSelectionRange(lastStart+1,lastStart+1);

            /*    		$('#propsInput').textext()[0].tags().addTags([ this.text+":" ]);
                        console.log(this.id);*/
        });
    }
    /*    function tagStyleSetter(item) {

        }*/
    /*    $(".form_datetime").datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            language:'zh-CN',
            pickerPosition:"bottom-left"
          });
    */
    $(".date").datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        showMeridian: true,
        pickerPosition: "bottom-left",
        language: 'zh-CN',//中文，需要引用zh-CN.js包
        startView: 2,//月视图
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
    });
});