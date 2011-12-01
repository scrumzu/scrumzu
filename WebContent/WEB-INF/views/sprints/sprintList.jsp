<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>


<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<h1 class="inline">Sprints</h1>
	<table class="itemList sprints grid" id="sprintListTable">
		<thead>
			<tr>
				<th scope="col" id="nameCol">Sprint<span></span></th>
				<th scope="col" id="dateFromCol">Date from<span></span></th>
				<th scope="col" id="dateToCol">Date to<span></span></th>
				<th scope="col" id="pbisNoCol">No. of PBIs<span></span></th>
				<th scope="col" id="teamsNoCol">No. of teams<span></span></th>
				<th scope="col" id="statusCol">Status<span></span></th>
				<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
					<th scope="col" class="checkboxCol"></th>
				</security:authorize>
			</tr>
		</thead>
		<tbody>
		<c:choose>
			<c:when test="${not empty sprints}">
				<c:forEach var="sprint" items="${sprints}">
					<tr id="sprint${sprint.idSprint}">
						<td>
							<h4>
								<a href="${projPath}/sprints/${sprint.idSprint}"
									id="sprint${sprint.idSprint}DetailsLink">${sprint.name}</a>
							</h4>
						</td>
						<td class="numeric">${sprint.dateFrom}</td>
						<td class="numeric">${sprint.dateTo}</td>
						<td class="numeric">${pbisCount.get(sprint.idSprint)}</td>
						<td class="numeric">${teamsCount.get(sprint.idSprint)}</td>
						<td>${sprint.sprintStatus.value}</td>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<td><input type="checkbox" id="chk${sprint.idSprint}"
								name="ids" value="${sprint.idSprint}" class="controls" /><label
								for="chk${sprint.idSprint}" class="controls chkBox"></label></td>
						</security:authorize>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
	</table>