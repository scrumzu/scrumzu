<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		amAddBtn("Project", '${scrumzuPath}/projects');
		amDeleteBtn("Project", '${scrumzuPath}/projects', 'ids');
		amEditBtn("Project", '${scrumzuPath}/projects', 'ids');
		initProjectsDataTable();
		updateButtonsStatus();
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
		$('input[name=idsAttribute]:checkbox').change(updateButtonsStatus);

		$('#am-deleteProject').click(function() {
			$.ajax({
				type : "DELETE",
				url : '${projPath}',
				data : [],
				success : function(data) {
					window.location = '${scrumzuPath}/projects';
				},
				datatype : "json",
				contentType : 'application/json',
			});
		});
	});

	function updateButtonsStatus() {
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if (len == 0) {
			$("#am-editProject").button('disable');
			$("#am-deleteProjects").button('disable');
		} else if (len == 1) {
			$("#am-editProject").button('enable');
			$("#am-deleteProjects").button('option', 'label', 'Remove project');
			$("#am-deleteProjects").button('enable');

		} else {
			$("#am-editProject").button('disable');
			$("#am-deleteProjects").button('option', 'label', 'Remove projects');
			$("#am-deleteProjects").button('enable');
		}
				
		var lena = $('input[name=idsAttribute]:checkbox:checked').get().length;
        if (lena == 0) {
            $("#am-deleteAttributes").button('disable');
        } else if (lena == 1) {
            $("#am-deleteAttributes").button('option', 'label', 'Remove attribute');
            $("#am-deleteAttributes").button('enable');

        } else {
            $("#am-deleteAttributes").button('option', 'label', 'Remove attributes');
            $("#am-deleteAttributes").button('enable');
        }
		
	}

	function initProjectsDataTable() {
		table = $('#projectListTable')
				.dataTable(
						{
							"sDom" : '<<"fourcol last right"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oLanguage" : {
								"sInfoEmpty" : "No projects found.",
								"sEmptyTable" : "No projects found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							}

						});
	}
</script>