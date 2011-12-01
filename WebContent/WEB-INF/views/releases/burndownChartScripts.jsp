<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
    value="${pageContext.request.contextPath}/${chosenProjectAlias}" />

<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="/scrumzu/resources/js/excanvas.min.js"></script><![endif]-->
<script type="text/javascript"
	src="/scrumzu/resources/js/jquery.jqplot.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jqplot.highlighter.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jqplot.canvasAxisLabelRenderer.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="/scrumzu/resources/css/jquery.jqplot.css" />

<script type="text/javascript">
	$(function() {
		getChartData();
		
	});

	function getChartData() {
		$.ajax({
			type : "GET",
			url : '${projPath}/releases/ajax/burndown/${release.idRelease}',
			data : [],
			beforeSend : function() {
				showAjaxLoader();
			},
			success : function(responseData) {
				hideAjaxLoader();
				
// 				console.log(responseData);
				var element_count = 0;
				for (e in responseData) { element_count++; }

				if(element_count> 0){
					var i = 1;
					var data = [];
					var ta = [];
					$.each(responseData, function(sprint, storyPoints) {
						if(storyPoints!=null)
							data.push(storyPoints);
						ta.push([i, ""+sprint]);
						i++;
					});
					
	                var start = [ 1, data[0]];
	                var end = [ ta.length, 0];
	                var def = [ start, end];
	                console.log(def);
	                
					$.jqplot('chart', [ data, def ], {
						seriesDefaults : {
							markerOptions : {
								shadow : false
							},
							shadow : false,
						},
						markerOptions : {
							shadow : false,
						},
						grid : {
							shadow : false,
							drawBorder : false,
							background : "#ffffff",
						},
						series : [ {
							lineWidth : 2,
							color : '#2e73f2',
							markerOptions : {
								size : 8
							}
						}, {
							lineWidth : 1,
							color : '#00bed0',
							markerOptions : {
								show : false
							}
						}, ],
	
						axes : {
							xaxis : {
								ticks : ta,
								label : "Sprints",
								pad : 0,
								labelRenderer : $.jqplot.CanvasAxisLabelRenderer,
								labelOptions : {
									fontFamily : 'Helvetica, Arial, sans-serif',
									fontSize : '14px'
								},
							},
							yaxis : {
								min : 0,
								tickOptions : {
									formatString : "%.0f"
								},
								labelRenderer : $.jqplot.CanvasAxisLabelRenderer,
								labelOptions : {
									fontFamily : 'Helvetica, Arial, sans-serif',
									fontSize : '14px'
								},
								label : "Story points remaining",
							}
						},
						highlighter : {
							show : true,
							sizeAdjust : 10,
							useAxesFormatters : true,
							tooltipContentEditor : function(a, b, c, d) {
								var xy = a.split(", ");
								var index = xy[0] - 1;
								var tmp = ta[index][1].split("<br/>");
								var sprint = tmp[0].split("<div style=\"text-align: center;\">")[1];
								var date = tmp[1];
// 								console.log("Sprint: " + sprint +"</br>Date: " + date +"</br>Value: " + xy[1]);
								return  "Sprint: " + sprint +"</br>Date: " + date +"</br>Remaining story points: " + xy[1];
							}
						},
					});
				}
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}
</script>