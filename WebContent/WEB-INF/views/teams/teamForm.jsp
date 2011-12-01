<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:choose>
	<c:when test="${not empty chosenProjectAlias}">
		<c:set var="actionPath"
			value="${pageContext.request.contextPath}/${chosenProjectAlias}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="actionPath" value="${pageContext.request.contextPath}"></c:set>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${op == 'add'}">
		<c:set var="opTitle" value="Add" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${actionPath}/teams/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${actionPath}/teams/${team.idTeam}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} team</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}TeamForm" class="form" commandName="team">
	<h2>Team details</h2>
	<div class="ninecol">
		<div class="outer-input">
			<div class="inner-input">
				<form:label path="name" id="teamName">Name</form:label>
				<br />
				<form:textarea path="name" rows="1" cols="100" id="teamFormName" />
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
	</div>
	<div class="threecol last">
		<div class="outer-input">
			<div class="inner-input">
				<form:label path="alias" id="teamAlias">Key</form:label>
				<br />
				<form:input path="alias" />
				<form:errors cssClass="error" path="alias" />
			</div>
		</div>
	</div>
	<div class="twelvecol last">
		<div class="outer-input">
			<div class="inner-input">
				<form:label path="description">Description</form:label>
				<br />
				<form:textarea path="description" rows="4" cols="100"
					id="teamFormDescription" />
			</div>
		</div>
	</div>
	<div class="twelvecol clear spacerT">
		<h2>Management</h2>
		<div class="threecol">
			<form:label path="project" id="teamSelectProject">Project</form:label>
			<br />
			<form:select path="project" id="teamFormProject">
				<form:option value="">-</form:option>
				<form:options items="${projects}" itemLabel="name" />
			</form:select>
		</div>
		<div class="threecol last">
			<form:label path="user" id="teamFormScrumMasterSelect">Scrum Master</form:label>
			<br />
			<form:select path="user" id="teamFormScrumMaster">
				<form:option value="">-</form:option>
				<form:options items="${users}" itemLabel="username"
					itemValue="idUser" />
			</form:select>
		</div>
	</div>
	<form:hidden path="idTeam" />
	<input type="submit" class="controls right clear" value="Save"
		id="submitButton">
</form:form>
