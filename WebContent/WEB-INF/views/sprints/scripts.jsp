<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />

<script type="text/javascript">
	function amStartEndBtn(action, path) {
		var btn = $('#am-' + action + 'Sprint');
		btn.click(function() {
			var ids = $("input[name=ids]:checkbox:checked").map(function() {
				return $(this).val();
			}).get();
			if (ids.length > 1) {
				alert("Choose one sprint only");
			} else if (ids.length == 0) {
				alert("Choose sprint first");
			} else {
				$.ajax({
					type : "PUT",
					url : '${projPath}/sprints/' + ids[0],
					data : action,
					beforeSend : function() {
						btn.button("disable").removeClass("ui-state-hover");
						showAjaxLoader();
					},
					complete : function() {
						btn.button("enable");
						btn.blur();
						hideAjaxLoader();
					},
					success : function(data) {
						var td = $("#sprint" + data[0]).find("td").eq(5);
						td.html(data[1]);
					},
					datatype : "json",
					contentType : 'application/json',
				});
			}
		});
	};

	function updateButtonsStatus() {
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if (len == 0) {
			$("#am-editSprint").button('disable');
			$("#am-deleteSprints").button('disable');
			$("#am-startSprint").button('disable');
			$("#am-endSprint").button('disable');
		} else if (len == 1) {
			$("#am-editSprint").button('enable');
			$("#am-deleteSprints").button('option', 'label', 'Remove sprint');
			$("#am-deleteSprints").button('enable');
	        $("#am-startSprint").button('enable');
	        $("#am-endSprint").button('enable');

		} else {
			$("#am-editSprint").button('disable');
			$("#am-deleteSprints").button('option', 'label', 'Remove sprints');
			$("#am-deleteSprints").button('enable');
	        $("#am-startSprint").button('disable');
	        $("#am-endSprint").button('disable');
		}
	}

	$(function() {
		crudControls('Sprint', '${projPath}/sprints', 'ids');
		amStartEndBtn('end', '${projPath}/sprints');
		amStartEndBtn('start', '${projPath}/sprints');
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
		updateButtonsStatus();
	});
</script>