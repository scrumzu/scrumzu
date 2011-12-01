$(document).ready(function() {

	initBasicButtons();
	initClearForm();
	initDatePicker();
	initCheckboxes();
	initFilterButtons();
	initSubmenuInfo();

});

// SUBMENU INFO TOGGLE
function initSubmenuInfo() {
	$("#submenuInfoToggle").click(
			function() {
				var text = $(this).text() == 'Show details' ? 'Hide details' : 'Show details';
				$(this).text(text);
				$("#submenuInfo").slideToggle('slow');
			});
}

// CONTROLS INITIALIZATION
function initFilterButtons() {
	$(".controls.filter").button({
		icons : {
			primary : "ui-icon-filter"
		}
	});

	$(".controls.options").button({
		icons : {
			primary : "ui-icon-gear"
		}
	});

	$(".controls.clearFilter").button({
		icons : {
			primary : "ui-icon-clear-filter"
		}
	});
}

function initBasicButtons() {

	$('.controls:submit').button();
	$('.controls.ui-button').click(function() {
		$(this).blur();
	});

	// CRUD controls
	$('.controls.add').button({
		icons : {
			primary : "ui-icon-plus"
		}
	});

	$(".controls.delete").button({
		icons : {
			primary : "ui-icon-trash"
		}
	});

	$(".controls.delete").button({
		icons : {
			primary : "ui-icon-trash"
		}
	});

	$(".controls.update").button({
		icons : {
			primary : "ui-icon-arrowrefresh-1-s"
		}
	});

	$(".controls.edit").button({
		icons : {
			primary : "ui-icon-pencil"
		}
	});

	$(".controls.save").button({
		icons : {
			primary : "ui-icon-disk"
		}
	});

	$(".controls.savenew").button({
		icons : {
			primary : "ui-icon-disk-new"
		}
	});

	$(".controls.start").button({
		icons : {
			primary : "ui-icon-play"
		}
	});
	$(".controls.done").button({
		icons : {
			primary : "ui-icon-circle-check"
		}
	});
	$(".controls.stop").button({
		icons : {
			primary : "ui-icon-stop"
		}
	});

	$(".controls.nt").button({
		text : false
	});
}

function initClearForm() {
	$('.controls.clearForm').click(
			function() {
				$(':input', '.form').not(':button, :submit, :reset, :hidden')
						.val('').removeAttr('checked').removeAttr('selected');
			});
}

function initDatePicker() {
	$("input.controls.datepicker").datepicker({
		dateFormat : 'yy-mm-dd'
	});
}

function initShowInfo() {
	// show info
	$(".controls.showInfo").click(function() {
		$(this).toggleClass('ui-icon-squaresmall-minus');
		$(this).parents('td').find('p').toggleClass('hidden');
	});
}

function initCheckboxes() {
	// checkboxes and checkall
	$('.controls:checkbox').button({
		icons : {
			secondary : "ui-icon-checkbox-off"
		},
		text : false
	});

	$(".controls.chkBox.ui-button, .controls.selectAll.ui-button").unbind(
			'click.button');

	$('.controls:checkbox').change(
			function() {
				if ($(this).is(':checked')) {
					$(this).button("option", {
						icons : {
							secondary : "ui-icon-check"
						}
					});
				} else {
					$(this).button("option", {
						icons : {
							secondary : "ui-icon-checkbox-off"
						}
					});
					$(this).button("widget").removeClass(
							'ui-state-focus ui-state-hover');
					$(this).button("refresh");
				}
			});

	$('.controls.selectAll').click(function() {
		$('input[name=ids]:checkbox').attr('checked', this.checked);
		$('input[name=ids]:checkbox').change();
		$('input[name=ids]:checkbox').button('refresh');
	});

	$('.controls.selectAll:checkbox').button({
		text : true
	});

	$('.selectAll').removeClass('ui-state-focus ui-state-active');
	$('.selectAll').attr('checked', false);
	$('.selectAll, .controls:checkbox').change();
	$('.selectAll, .controls:checkbox').button('refresh');
}
function crudControls(objName, path, chkName) {
	amAddBtn(objName, path);
	amEditBtn(objName, path, chkName);
	amDeleteBtn(objName, path, chkName);
	amDeleteSingleBtn(objName, path, chkName);
}

function amAddBtn(objName, path) {
	$('#am-add' + objName).click(function() {
		window.location = path + '/new';
	});
}

function amEditBtn(objName, path, chkName) {
	$('#am-edit' + objName).click(
			function() {
				var ids = $("input[name=" + chkName + "]:checkbox:checked")
						.map(function() {
							return $(this).val();
						}).get();
				if (ids.length > 1) {
					alert("Select one " + objName + " only");
				} else if (ids.length == 0) {
					alert("Select " + objName + " first");
				} else {
					window.location = path + "/" + ids[0] + "/edit";
				}
			});
};

function amDeleteBtn(objName, path, chkName) {
	var btn = $('#am-delete' + objName + 's');
	btn.click(function() {
		var ids = $("input[name=" + chkName + "]:checkbox:checked").map(
				function() {
					return $(this).val();
				}).get();

		if (ids.length == 0) {
			alert("Select " + objName + " first");
		} else {

			$.ajax({
				type : "DELETE",
				url : path,
				data : $.toJSON(ids),
				beforeSend : function() {
					btn.button("disable").removeClass("ui-state-hover");
					showAjaxLoader();
				},
				success : function(data) {
					$.each(ids, function(index, value) {
						table
								.fnDeleteRow(table.fnGetPosition(document
										.getElementById(objName.toLowerCase()
												+ value)));
						if (objName.toLowerCase() == 'project'
								|| objName.toLowerCase() == 'team') {
							$("#mm-" + objName.toLowerCase() + value).remove();
						}
					});
					btn.button("enable");
					btn.blur();
					updateButtonsStatus();
					hideAjaxLoader();
				},
				datatype : "json",
				contentType : 'application/json',
			});
		}
		;
	});
}

function amDeleteSingleBtn(objName, path, chkName) {
	var btn = $('#am-delete' + objName);
	btn.click(function() {
		var ids = $("input[name=" + chkName + "]:checkbox:checked").map(
				function() {
					return $(this).val();
				}).get();
		$.ajax({
			type : "DELETE",
			url : path + '/' + ids[0],
			data : $.toJSON(ids),
			success : function(data) {
				window.location = path;
			},
			datatype : "json",
			contentType : 'application/json',
		});
	});
}

function showAjaxLoader() {
	hideAjaxLoader(); // one instance only
	$("#content h1")
			.append(
					' <img id="ajaxLoader" src="/scrumzu/resources/img/spinner.gif" alt="Processing..." />');
}

function hideAjaxLoader() {
	$("#ajaxLoader").remove();
}

function createSelectInput(dict, param, pvalue) {
	var s = '<select ' + param + '="' + pvalue + '">';
	$.each(dict, function(key, value) {
		s = s + '<option value="' + key + '">' + value + '</option>';
	});
	return s;
};
