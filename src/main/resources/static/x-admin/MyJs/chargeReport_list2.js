$(function(){
	queryUsersFY();
});

/*****S 分页查询相关方法**********/
function queryUsersFY(){
    $.ajax({
       url:"/firstcharge/pageFirstChargeReport2.html",
       data:$("#queryUserForm").serialize(),
       dataType:'json',
        type:'post',
        async:true,
        success:showUsersTable
    });
}

function reDisposeResult(value) {
	if ("-" == value) {
		return value;
	}
	
	return "1" + " / " + NumberUtils.toFixedDecimal(value);
}

/**
 * 填充表格数据
 * @param pageInfo  ajax返回的参数信息
 */
function showUsersTable(pageInfo){
    var total = pageInfo.total;//页总数
    var pageNum = pageInfo.pageNum;//页号
    var pageSize = pageInfo.pageSize;//页大小

    var users = pageInfo.list;
    $("#memberTbody").html("");//清空表格中数据并重新填充数据

    for(var i=0,length_1 = users.length;i<length_1;i++){
        var index = (pageNum - 1) * pageSize + i + 1;
        var tr = "<tr>"
            +'<td>'+index+'</td>'
            +'<td>'+replaceNull(users[i].user_name)+'</td>'
            +'<td><a href=javascript:void(0) onclick="updateSecondParentName(' + users[i].id + ',\'' + replaceNull(users[i].second_parent_name) + '\',this)"> ' + replaceNull(users[i].second_parent_name) + '</a></td>'
            +'<td>'+replaceNull(users[i].parent_name)+'</td>'
            +'<td>'+replaceNull(users[i].gmt_created, 10)+'</td>'
            +'<td class="day0">'+reDisposeResult(users[i].第0天)+'</td>'
            +'<td class="day1">'+reDisposeResult(users[i].第1天)+'</td>'
            +'<td class="day2">'+reDisposeResult(users[i].第2天)+'</td>'
            +'<td class="day3">'+reDisposeResult(users[i].第3天)+'</td>'
            +'<td class="day4">'+reDisposeResult(users[i].第4天)+'</td>'
            +'<td class="day5">'+reDisposeResult(users[i].第5天)+'</td>'
            +'<td class="day6">'+reDisposeResult(users[i].第6天)+'</td>'
            +'<td class="day7">'+reDisposeResult(users[i].第7天)+'</td>'
            +'<td class="day8">'+reDisposeResult(users[i].第8天)+'</td>'
            +'<td class="day9">'+reDisposeResult(users[i].第9天)+'</td>'
            +'<td class="day10">'+reDisposeResult(users[i].第10天)+'</td>'
            +'<td class="day11">'+reDisposeResult(users[i].第11天)+'</td>'
            +'<td class="day12">'+reDisposeResult(users[i].第12天)+'</td>'
            +'<td class="day13">'+reDisposeResult(users[i].第13天)+'</td>'
            +'<td class="day14">'+reDisposeResult(users[i].第14天)+'</td>'
            +'<td class="day15">'+reDisposeResult(users[i].第15天)+'</td>'
            +'<td class="day16">'+reDisposeResult(users[i].第16天)+'</td>'
            +'<td class="day17">'+reDisposeResult(users[i].第17天)+'</td>'
            +'<td class="day18">'+reDisposeResult(users[i].第18天)+'</td>'
            +'<td class="day19">'+reDisposeResult(users[i].第19天)+'</td>'
            +'<td class="day20">'+reDisposeResult(users[i].第20天)+'</td>'
            +'<td class="day21">'+reDisposeResult(users[i].第21天)+'</td>'
            +'<td class="day22">'+reDisposeResult(users[i].第22天)+'</td>'
            +'<td class="day23">'+reDisposeResult(users[i].第23天)+'</td>'
            +'<td class="day24">'+reDisposeResult(users[i].第24天)+'</td>'
            +'<td class="day25">'+reDisposeResult(users[i].第25天)+'</td>'
            +'<td class="day26">'+reDisposeResult(users[i].第26天)+'</td>'
            +'<td class="day27">'+reDisposeResult(users[i].第27天)+'</td>'
            +'<td class="day28">'+reDisposeResult(users[i].第28天)+'</td>'
            +'<td class="day29">'+reDisposeResult(users[i].第29天)+'</td>'
            +'<td class="day30">'+reDisposeResult(users[i].第30天)+'</td>';
            
    	tr +='</tr>'
        $("#memberTbody").append(tr);
    	
    	if (i == length_1 -1) {
    		doCalculateTotal(users.length);
    	}
    }

    //开启分页组件
    usersPage(total,pageNum,pageSize);
}

