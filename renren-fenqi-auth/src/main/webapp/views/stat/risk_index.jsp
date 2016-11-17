<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link href="${ctx }/static/css/datatable/jquery.dataTables.css" rel="stylesheet">
<%-- <link href="${ctx}/static/css/bootstrap.css" rel="stylesheet"> --%>
<link href="${ctx }/static/css/daterangepicker/daterangepicker.css" rel="stylesheet">
<style>
.m-charts{
	width: 400px;
	height:300px;
}
.l-charts{
	width: 600px;
	height:400px;
}
</style>
<c:set var="searchUrl" value="/role/loadRoleList"/>
<%-- <script src="${ctx }/static/js/jquery-1.10.2.js"></script> --%>
<script src="${ctx }/static/js/datatable/jquery.dataTables.js"></script>
<script src="${ctx }/static/js/datatable/dataTables.bootstrap.js"></script>
<!-- 引入 ECharts 文件 -->
<script src="${ctx }/static/js/echart/echarts.js"></script>
<script src="${ctx }/static/js/daterangepicker/moment.js"></script>
<script src="${ctx }/static/js/daterangepicker/daterangepicker.js"></script>
<script type="text/javascript">
    $(function () {
        var searchUrl = "${searchUrl}";
//         alert(moment().subtract(1, 'week').format('YYYY-MM-DD'));
        $('#date').daterangepicker({
        	"ranges": {
              	  "今天": [
                           moment().startOf('day'),
                           moment().endOf('day')
                         ],
                    "最近一周": [
                           
							moment().subtract(1, 'week').format('YYYY-MM-DD'),
							moment().endOf('day')
                    ],
                    "最近一个月": [
                        moment().subtract(1, 'month').format(),
                        moment().endOf('day')
                    ]
             },
            "locale": {
                "format": "YYYY-MM-DD",
            },
//             "startDate":moment().subtract(1, 'week').format('YYYY-MM-DD')
            "startDate":moment().startOf('day')
        }, function(start, end, label) {
        	initPage(start.format('YYYY-MM-DD'),end.format('YYYY-MM-DD'));
        });
		refresh();
    });
	function initPage(startDate,endDate){
// 		alert(startDate);
        init_carb('data_rc_table',startDate,endDate,'10006','node_commerce_audit',true);
        init_carb('data_final_table',startDate,endDate,'10006','node_final_data_audit',false);
        init_carb('data_swap_table',startDate,endDate,'10005','node_car_audit',true);
        init_carb('data_credit_v1',startDate,endDate,'10004','node_risk_control_audit',false);
        init_carb('data_credit_v2',startDate,endDate,'10011','node_risk_control_audit',false);
        init_carb('data_credit_adjust',startDate,endDate,'10007','node_risk_control_audit',false);
//         init_carb_final(startDate,endDate);
//         init_carb_swap(startDate,endDate);
//         init_carb_credit_v1(startDate,endDate);
		
	}
	
	function refresh(){
		var dateStr = $("#date").val().split(" 至 ");
		var startDate =  dateStr[0];
		var endDate =  dateStr[1];
		initPage(startDate,endDate);
	}
    /**
     * 检查是否为空
     * @param data 传入值
     * @returns {Boolean} 如果为空，undefined或者null则返回false；否则返回true
     */
    function checkNull(data) {
        if (null == data || undefined == data || $.trim(data).length == 0) {
            return true;
        } else {
            return false;
        }
    }
    function init_carb(tableId,startTime,endTime,processId,taskKey,carNumVisible) {
    	var table = $('#'+tableId);
    	var	charts_div = table.closest(".panel-body").find(".charts");
    	var	charts = table.closest(".panel-body").find(".draw-charts");
    	table.DataTable({
        	destroy: true,
            paging: true,
            searching: false,
            info: true,
            language: {
                url: "/static/resource/datatable/cn.json"
            },
            createdRow: function( row, data, dataIndex ) {
                 
             },
            ajax: {
                url: "/stat/risk/carb",
                data:{startTime:startTime,endTime:endTime,processId:processId,taskKey:taskKey},
                type: "GET",
                dataSrc: function (json) {
                    if (json.suc == true) {
                    	var initData = initSeriesData(json.data,10);
//                     	alert(processId=='10006' || processId = '10005');
                    	if(tableId=='data_rc_table' || tableId == 'data_swap_table'){
	                    	initBingTu(charts[0],initData,"operatorName","count","审核订单总数分析","审核总量占比");
	                    	initZhuZhuang(charts[1],initData,"operatorName","avgDuration","单次处理平均时间柱状图","单位：分钟");
	                    	initBingTu(charts[2],initData,"operatorName","carNum","车辆总数分析","车辆总数占比");
	                    	initZhuZhuang(charts[3],initData,"operatorName","totalDuration","节点总耗时平均值柱状图","单位：分钟");
                    	}else{
	                    	initBingTu(charts[0],initData,"operatorName","count","审核订单总数分析","审核总量占比");
	                    	initZhuZhuang(charts[1],initData,"operatorName","avgDuration","单次处理平均时间柱状图","单位：分钟");
	                    	initZhuZhuang(charts[2],initData,"operatorName","totalDuration","节点总耗时平均值柱状图","单位：分钟");
                    	}
						if(initData.length<=0){
							charts_div.hide();
						}else{
							charts_div.show();
						}
	                    return json.data;
                    }else{
                    	errorShow(json.msg);
                    }
                    return [];
                }
            },
            columns: [
                {data: 'operator', title: '操作者ID'},
                {data: 'operatorName', title: '操作者姓名'},
                {data: 'count', title: '审核总数'},
                {data: 'passCount', title: '通过总数'},
                {data: 'rejectCount', title: '拒绝总数'},
                {data: 'patchNum', title: '补件次数'},
                {data: 'carNum', title: '车辆总数', visible:carNumVisible},
                {data: 'totalDuration', title: '节点总耗时平均值(分钟)'},
                {data: 'avgDuration', title: '单次处理平均时间(分钟)',},
                {data: 'auditDuration', title: '最后一次审核平均时间(分钟)'}
            ]
        });
    }
	
    //初始化数据，data：后台数据，limit_num：图表展示数量限制
    function initSeriesData(data,limit_num){
//     	var limit_num = 8;//最多展示八块饼图
    	var seriesData = [];//数据
    	for(var i=0;i<data.length;i++){
    		seriesData.push({
    			count: data[i].count,
    			passCount: data[i].passCount,
    			rejectCount: data[i].rejectCount,
    			carNum: data[i].carNum,
    			patchNum: data[i].patchNum,
    			avgDuration:data[i].avgDuration,
    			auditDuration:data[i].auditDuration,
    			duration:data[i].duration,
    			totalDuration:data[i].totalDuration,
    			operatorName: data[i].operatorName
   			});
        }
    	seriesData.sort(function(x,y){
            return x.count-y.count
        });
    	var avgDuration = 0;
    	var auditDuration = 0;
    	var totalDuration = 0;
    	var duration = 0;
    	var sum = 0;
    	if(seriesData.length>limit_num){
    		var seriesDataUn = [];//合并后的数据
    		var countUn = 0; 
    		var passCountUn = 0; 
    		var rejectCountUn = 0; 
    		var carNumUn = 0; 
    		var patchNumUn = 0; 
    		for(var i=0;i<seriesData.length;i++){
    			if(i<(seriesData.length-limit_num)){
    				countUn += seriesData[i].count;
    				passCountUn += seriesData[i].passCount;
    				rejectCountUn += seriesData[i].rejectCount;
    				carNumUn += seriesData[i].carNum;
    				patchNumUn += seriesData[i].patchNum;
    				avgDuration += seriesData[i].avgDuration;
    				auditDuration += seriesData[i].auditDuration;
    				sum += 1;
//     				alert(seriesData[i].operatorName);
    				seriesDataUn[0] = {
    						count: countUn,
    						passCount: passCountUn,
    						rejectCount: rejectCountUn,
    						carNum:carNumUn,
    						patchNum:patchNumUn,
    						avgDuration:(avgDuration/sum).toFixed(0),
    						auditDuration:(auditDuration/sum).toFixed(0),
    						duration:(duration/sum).toFixed(0),
    						totalDuration:(totalDuration/sum).toFixed(0),
    						operatorName: '其他'
   						}
    			}else{
	    			seriesDataUn.push({
	    						count: seriesData[i].count,
	    						passCount: seriesData[i].passCount,
	    						rejectCount: seriesData[i].rejectCount,
	    						carNum: seriesData[i].carNum,
	    						patchNum: seriesData[i].patchNum,
	    						avgDuration:seriesData[i].avgDuration,
	    						auditDuration:seriesData[i].auditDuration,
	    						duration:seriesData[i].duration,
	    						totalDuration:seriesData[i].totalDuration,
	    						operatorName: seriesData[i].operatorName
    						});
    			}
            }
    		return seriesDataUn;
    	}else{
    		return seriesData;
    	}
    }
    function initBingTu(dom,initData,nameKey,valueKey,title_text,title_subtext) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(dom);
        //个性化处理
        var legend = [];//标题
    	var seriesData = [];//数据
    	for(var i=0;i<initData.length;i++){
    		legend.push(initData[i].operatorName);
        }
    	for(var i=0;i<initData.length;i++){
// 	        alert(JSON.stringify(initData[i]));
    		seriesData.push({'name':initData[i][nameKey],'value':initData[i][valueKey]});
        }
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: title_text,
                subtext: title_subtext,
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: '',
                data: []
            },
            series: [
                {
                    name: '审核占比',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: seriesData,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    function initZhuZhuang(dom,initData,xKey,yKey,title_text,title_subtext) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(dom);
      	//个性化处理
        var x = [];//标题
    	var y = [];//数据
//    		alert(JSON.stringify(initData));
    	for(var i=0;i<initData.length;i++){
    		x.push(initData[i][xKey]);
    		y.push(initData[i][yKey]);
        }
        // 指定图表的配置项和数据
        //app.title = '坐标轴刻度与标签对齐';

        option = {
            title: {
                text: title_text,
                subtext: title_subtext,
                x: 'center'
            },
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
//                     data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    data: x,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '',
                    type: 'bar',
                    barWidth: '30%',
//                     data: [10, 52, 200, 334, 390, 330, 220]
                    data: y
                }
            ]
        };


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    
    function errorShow(text){
		$("#error_div").modal('show');
		$("#error_msg_model").html(text);
	}
