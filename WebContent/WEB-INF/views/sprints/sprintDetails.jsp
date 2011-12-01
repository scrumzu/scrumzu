<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<h1 id="sprintDetailsName">${sprint.name}</h1>
<div class="sixcol last clear">
	<ul>
		<li id="sprintDetailsDateFrom"><b>Date from:</b> ${sprint.dateFrom}</li>
		<li id="sprintDetailsDateTo"><b>Date to:</b> ${sprint.dateTo}</li>
		<li id="sprintDetailsStatus"><b>Status:</b> ${sprint.sprintStatus.value}</li>
	</ul>
</div>
<input type="checkbox" name="ids" value="${sprint.idSprint}"
	checked="checked" class="hidden" />
