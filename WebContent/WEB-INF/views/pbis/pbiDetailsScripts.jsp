<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		initDataTable();
	});
	function initDataTable() {
		table = $('#pbiDetailsHistoryTable')
				.dataTable(
						{
							"sDom" : '<<"sixcol right last"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							},
							"aaSorting": [[ 0, "desc" ]]
						});
		initSearch();
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
		$("#pbiDetailsHistoryTable_filter").append(
				createSelectInput(columns, "id", "searchByColumnSelect"));

		$("#pbiDetailsHistoryTable_filter input").keyup(function() {
			var c = $("#searchByColumnSelect").val();
			if (c != "_all") {
				table.fnFilter(this.value, c);
			} else {
				table.fnFilter(this.value);
			}
		});

		$("#searchByColumnSelect").change(function() {
			var c = $("#searchByColumnSelect").val();
			var text = $("#pbiDetailsHistoryTable_filter input").val();

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