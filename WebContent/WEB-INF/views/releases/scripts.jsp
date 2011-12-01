<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />

<script type="text/javascript">
	function initDataTable() {
		table = $('#releaseListTable')
				.dataTable(
						{
							"sDom" : '<<"fourcol last right"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oLanguage" : {
								"sInfoEmpty" : "No releases found.",
								"sEmptyTable" : "No releases found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							}
						});
	}
	$(function() {
		crudControls('Release', '${projPath}/releases', 'ids');
		initDataTable();
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
		updateButtonsStatus();
	});

	function updateButtonsStatus() {
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if (len == 0) {
			$("#am-editRelease").button('disable');
			$("#am-deleteReleases").button('disable');
		} else if (len == 1) {
			$("#am-editRelease").button('enable');
			$("#am-deleteReleases").button('option', 'label', 'Remove release');
			$("#am-deleteReleases").button('enable');

		} else {
			$("#am-editRelease").button('disable');
			$("#am-deleteReleases")
					.button('option', 'label', 'Remove releases');
			$("#am-deleteReleases").button('enable');
		}
	}
</script>