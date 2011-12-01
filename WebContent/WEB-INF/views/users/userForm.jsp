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
		<c:set var="actionURI" value="${scrumzuPath}/users/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI"
			value="${scrumzuPath}/users/${user.idUser}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} User</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}UserForm" class="form" commandName="user">
	<div class="threecol">
		<div class="outer-input">
			<form:label path="username" id="username">Username</form:label>
			<br />
			<div class="inner-input">
				<form:input path="username" id="userFormUsername" />
				<form:errors cssClass="error" path="username" />
			</div>
		</div>
		<div class="outer-input" id="userFormPasswordOptions">
			<form:label path="password" id="password">
				<c:choose>
					<c:when test="${op == 'add'}">
			        Password:
			        </c:when>
					<c:when test="${op == 'edit'}">
						<input type="checkbox" id="changePassword" name="changePassword"
							class="controls" />
						<label for="changePassword" class="controls chkBox"></label>Change password:
                    </c:when>
				</c:choose>
			</form:label>
			<br />
			<div class="inner-input">
				<c:choose>
					<c:when test="${op == 'add'}">
						<input type="password" name="newPassword" id="userFormPassword">
					</c:when>
					<c:when test="${op == 'edit'}">
						<input type="password" name="newPassword" id="userFormNewPassword">
					</c:when>
				</c:choose>
				<form:hidden path="password" />
				<form:errors cssClass="error" path="password" />
			</div>
		</div>
	</div>
	<div class="threecol last">
		<form:label path="authoritiesList">Authority</form:label>
		<form:select path="authoritiesList" items="${authorities}"
			itemLabel="simpleName" id="userAuthorities" itemValue="idAuthority" />
		<form:errors cssClass="error" path="authoritiesList" />
	</div>
	<form:hidden path="idUser" />
	<input type="submit" class="controls right clear" value="Save"
		id="submitButton">
</form:form>
