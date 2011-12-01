<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<security:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')">
	<h1 class="inline">Users list</h1>
	<table class="itemList users grid" id="userListTable">
		<thead>
			<tr>
				<th scope="col" id="usernameCol">Username<span></span></th>
				<th scope="col" id="teamMemberAuthCol">Team Member<span></span></th>
				<th scope="col" id="scrumMasterAuthCol">Scrum Master<span></span></th>
				<th scope="col" id="productOwnerAuthCol">Product Owner<span></span></th>
				<th scope="col" id="adminAuthCol">Administrator<span></span></th>
				<th scope="col" class="checkboxCol"></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty users}">
					<c:forEach var="user" items="${users}">
						<tr id="user${user.idUser}">
							<td>${user.username}</td>
							<td><c:if test="${user.hasRole('ROLE_TEAM_MEMBER')}">
									<span class="ui-icon ui-icon-bullet center">team member</span>
								</c:if></td>
							<td><c:if test="${user.hasRole('ROLE_SCRUM_MASTER')}">
									<span class="ui-icon ui-icon-bullet center">scrum master</span>
								</c:if></td>
							<td><c:if test="${user.hasRole('ROLE_PRODUCT_OWNER')}">
									<span class="ui-icon ui-icon-bullet center">product
										owner</span>
								</c:if></td>
							<td><c:if test="${user.hasRole('ROLE_ADMIN')}">
									<span class="ui-icon ui-icon-bullet center">administrator</span>
								</c:if></td>
							<td><c:if test="${not user.hasRole('ROLE_ADMIN')}">
									<input type="checkbox" id="chk${user.idUser}" name="ids"
										value="${user.idUser}" class="controls" />
									<label for="chk${user.idUser}" class="controls chkBox"></label>
								</c:if></td>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
		</tbody>
	</table>
</security:authorize>
