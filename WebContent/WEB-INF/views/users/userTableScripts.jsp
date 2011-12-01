<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		initDataTable();
		initSearch();
	});

	function initSearch() {
		columns = {
			"_all" : "All columns"
		};
		$.each(table.fnSettings().aoColumns, function(index, col) {
			if (col.sTitle != '') {
				columns[index] = col.sTitle;
			}
		});
		$("#userListTable_filter").append(
				createSelectInput(columns, "id", "searchByColumnSelect"));

		$("#userListTable_filter input").keyup(function() {
			var c = $("#searchByColumnSelect").val();
			if (c != "_all") {
				table.fnFilter(this.value, c);
			} else {
				table.fnFilter(this.value);
			}
		});

		$("#searchByColumnSelect").change(function() {
			var c = $("#searchByColumnSelect").val();
			var text = $("#userListTable_filter input").val();

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

	function initDataTable() {
		table = $('#userListTable')
				.dataTable(
						{
							"sDom" : '<<"sixcol last right"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oLanguage" : {
								"sInfoEmpty" : "No users found.",
								"sEmptyTable" : "No users found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							}
						});
	}
</script>