<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1 id="teamDetailsHeader">${team.alias} - ${team.name}</h1>
<p id="teamDetailsDescription">${team.description}</p>
<div class="sixcol last clear">
	<ul>
		<li id="teamDetailsProject"><b>Project:</b> <a
			href="${pageContext.request.contextPath}/${team.project.alias}">${team.project.name}</a></li>
		<li id="teamDetailsScrumMaster"><b>Scrum Master: </b>
			${team.user.username}</li>
	</ul>
</div>
<input type="checkbox" name="ids" value="${team.idTeam}"
	checked="checked" class="hidden" />
