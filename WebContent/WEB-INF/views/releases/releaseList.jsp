<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>


<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<h1 class="inline">Releases</h1>
<table class="itemList releases grid" id="releaseListTable">
	<thead>
		<tr>
			<th scope="col" id="nameCol">Release<span></span></th>
			<th scope="col" id="dateFromCol">Date from<span></span></th>
			<th scope="col" id="dateToCol">Date to<span></span></th>
			<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
				<th scope="col" class="checkboxCol"></th>
			</security:authorize>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty releases}">
				<c:forEach var="release" items="${releases}">
					<tr id="release${release.idRelease}">
						<td>
							<h4>
								<a href="${projPath}/releases/${release.idRelease}"
									id="release${release.idRelease}DetailsLink">${release.name}</a>
							</h4>
						</td>
						<td class="numeric">${release.dateFrom}</td>
						<td class="numeric">${release.dateTo}</td>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<td><input type="checkbox" id="chk${release.idRelease}"
								name="ids" value="${release.idRelease}" class="controls" /><label
								for="chk${release.idRelease}" class="controls chkBox"></label></td>
						</security:authorize>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
</table>