function doCalculateTotal(total) {
	var tr = "<tr><td colspan='3'>汇总</td>";
	
	var totalRates = [];
	for (var i = 0; i < 31; i++) {
		var oneValues = 0, sumValue = 0;
		$(".day" + i).each(function() {
			if ("-" != $(this).text()) {
				oneValues++;
				sumValue = parseFloat(sumValue) + parseFloat($(this).text().replace("1 / ", ""));
			}
		})
		
		tr += "<td>"+ oneValues +"<br/>(" + ((oneValues/total) * 100).toFixed(2)  +"% / " + NumberUtils.toFixedDecimal(sumValue) + ")</td>";
	}
	
	tr += "</tr>";
	$("#memberTbody").append(tr);
}

/**
 * layui的分页插件
 * @param total 总数
 * @param pageNum   当前页
 * @param pageSize  页大小
 */
function usersPage(total,pageNum,pageSize){
    //使用layui的分页插件
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;

        //执行一个laypage实例
        laypage.render({
            elem: 'pageDiv', //注意，这里的 test1 是 ID，不用加 # 号
            count: total, //数据总数，从服务端得到
            limit:pageSize,//每页显示的条数。laypage将会借助 count 和 limit 计算出分页数。
            curr:pageNum,//当前页号
            limits:[50, 40, 30, 20, 10],
            layout:['limit','prev', 'page', 'next','skip','count'],//显示哪些分页组件
            jump: function(obj, first){//点击页号的时候执行的函数
                //obj包含了当前分页的所有参数，比如：
                // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                // console.log(obj.limit); //得到每页显示的条数

                $("[name='pageNum']").val(obj.curr);//向隐藏域设置当前页的值
                $("[name='pageSize']").val(obj.limit);//向隐藏域设置当前页的大小
                if(!first){//首次不执行(点击的时候才执行)
                	queryUsersFY();//执行查询分页函数(这个函数必须写在这里)
                }
            }

        });
    });
}

/**
 * 清空查询条件的按钮
 * 1.清空所有的条件，包括隐藏域的条件，2.重新查询一次
 * @param obj   将清空条件按钮自己传下来
 */
function clearQueryCondition(obj) {
    //1.清空条件
    var form = $(obj).parents("form");
    form.find("input").val("");
    form.find("select").val("");
    //2.重新查询一次
    queryUsersFY();
}



/*****E 分页查询相关方法**********/

function deleteUser(id){
	var layer = layui.layer;
	layer.confirm('确认删除?', function(index){
		$.post("/user/deleteUser.html",{"id":id},function(result){
			if(result.success == true){
				layer.msg("删除成功!");
				queryUsersFY();
			}else{
				layer.msg(result.msg);
			}
		});
	  layer.close(index);
	});
}

function updateUser(id){
	var url = '/user/updateUser.html?id='+id;
	x_admin_show('修改用户',url,600,400);
}

function updateSecondParentName(id, defaultValue, obj) {
	if (defaultValue && "-" == defaultValue) {
		defaultValue = "";
	}
	
	var layer = layui.layer;
	layer.prompt({
		  formType: 2,
		  value: defaultValue,
		  title: '请输入子账号',
		  area: ['200px', '50px'] //自定义文本域宽高
		}, function(value, index, elem){
			layer.close(index);
			$.post('/firstcharge/update.html', {"id": id, "secondParentName": value });
			$(obj).text(replaceNull(value));
		});
}