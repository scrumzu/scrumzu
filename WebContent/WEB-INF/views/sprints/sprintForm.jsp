<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<c:choose>
	<c:when test="${op == 'add'}">
		<c:set var="opTitle" value="Add" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${projPath}/sprints/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI"
			value="${projPath}/sprints/${sprint.idSprint}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} sprint</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}SprintForm" class="form" commandName="sprint">
	<div class="sixcol">
		<div class="outer-input">
			<form:label path="name">Custom name</form:label>
			<br />
			<div class="inner-input">
				<form:input path="name" id="sprintFormName" class="controls" />
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
	</div>
	<div class="threecol">
		<div class="outer-input">
			<form:label path="dateFrom">Date from</form:label>
			<br /> <span class="ui-icon ui-icon-calendar left"></span>
			<div class="inner-input date">
				<form:input path="dateFrom" id="sprintFormDateFrom"
					class="controls datepicker" />
				<form:errors cssClass="error" path="dateFrom" />
			</div>
		</div>
	</div>
	<div class="threecol last">
		<div class="outer-input">
			<form:label path="dateTo">Date to</form:label>
			<br /> <span class="ui-icon ui-icon-calendar left"></span>
			<div class="inner-input date">
				<form:input path="dateTo" id="sprintFormDateTo"
					class="controls datepicker" />
				<form:errors cssClass="error" path="dateTo" />
			</div>
		</div>
	</div>
	<div class="threecol clear last">
		<label>Status</label> <br />
		<form:select id="sprintStatus" path="sprintStatus" items="${statuses}" />
		<form:errors cssClass="error" path="sprintStatus" />
	</div>
	<form:hidden path="idSprint" />
	<input type="submit" class="controls right clear" value="Save"
		id="submitButton">
</form:form>
