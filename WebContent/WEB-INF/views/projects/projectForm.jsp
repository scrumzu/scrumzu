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
		<c:set var="actionURI" value="${scrumzuPath}/projects/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${projPath}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} project</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}ProjectForm" class="form" commandName="project">
	<div class="ninecol">
		<div class="outer-input">
			<form:label path="name" id="projectName">Name</form:label>
			<br />
			<div class="inner-input">
				<form:textarea path="name" rows="1" cols="100" id="projectFormName" />
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
	</div>
	<div class="threecol last">
		<div class="outer-input">
			<form:label path="alias" id="projectAlias">Key</form:label>
			<br />
			<div class="inner-input">
				<form:input path="alias" />
				<form:errors cssClass="error" path="alias" />
			</div>
		</div>
	</div>
	<div class="twelvecol last">
		<div class="outer-input">
			<form:label path="description">Description</form:label>
			<br />
			<div class="inner-input">
				<form:textarea path="description" rows="4" cols="100"
					id="projectFormDescription" />
			</div>
		</div>
	</div>
	<div class="fourcol last clear">
		<div class="outer-input">
			<form:label path="url" id="projectUrl">Website</form:label>
			<br />
			<div class="inner-input">
				<form:input path="url" />
			</div>
		</div>
	</div>
	<div class="threecol last clear">
		<div class="outer-input">
			<form:label path="owner" id="projectOwner">Product Owner</form:label>
			<br />
			<div class="inner-input">
				<form:input path="owner" />
				<form:errors cssClass="error" path="owner" />
			</div>
		</div>
	</div>
	<div class="threecol last clear">
		<div class="outer-input">
			<form:label path="version" id="projectVersion">Version</form:label>
			<br />
			<div class="inner-input">
				<form:input path="version" />
				<form:errors cssClass="error" path="version" />
			</div>
		</div>
	</div>
	<form:hidden path="idProject" />
	<input type="submit" class="controls right clear" value="Save"
		id="submitButton">
</form:form>
