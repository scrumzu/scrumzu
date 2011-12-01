<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<script type="text/javascript">
	$(function() {
		crudControls('PBI', '${projPath}/pbis', 'ids');
		updateButtonsStatus();
		$('input[name=ids]:checkbox').change(updateButtonsStatus);
	});
	
	
	function updateButtonsStatus(){
		var len = $('input[name=ids]:checkbox:checked').get().length;
		if(len == 0){
			$("#am-markAsDonePBIs").button('disable');
			$("#am-editPBI").button('disable');
			$("#am-deletePBIs").button('disable');
		}
		else if(len==1){
			$("#am-markAsDonePBIs").button('enable');
			$("#am-markAsDonePBIs").button('option', 'label',  'Mark PBI as done');
			$("#am-editPBI").button('enable');
			$("#am-deletePBIs").button('option', 'label',  'Remove PBI');
			$("#am-deletePBIs").button('enable');
			
		}
		else{
			$("#am-markAsDonePBIs").button('enable');
			$("#am-markAsDonePBIs").button('option', 'label',  'Mark PBIs as done');
            $("#am-editPBI").button('disable');
            $("#am-deletePBIs").button('option', 'label',  'Remove PBIs');
            $("#am-deletePBIs").button('enable');
		}
	}
	
</script>

