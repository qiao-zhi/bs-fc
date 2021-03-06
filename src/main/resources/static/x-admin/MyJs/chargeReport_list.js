$(function(){
	queryUsersFY();
});

/*****S 分页查询相关方法**********/
function queryUsersFY(){
    $.ajax({
       url:"/firstcharge/pageFirstChargeReport.html",
       data:$("#queryUserForm").serialize(),
       dataType:'json',
        type:'post',
        async:true,
        success:showUsersTable
    });
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
            +'<td class="day0">'+replaceNull(users[i].当天)+'</td>'
            +'<td class="day1">'+replaceNull(users[i].第1天)+'</td>'
            +'<td class="day2">'+replaceNull(users[i].第2天)+'</td>'
            +'<td class="day3">'+replaceNull(users[i].第3天)+'</td>'
            +'<td class="day4">'+replaceNull(users[i].第4天)+'</td>'
            +'<td class="day5">'+replaceNull(users[i].第5天)+'</td>'
            +'<td class="day6">'+replaceNull(users[i].第6天)+'</td>'
            +'<td class="day7">'+replaceNull(users[i].第7天)+'</td>'
            +'<td class="day8">'+replaceNull(users[i].第8天)+'</td>'
            +'<td class="day9">'+replaceNull(users[i].第9天)+'</td>'
            +'<td class="day10">'+replaceNull(users[i].第10天)+'</td>'
            +'<td class="day11">'+replaceNull(users[i].第11天)+'</td>'
            +'<td class="day12">'+replaceNull(users[i].第12天)+'</td>'
            +'<td class="day13">'+replaceNull(users[i].第13天)+'</td>'
            +'<td class="day14">'+replaceNull(users[i].第14天)+'</td>'
            +'<td class="day15">'+replaceNull(users[i].第15天)+'</td>';
            
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
	var tr = "<tr><td colspan='2'>汇总</td>";
	
	var totalRates = [];
	for (var i = 0; i < 16; i++) {
		var oneValues = 0;
		$(".day" + i).each(function() {
			if ("1" == $(this).text()) {
				oneValues++;
			}
		})
		
		tr += "<td>"+ oneValues +"<br/>(" + ((oneValues/total) * 100).toFixed(2)  +"%)</td>";
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
            limits:[100, 50, 40, 30, 20, 10],
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