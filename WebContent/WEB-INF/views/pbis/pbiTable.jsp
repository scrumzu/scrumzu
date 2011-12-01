<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />
<security:authentication property="principal.username" var="userLogin" />

<table class="itemList pbis grid" id="pbiListTable">
	<thead>
		<tr>
			<th scope="col" id="nameCol">PBI title<span></span></th>
			<th scope="col" id="priorityCol">Priority<span></span></th>
			<th scope="col" id="storyPointsCol">Story points<span></span></th>
			<c:forEach var="a" items="${attributes }">
				<th scope="col" id="${a.name}Col">${a.name}<span></span></th>
			</c:forEach>
			<th scope="col" id="teamCol">Team<span></span></th>
			<th scope="col" id="sprintCol">Sprint<span></span></th>
			<th scope="col" id="statusCol">Status<span></span></th>
			<security:authorize
				access="hasAnyRole('ROLE_PRODUCT_OWNER', 'ROLE_SCRUM_MASTER')">
				<th scope="col" class="checkboxCol"></th>
			</security:authorize>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty pbis}">
				<c:forEach var="pbi" items="${pbis}">
					<tr id="pbi${pbi.idPBI}">
						<td>
							<h4>
								<span class="controls showInfo ui-icon-squaresmall-plus"></span><a
									href="${projPath}/pbis/${pbi.idPBI}"
									id="pbi${pbi.idPBI}DetailsLink">${pbi.title}</a>
							</h4>
							<p class="hidden">${pbi.description}</p>
						</td>
						<td class="numeric">${pbi.priority}</td>
						<td class="numeric">${pbi.workItems[0].storyPoints}</td>

						<c:forEach var="a" items="${attributes}">
							<c:choose>
								<c:when test="${not empty pbi.doubleAttributes[a]}">
									<td class="numeric">${pbi.doubleAttributes[a]}</td>
								</c:when>
								<c:when test="${not empty  pbi.stringAttributes[a]}">
									<td>${pbi.stringAttributes[a]}</td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<td><c:if test="${not empty pbi.workItems[0].team}">
								<a href="${projPath}/teams/${pbi.workItems[0].team.idTeam}"
									id="team${pbi.workItems[0].team.idTeam}DetailsLink">${pbi.workItems[0].team.alias}</a>
							</c:if></td>
						<td><c:if test="${not empty pbi.workItems[0].sprint}">
								<a
									href="${projPath}/sprints/${pbi.workItems[0].sprint.idSprint}"
									id="sprint${pbi.workItems[0].sprint.idSprint}DetailsLink">${pbi.workItems[0].sprint.name}</a>
							</c:if></td>
						<td class="pbiStatusCell">${pbi.workItems[0].status.value}</td>

						<td><security:authorize
								access="hasRole('ROLE_PRODUCT_OWNER')">
								<input type="checkbox" id="chk${pbi.idPBI}" name="ids"
									value="${pbi.idPBI}" class="controls" />
								<label for="chk${pbi.idPBI}" class="controls chkBox"></label>
							</security:authorize> <security:authorize
								access="hasRole('ROLE_SCRUM_MASTER') and not hasRole('ROLE_PRODUCT_OWNER')">
								<c:if
									test="${not empty pbi.workItems[0].team and pbi.workItems[0].team.user.username == userLogin }">
									<input type="checkbox" id="chk${pbi.idPBI}" name="ids"
										value="${pbi.idPBI}" class="controls" />
									<label for="chk${pbi.idPBI}" class="controls chkBox"></label>
								</c:if>
							</security:authorize></td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
</table>