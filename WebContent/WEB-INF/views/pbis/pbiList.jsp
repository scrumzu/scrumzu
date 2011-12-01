<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<h1>Backlog</h1>
<jsp:include page="pbiFilterOptions.jsp"></jsp:include>
<jsp:include page="pbiColumnOptions.jsp"></jsp:include>
<div id="filterActionMenu" class="eightcol">
	<div class="left">
		<button id="filter-select" class="controls filter">Filter</button>
		<button id="filter-clear" class="controls clearFilter">Clear</button>
		<button id="columns-visibility" class="controls options spacerL">Columns visibility</button>
	</div>
</div>
<jsp:include page="pbiTable.jsp"></jsp:include>
