<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${pbi.project.alias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<span class="smallInfo">${pbi.type}<br></span>
<h1 id="pbiDetailsTitle">${pbi.title}</h1>
<p id="pbiDetailsDescription">${pbi.description}</p>
<div class="sixcol">
	<ul>
		<li id="pbiDetailsProject"><b>Project:</b> <a href="${projPath}">${pbi.project.alias}
				- ${pbi.project.name}</a></li>
		<li id="pbiDetailsTeam"><b>Team:</b> <c:choose>
				<c:when test="${not empty pbi.workItems[0].team}">
					<a href="${scrumzuPath}/teams/${pbi.workItems[0].team.idTeam}">${pbi.workItems[0].team.alias}
						- ${pbi.workItems[0].team.name}</a>
				</c:when>
			</c:choose></li>
		<li id="pbiDetailsSprint"><b>Sprint:</b> <c:choose>
				<c:when test="${not empty pbi.workItems[0].sprint}">
		 ${pbi.workItems[0].sprint.name}
			<span class="smallInfo">(from: <fmt:formatDate
							pattern="yyyy-MM-dd" value="${pbi.workItems[0].sprint.dateFrom}" />
						to: <fmt:formatDate pattern="yyyy-MM-dd"
							value="${pbi.workItems[0].sprint.dateTo}" />)
					</span>
				</c:when>
			</c:choose></li>
		<li id="pbiDetailsStatus"><b>Status:</b>
			${pbi.workItems[0].status.value}</li>
	</ul>
</div>
<div class="sixcol last">
	<ul>
		<li id="pbiDetailsPriority"><b>Priority:</b> ${pbi.priority}</li>
		<li id="pbiDetailsStoryPoints"><b>Story points:</b>
			${pbi.workItems[0].storyPoints}</li>
		<li id="pbiDetailsDateCreation"><b>Created:</b>
			${pbi.dateCreation}</li>
	</ul>
</div>
<c:if test="${not empty attributes}">
	<div class="sixcol clear last spacerT">
		<h2>Project specific attributes:</h2>
		<ul>
			<c:forEach var="a" items="${attributes}">
				<li id="pbiFormCustomAttr-${a.camelName}"><b>${a.name} <span class="smallInfo">(${atrib.type})</span>: </b><c:choose>
						<c:when test="${not empty pbi.doubleAttributes[a]}">
                                   ${pbi.getDoubleAttribute(a.name)}
                                </c:when>
						<c:when test="${not empty pbi.stringAttributes[a]}">
                                    ${pbi.getStringAttribute(a.name)}
                                </c:when>
					</c:choose></li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<div class="twelvecol last clear spacerT" id="pbiDetailsHistory">
	<h2 class="inline">PBI status history</h2>
	<table class="itemList pbisHistory grid" id="pbiDetailsHistoryTable">
		<thead>
			<tr>
				<th scope="col" id="dateCol">Date<span></span></th>
				<th scope="col" id="timeCol">Time<span></span></th>
				<th scope="col" id="sprintCol">Sprint<span></span></th>
				<th scope="col" id="teamCol">Team<span></span></th>
				<th scope="col" id="statusCol">Status<span></span></th>
				<th scope="col" id="storyPointsCol">Story points<span></span></th>
				<th scope="col" id="userCol">Modified by<span></span></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pbi.workItems}" var="wi">
				<tr>
					<td><span class="ui-icon ui-icon-calendar left"></span>
						${fn:split(wi.date,' ')[0]}</td>
					<td><span class="ui-icon ui-icon-clock left"></span>
						${fn:split(fn:split(wi.date,' ')[1], '.')[0]}</td>
					<td><c:if test="${not empty wi.sprint}">
							<a href="${projPath}/sprints/${wi.sprint.idSprint}">${wi.sprint.name}</a>
						</c:if></td>
					<td><c:if test="${not empty wi.team}">
							<a href="${scrumzuPath}/teams/${wi.team.idTeam}">${wi.team.name}</a>
						</c:if></td>
					<td>${wi.status.value}</td>
					<td>${wi.storyPoints}</td>
					<td>${wi.user.username}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<input type="checkbox" name="ids" value="${pbi.idPBI}" checked="checked"
	class="hidden" />

