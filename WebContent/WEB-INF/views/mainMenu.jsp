<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<div id="mainMenu">
	<ul class="topnav ui-state-hover">
		<security:authorize access="hasRole('ROLE_ADMIN')">
			<li><a href="${scrumzuPath}/users">Users</a>
				<ul class="subnav">
					<li class="promote"><a href="${scrumzuPath}/users">All
							users</a></li>
					<li class="promote"><a href="${scrumzuPath}/users/new">Add
							user</a></li>
				</ul></li>
		</security:authorize>
		<c:choose>
			<c:when test="${not empty chosenProjectAlias}">
				<li><a href="${projPath}/teams">Teams</a>
					<ul class="subnav">
						<li class="promote"><a href="${scrumzuPath}/teams">All
								teams</a></li>
						<security:authorize access="hasRole('ROLE_SCRUM_MASTER')">
							<li class="promote"><a href="${projPath}/teams/new">Add
									new team</a></li>
						</security:authorize>
						<c:forEach var="team" items="${teams}">
							<li><a id="mm-team${team.idTeam}"
								href="${projPath}/teams/${team.idTeam}">${team.name}</a></li>
						</c:forEach>
					</ul></li>
				<li><a href="${projPath}/pbis">Backlog</a></li>
				<li><a href="${projPath}/releases">Releases</a></li>
				<li><a href="${projPath}/sprints">Sprints</a></li>
				<li><a href="${projPath}">${chosenProjectAlias}</a>
					<ul class="subnav">
						<li class="promote"><a href="${scrumzuPath}/projects">All
								projects</a></li>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<li class="promote"><a href="${scrumzuPath}/projects/new">Add
									new project</a></li>
						</security:authorize>
						<c:forEach var="proj" items="${projects}">
							<li><a id="mm-project${proj.idProject}"
								href="${scrumzuPath}/${proj.alias}">${proj.alias}</a></li>
						</c:forEach>
					</ul></li>
			</c:when>
			<c:when test="${empty chosenProjectAlias}">
				<li><a href="${scrumzuPath}/teams">Teams</a>
					<ul class="subnav">
						<li class="promote"><a href="${scrumzuPath}/teams">All
								teams</a></li>
						<security:authorize access="hasRole('ROLE_SCRUM_MASTER')">
							<li class="promote"><a href="${scrumzuPath}/teams/new">Add
									new team</a></li>
						</security:authorize>
						<c:forEach var="team" items="${teams}">
							<li><a id="mm-team${team.idTeam}"
								href="${scrumzuPath}/teams/${team.idTeam}">${team.name}</a></li>
						</c:forEach>
					</ul></li>
				<li><a href="${scrumzuPath}/projects">Projects</a>
					<ul class="subnav">
						<li class="promote"><a href="${scrumzuPath}/projects">All
								projects</a></li>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<li class="promote"><a href="${scrumzuPath}/projects/new">Add
									new project</a></li>
						</security:authorize>
						<c:forEach var="proj" items="${projects}">
							<li><a id="mm-project${proj.idProject}"
								href="${scrumzuPath}/${proj.alias}">${proj.alias}</a></li>
						</c:forEach>
					</ul>
			</c:when>
		</c:choose>
	</ul>
</div>