<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<h1 id="releaseDetailsName">${release.name}</h1>
<div class="sixcol last clear">
	<ul>
		<li id="releaseDetailsDateFrom"><b>Date from:</b>
			${release.dateFrom}</li>
		<li id="releaseDetailsDateTo"><b>Date to:</b> ${release.dateTo}</li>
		<li id="releaseDetailsStatus"><b>Project:</b>
			${release.project.name}</li>
	</ul>
</div>
<div class="twelvecol last clear spacerT" id="burndownChart">
	<h2>Release burndown chart</h2>
	<div id="chart"></div>
</div>
<div class="twelvecol last clear spacerT">
	<h2 class="inline">Selected PBIs</h2>
	<table class="itemList pbisInRelease grid" id="pbisInReleaseTable">
		<thead>
			<tr>
				<th scope="col" id="pbiTitleCol">Title<span></span></th>
				<th scope="col" id="dateCol">Added on<span></span></th>
				<th scope="col" id="userCol">Added by<span></span></th>
 				<th scope="col" id="statusCol">Status<span></span></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty release.releaseItems }">
					<c:forEach items="${release.releaseItems}" var="ri">
						<tr>
							<td>${ri.pbi.title}</td>
							<td><span class="ui-icon ui-icon-calendar left"></span>
								${ri.date}</td>
							<td>${ri.user.username}</td>
							<td>${ri.pbi.workItems[0].status.value}</td>
						</tr>
					</c:forEach>
				</c:when>
			</c:choose>
		</tbody>
	</table>
</div>

<input type="checkbox" name="ids" value="${release.idRelease}"
	checked="checked" class="hidden" />
