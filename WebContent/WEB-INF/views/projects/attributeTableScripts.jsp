<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		amAddBtn("Attribute", '${projPath}/attributes');
		amDeleteBtn("Attribute", '${projPath}/attributes', 'idsAttribute');
		initDataTable();
	});

	function initDataTable() {
		table = $('#attributeListTable').dataTable({
			"aaSorting" : [ [ 1, "desc" ] ],
			"sDom" : '<t>',
			"bStateSave" : true,
			"oSearch": { "sSearch": "", "bRegex": false, "bSmart": false }
		});
	};
</script>