</script>
<div>
    <div class="row">
        <ol class="breadcrumb">
            <li><a href="javascipt:;">风控数据管理</a></li>
            <li class="active">风控数据</li>
        </ol>
		<div class="col-md-4  " style="position: relative;margin-bottom:10px;">
			<div>
	            <h4>选择时间范围</h4>
	            <input type="text" id="date" readOnly='true' class="form-control">
	            <i class="fa icon-calendar" style="position: absolute;right: 20px;bottom: 10px;"></i>
			</div>
         </div>
         <div class="padding-bootom fl" style="position: relative;margin-top:39px">
              <button  class="btn btn-primary" id="refresh" onclick="javascript:refresh();" >刷新</button>
          </div>
<!--             <input type="button" id="refresh" value='刷新' class="btn sm-btn"> -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            贷款信息审核数据分析
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_rc_table" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <!-- /.table-responsive -->
<!--                             <ul class="nav nav-list"> -->
<!--                                 <li class="divider"></li> -->
<!--                             </ul> -->
                            <hr>
                            <div class='charts'>
	                            <div id="" style="" class="draw-charts l-charts col-lg-6 "></div>
	                            <div id="" style="" class="draw-charts l-charts col-lg-6"></div>
	                            <div id="" style="" class="draw-charts l-charts col-lg-6"></div>
	                            <div id="" style="" class="draw-charts l-charts col-lg-6"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            车辆置换数据分析
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_swap_table" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <hr>
                            <div class='charts'>
	                            <div id="" style=";" class="draw-charts l-charts col-lg-6 "></div>
	                            <div id="" style=";" class="draw-charts l-charts col-lg-6"></div>
	                            <div id="" style=";" class="draw-charts l-charts col-lg-6"></div>
	                            <div id="" style=";" class="draw-charts l-charts col-lg-6"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            贷款最终审核数据分析
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_final_table" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <hr>
                            <div class='charts'>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4 "></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                           车商授信（老）
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_credit_v1" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <hr>
                            <div class='charts'>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4 "></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            车商授信（新）
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_credit_v2" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <hr>
                            <div class='charts'>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4 "></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            车商提额
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table id="data_credit_adjust" class="table table-striped table-bordered table-hover">
                                </table>
                            </div>
                            <hr>
                            <div class='charts'>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4 "></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
	                            <div id="" style=";" class="draw-charts m-charts col-lg-4"></div>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
    </div>
    <div class="panel-body">
		<div class="modal fade bs-example-modal-sm" id="error_div" tabindex="-1" role="dialog"
			aria-labelledby="errorModalLabel" aria-hidden="true" style="padding-top: 150px;overflow-y: hidden">
			<div class="modal-dialog" >
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="errorModalLabel"><center>错误信息</center></h4>
					</div>
					<div class="modal-body">
						<h3 style="color:red" id="error_msg_model"></h3>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							关闭</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
	</div>
</div>
