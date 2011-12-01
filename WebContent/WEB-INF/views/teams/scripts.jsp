<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<c:choose>
	<c:when test="${not empty chosenProjectAlias }">
		<c:set var="teamPath" value="${projPath}/teams" />
	</c:when>
	<c:otherwise>
		<c:set var="teamPath" value="${scrumzuPath}/teams" />
	</c:otherwise>
</c:choose>


<script type="text/javascript">
	$(function() {
		crudControls("Team", "${teamPath}", 'ids');
		initDataTable();
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
		updateButtonsStatus();
	});

	function updateButtonsStatus() {
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if (len == 0) {
			$("#am-editTeam").button('disable');
			$("#am-deleteTeams").button('disable');
		} else if (len == 1) {
			$("#am-editTeam").button('enable');
			$("#am-deleteTeams").button('option', 'label', 'Remove team');
			$("#am-deleteTeams").button('enable');

		} else {
			$("#am-editTeam").button('disable');
			$("#am-deleteTeams").button('option', 'label', 'Remove teams');
			$("#am-deleteTeams").button('enable');
		}
	}

	function initDataTable() {
		table = $('#teamListTable')
				.dataTable(
						{
							"sDom" : '<<"sixcol last right"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oLanguage" : {
								"sInfoEmpty" : "No teams found.",
								"sEmptyTable" : "No teams found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							}
						});
	}
</script>