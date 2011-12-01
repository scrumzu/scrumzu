<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />
<security:authentication property="principal.username" var="userLogin" />


<security:authorize access="!hasRole('ROLE_ADMIN')">
    <script type="text/javascript">
        function refreshUpdateDeleteButtons(username) {
            if (username == '${userLogin}') {
                $("#filter-update").button("enable").show();
                $("#filter-delete").button("enable").show();
            } else {
                $("#filter-update").button("enable").hide();
                $("#filter-delete").button("enable").hide();
            }
        }
    </script>
</security:authorize>

<security:authorize access="hasRole('ROLE_ADMIN')">
    <script type="text/javascript">
        function refreshUpdateDeleteButtons(username) {
            $("#filter-update").button("enable").show();
            $("#filter-delete").button("enable").show();
        }
    </script>
</security:authorize>

<script type="text/javascript">
	var teams = {};
	var sprints = {};
	var statuses = {};
	var logicOperators = {
		"and" : "and",
		"or" : "or",
	};

	var table = '';

	function initFilterCollections() {
		$.ajax({
			type : "GET",
			url : '${projPath}/pbis/ajax/init_filters',
			data : [],
			beforeSend : function() {
				$("#filter-select").button("disable").removeClass(
						"ui-state-hover");
				showAjaxLoader();
			},
			success : function(data) {
				var collections = eval(data);
				teams = collections.teams;
				sprints = collections.sprints;
				statuses = collections.statuses;
				hideAjaxLoader();
				$("#filter-select").button("enable");
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}

	function getDeleteButton() {
		return $(
				'<button class="filter-deleteColumn controls delete nt">-</button>')
				.button({
					icons : {
						primary : "ui-icon-minus"
					},
					text : false
				}).click(filterDeleteColumns);
	};

	function getAddButton() {
		return $('<button class="filter-addColumn controls add nt">+</button>')
				.button({
					icons : {
						primary : "ui-icon-plus"
					},
					text : false
				}).click(filterAddColumns);
	};

	function filterAddColumns() {
		var row = $("#filterTable tbody tr:last");

		row.find(".filter-addColumn").remove();
		row.find(".filter-deleteColumn").remove();

		var newrow = row.clone();
		newrow.find(".filterLogic").remove();
		newrow.find(".filterValue").remove();
		newrow.find("td:last").append(getDeleteButton());
		newrow.find("td:last").append(getAddButton());
		newrow.find(".filterColumns").change(filterColumnChange);
		newrow.find(".filterColumns").change();

		if ($("#filterTable tbody tr").length > 0) {
			newrow.find("td:first").append(
					createSelectInput(logicOperators, 'class', 'filterLogic'));
		}
		$("#filterTable tbody").append(newrow);
		row.find("td:last").append(getDeleteButton());
	};

	function filterDeleteColumns() {
		$(this).parents('tr').remove();

		if (!$("#filterTable tbody tr:last").find(".filter-addColumn").length) {
			$("#filterTable tbody tr:last td:last").append(getAddButton());
		}

		$("#filterTable tbody tr:first").find(".filterLogic").remove();

		if ($("#filterTable tbody tr").length == 1) {
			$("#filterTable tbody tr:first").find(".filter-deleteColumn")
					.remove();
		}
	};

	function filterColumnChange() {
		var tr = $(this).parents('tr');
		tr.find(".filterValue").remove();

		var columnValue = $(this).val();
		if (columnValue == "team") {
			value = createSelectInput(teams, 'class', 'filterValue');
		} else if (columnValue == "sprint") {
			value = createSelectInput(sprints, 'class', 'filterValue');
		} else if (columnValue == "status") {
			value = createSelectInput(statuses, 'class', 'filterValue');
		} else {
			value = '<input class="filterValue"></input>';
		}

		tr.find("td").eq(3).append(value);

	};

	function openFilterDialog() {
		$("#filterOptions").dialog("open");
	};

	function openColumnsDialog() {
		$("#columnOptions").dialog("open");
	};

	function updateTable(data) {
		table.fnDestroy();
		$("#pbiListTable").remove();
		$("#filterActionMenu").after(data);
		initDataTable();
		updateColumnsVis();
	}

	function getPbis(path, data) {
		$.ajax({
			type : "GET",
			url : path,
			data : [],
			beforeSend : function() {
				showAjaxLoader();
			},
			success : function(data) {
				updateTable(data);
				hideAjaxLoader();
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}
	function getAllPbisForProject() {
		getPbis('${projPath}/pbis/ajax/all', []);
	}

	function getFilteredPbis(filterId) {
		var filter = createFilterObj();
		$.ajax({
			type : "POST",
			url : '${projPath}/pbis/ajax/filters/',
			data : $.toJSON(filter),
			beforeSend : function() {
				showAjaxLoader();
			},
			success : function(data) {
				hideAjaxLoader();
				updateTable(data);
				$('input[name=filterPublic]:checkbox').attr('checked',
						filter.isPublic);
				$('input[name=filterPublic]:checkbox').change();
				$('input[name=filterPublic]:checkbox').button('refresh');
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}

	function loadFilter() {
		var idFilter = $(this).val();
		if (idFilter != 0) {
			$.ajax({
				type : "GET",
				url : '${scrumzuPath}/filters/' + idFilter,
				data : [],
				success : function(data) {
					var filter = eval(data);
					$("#newFilterName").val(filter.name);

					$('input[name=filterPublic]:checkbox').attr('checked',
							filter.isPublic);
					$('input[name=filterPublic]:checkbox').change();
					$('input[name=filterPublic]:checkbox').button('refresh');

					$.each(filter.filterItems,
							function(i, fi) {
								if (!$("#filterTable tbody tr").eq(i).length) {
									filterAddColumns();
								}
								var row = $("#filterTable tbody tr").eq(i);

								row.find("select.filterLogic").val(fi.andOr);
								row.find("select.filterColumns").val(fi.field)
										.change();
								row.find("select.filterOperators").val(
										fi.operator);
								row.find(".filterValue").val(fi.value);
							});

					$("#filterTable tbody").find(
							"tr:gt(" + (filter.filterItems.length - 1) + ")")
							.remove();
					$("#filterTable tbody tr:last td:last").empty();
					$("#filterTable tbody tr:last td:last").append(
							getDeleteButton());
					$("#filterTable tbody tr:last td:last").append(
							getAddButton());
					$("#filterTable tbody tr:first td:last").find(
							".filter-deleteColumn").remove();

					refreshUpdateDeleteButtons(filter.user.username);
				},
				datatype : "json",
				contentType : 'application/json',
			});
		} else {
			resetFilterTable();
		}
	}

	function createFilterObj() {
		var filterName = $("#newFilterName").val();
		var filterItems = new Array();

		var filterTableRows = $("#filterTable tbody tr");
		$.each(filterTableRows, function(i) {
			var fi = {};
			var row = filterTableRows.eq(i);
			fi["andOr"] = row.find("select.filterLogic").val();
			fi["field"] = row.find("select.filterColumns").val();
			fi["operator"] = row.find("select.filterOperators").val();
			fi["value"] = row.find(".filterValue").val();
			filterItems.push(fi);
		});

		var id = $("#filterList").val();

		var filter = {
			"user" : null,
			"idFilter" : id,
			"name" : filterName,
			"filterItems" : filterItems,
			"isPublic" : $("#filterPublic").is(":checked")
		};
		return filter;

	}

	function deleteFilter() {
		var btn = $(this);
		var id = $("#filterList").val();
		$.ajax({
			type : "DELETE",
			url : "${scrumzuPath}/filters/" + id,
			data : [],
			beforeSend : function() {
				btn.button("disable").removeClass("ui-state-hover");
			},
			success : function(data) {
				btn.button("enable");
				btn.blur();
				$('#filterList option[value="' + id + '"]').remove();
				resetFilterTable();
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}

	function saveFilter(filter, buttonId) {
		var btn = $(buttonId);
		$.ajax({
			type : "PUT",
			url : "${scrumzuPath}/filters/",
			data : $.toJSON(filter),
			beforeSend : function() {
				btn.button("disable").removeClass("ui-state-hover");
			},
			success : function(data) {
				$('option[value="' + data + '"]').remove();
				var option = '<option value="' + data +'">' + filter.name
						+ '</option>';
				$("#filterList").append(option);
				$('#filterList').val(data);
				btn.button("enable");
				btn.blur();
				$("#filter-update").button("enable").show();
				$("#filter-delete").button("enable").show();
			},
			datatype : "json",
			contentType : 'application/json',
		});
	}

	function resetFilterTable() {
		$("#filterTable tbody").find("tr:gt(0)").remove();
		$("#filterTable tbody tr:last td:last").empty();
		$("#filterTable tbody tr:last td:last").append(getAddButton());

		$("#filterOptions select").each(function() {
			$(this).find("option:first").attr("selected", "selected");
		});
		$("#filterOptions input:text").each(function() {
			$(this).val("");
		});

		$('input[name=filterPublic]:checkbox').attr('checked', false);
		$('input[name=filterPublic]:checkbox').change();
		$('input[name=filterPublic]:checkbox').button('refresh');

		$("#filter-update").button("disable").removeClass("ui-state-hover")
				.hide();
		$("#filter-delete").button("disable").removeClass("ui-state-hover")
				.hide();
	}

	function clearFilter() {
		resetFilterTable();
		getAllPbisForProject();
	}

	function runFilter() {
		$("#filterOptions").dialog("close");
		getFilteredPbis($("#filterList option:selected").val());
	}

	function showHideColumns() {
		$("#columnOptions").dialog("close");
		updateColumnsVis();

	}

	function updateColumnsVis() {
		$.each($("#pbiColumnsTable tbody tr"), function(index, row) {
			var vis = $(this).find("input[type=checkbox]").is(":checked");
			table.fnSetColumnVis(index, vis);
		});
	}

	function initDataTable() {
		table = $('#pbiListTable')
				.dataTable(
						{
							"sDom" : '<<"fourcol last"f><t><"threecol clear"li><"ninecol last"p>>',
							"bDestroy" : true,
							"sPaginationType" : "links",
							"sScrollX" : "100%",
							"oLanguage" : {
								"sInfoEmpty" : "No PBIs found.",
								"sEmptyTable" : "No PBIs found."
							},
							"oSearch" : {
								"sSearch" : "",
								"bRegex" : false,
								"bSmart" : false
							},
							"bStateSave" : true
						});
		initShowInfo();
		initCheckboxes();
		updateButtonsStatus();
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
		$("#pbiListTable_filter").append(
				createSelectInput(columns, "id", "searchByColumnSelect"));

		$("#pbiListTable_filter input").keyup(function() {
			var c = $("#searchByColumnSelect").val();
			if (c != "_all") {
				table.fnFilter(this.value, c);
			} else {
				table.fnFilter(this.value);
			}
		});

		$("#searchByColumnSelect").change(function() {
			var c = $("#searchByColumnSelect").val();
			var text = $("#pbiListTable_filter input").val();

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

	function putPbis(buttonId, path, dataF, successF) {
		var btn = $(buttonId);
		btn.click(function() {
			var ids = $("input[name=ids]:checkbox:checked").map(function() {
				return $(this).val();
			}).get();

			if (ids.length == 0) {
				alert("Select PBIs first");
			} else {
				$.ajax({
					type : "PUT",
					url : path,
					data : dataF(ids),
					beforeSend : function() {
						btn.button("disable").removeClass("ui-state-hover");
						showAjaxLoader();
					},
					success : function(data) {
						successF(data);
						btn.button("enable");
						btn.blur();
						hideAjaxLoader();
					},
					datatype : "json",
					contentType : 'application/json',
				});
			}
		});

	}

	function amMarkAsDoneBtn(action) {
		putPbis("#am-markAsDonePBIs", '${projPath}/pbis/ajax/markasdone',
				function(ids) {
					return $.toJSON(ids);
				}, function(data) {
					var dataObj = eval(data);
					var status = dataObj.status;
					var ids = dataObj.ids;
					$.each(ids, function() {
						var td = $("#pbi" + this).find("td.pbiStatusCell");
						td.html(status);
					});
				});
	}

	function saveNewFilter() {
		var filter = createFilterObj();
		filter.idFilter = null;
		saveFilter(filter, "filter-save");
	}

	function updateFilter() {
		var filter = createFilterObj();
		saveFilter(filter, "filter-update");
	}

	$(function() {

		initDataTable();
		initFilterCollections();
		amMarkAsDoneBtn("markAsDone");

		$.each(table.fnSettings().aoColumns, function(index, col) {
			var cb = $("#columnOptions table tbody tr").eq(index).find(
					"td:last input:checkbox");
			var vis = table.fnSettings().aoColumns[index].bVisible;
			cb.attr('checked', vis);
			cb.change();
			cb.button('refresh');
		});

		$(".filterColumns").change(filterColumnChange);
		$("#filter-select").click(openFilterDialog);
		$("#filter-clear").click(clearFilter);
		$("#filter-save").click(saveNewFilter);
		$("#filter-update").click(updateFilter).button("disable").hide();
		$("#filter-delete").click(deleteFilter).button("disable").hide();
		$("#filter-run").click(runFilter);
		$("#filterList").change(loadFilter);

		$("#columns-visibility").click(openColumnsDialog);
		$("#columns-ok").click(showHideColumns);

		$(".filter-deleteColumn").click(filterDeleteColumns);
		$(".filter-addColumn").click(filterAddColumns);

		$("#filterOptions").dialog({
			autoOpen : false,
			show : "fade",
			hide : "fade",
			modal : true,
			draggable : true,
			width : "auto",
			height : "auto",
			title : "Filter PBIs"
		});

		$("#columnOptions").dialog({
			autoOpen : false,
			show : "fade",
			hide : "fade",
			modal : true,
			draggable : true,
			width : "auto",
			height : "auto",
			title : "Change columns visibility"
		});
	});
</script>
