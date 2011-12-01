<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		crudControls("User", "${scrumzuPath}/users", 'ids');
		$("#changePassword").change(function() {
			$("#userFormNewPassword").toggle();
		});

		$("#userFormNewPassword").hide();
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
		updateButtonsStatus();
	});

	function updateButtonsStatus() {
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if (len == 0) {
			$("#am-editUser").button('disable');
			$("#am-deleteUsers").button('disable');
		} else if (len == 1) {
			$("#am-editUser").button('enable');
			$("#am-deleteUsers").button('option', 'label', 'Remove user');
			$("#am-deleteUsers").button('enable');

		} else {
			$("#am-editUser").button('disable');
			$("#am-deleteUsers").button('option', 'label', 'Remove users');
			$("#am-deleteUsers").button('enable');
		}
	}
</script>