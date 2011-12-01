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
		<c:set var="actionURI" value="${projPath}/attributes/new" />
	</c:when>
</c:choose>

<h1>${opTitle} attribute</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}AttributeForm" class="form" commandName="attribute">
	<div class="sixcol">
		<div class="outer-input">
			<form:label path="name" id="attributeName">Name</form:label>
			<br />
			<div class="inner-input">
				<form:input path="name" id="attributeFormName"/>
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
		<div class="threecol last">
			<form:label path="type">Type</form:label>
			<form:select path="type" items="${types}" itemLabel="value"
				id="attributeFormType" />
		</div>
	</div>
	<form:hidden path="idAttribute" />
	<input type="submit" class="controls right clear" value="Save"
		id="submitButton">
</form:form>
