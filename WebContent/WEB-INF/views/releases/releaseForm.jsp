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
		<c:set var="actionURI" value="${projPath}/releases/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI"
			value="${projPath}/releases/${release.idRelease}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} release</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}ReleaseForm" class="form" commandName="release">
	<div class="eightcol last">
	<h2>Release details</h2>
		<div class="outer-input">
			<form:label path="name">Name</form:label>
			<br />
			<div class="inner-input">
				<form:input path="name" id="releaseFormName" class="controls" />
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
	</div>
	<div class="threecol clear">
		<div class="outer-input">
			<form:label path="dateFrom">Date from</form:label>
			<br /> <span class="ui-icon ui-icon-calendar left"></span>
			<div class="inner-input date">
				<form:input path="dateFrom" id="releaseFormDateFrom"
					class="controls datepicker" />
				<form:errors cssClass="error" path="dateFrom" />
			</div>
		</div>
	</div>
	<div class="threecol">
		<div class="outer-input">
			<form:label path="dateTo">Date to</form:label>
			<br /> <span class="ui-icon ui-icon-calendar left"></span>
			<div class="inner-input date">
				<form:input path="dateTo" id="releaseFormDateTo"
					class="controls datepicker" />
				<form:errors cssClass="error" path="dateTo" />
			</div>
		</div>
	</div>

	<div class="twelvecol last clear spacerT">
		<h2 class="inline">Select PBIs</h2>
		<table class="itemList pbis grid" id="pbisInReleaseFormTable">
			<thead>
				<tr>
					<th scope="col" id="nameCol">PBI title<span></span></th>
					<th scope="col" class="checkboxCol"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="pbi" items="${pbis}" varStatus="row">
					<tr id="pbi${pbi.idPBI}">
						<td><h4>
								<span class="controls showInfo ui-icon-squaresmall-plus"></span><a
									href="${projPath}/pbis/${pbi.idPBI}"
									id="pbi${pbi.idPBI}DetailsLink">${pbi.title}</a>
							</h4>
							<p class="hidden">${pbi.description}</p></td>

						<!-- <input type="checkbox" checked="checked" value="2" name="pbis" id="pbis2">-->
						<td><label for="pbis${row.index + 1}" class="controls chkBox"></label></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="hidden">
		<form:checkboxes items="${pbis}" path="pbis" itemValue="idPBI"
			cssClass="controls" />
	</div>
	<form:hidden path="idRelease" />
	<input type="submit" class="controls right clear spacerT" value="Save"
		id="submitButton">
</form:form>
