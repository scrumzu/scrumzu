<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />

<script type="text/javascript">
	$(function() {
		initDataTable();
		initSearch();
	});
	
	function initDataTable() {
		table = $('#sprintListTable')
				.dataTable(
						{
							"sDom" : '<<"fourcol last right"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oLanguage" : {
								"sInfoEmpty" : "No sprints found.",
								"sEmptyTable" : "No sprints found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							}
						});
	}

	function initSearch() {
		columns = {
			"_all" : "All columns"
		};
		$.each(table.fnSettings().aoColumns, function(index, col) {
			if (col.sTitle != '') {
				columns[index] = col.sTitle;
			}
		});
		$("#sprintListTable_filter").append(
				createSelectInput(columns, "id", "searchByColumnSelect"));

		$("#sprintListTable_filter input").keyup(function() {
			var c = $("#searchByColumnSelect").val();
			if (c != "_all") {
				table.fnFilter(this.value, c);
			} else {
				table.fnFilter(this.value);
			}
		});

		$("#searchByColumnSelect").change(function() {
			var c = $("#searchByColumnSelect").val();
			var text = $("#sprintListTable_filter input").val();

			$.each(table.fnSettings().aoColumns, function(index, col) {
				if (index != c) {
					table.fnFilter("", index);
				}
			});
			if (c != "_all") {
				table.fnFilter(text, c);
			} else {
				table.fnFilter(text);
			}
		});
	}
</script>