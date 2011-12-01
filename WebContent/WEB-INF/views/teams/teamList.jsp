<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />
<security:authentication property="principal.username" var="userLogin" />

<c:choose>
	<c:when test="${not empty chosenProjectAlias}">
		<c:set var="teamPath" value="${projPath}/teams" />
	</c:when>
	<c:otherwise>
		<c:set var="teamPath" value="${scrumzuPath}/teams" />
	</c:otherwise>
</c:choose>

<h1 class="inline">Teams</h1>
<table class="itemList teams grid" id="teamListTable">
	<thead>
		<tr>
			<th scope="col" id="detailsCol"></th>
			<security:authorize access="hasRole('ROLE_SCRUM_MASTER')">
				<th scope="col" class="checkboxCol"></th>
			</security:authorize>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty teams}">
				<c:forEach var="team" items="${teams}">
					<tr id="team${team.idTeam}">
						<td>
							<h3>
								<a href="${teamPath}/${team.idTeam}"
									id="team${team.idTeam}DetailsLink">${team.alias} -
									${team.name}</a>
							</h3>
							<p>${team.description}</p>
							<ul>
								<li><b>Project: </b><a
									href="${scrumzuPath}/${team.project.alias}">${team.project.name}</a></li>
								<li><b>Scrum Master: </b>${team.user.username}</li>
							</ul>
						</td>
						<security:authorize access="hasRole('ROLE_SCRUM_MASTER')">
							<td><c:if test="${empty team.user.username or team.user.username eq userLogin}">
									<input type="checkbox" id="chk${team.idTeam}" name="ids"
										value="${team.idTeam}" class="controls" />
									<label for="chk${team.idTeam}" class="controls chkBox"></label>
								</c:if></td>

						</security:authorize>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
</table>