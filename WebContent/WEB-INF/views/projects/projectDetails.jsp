<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1 id="projectDetailsHeader">${project.alias} - ${project.name}</h1>
<p id="projectDetailsDescription">${project.description}</p>
<div class="sixcol last clear">
	<ul>
		<li id="projectDetailsWebsite"><b>Website: </b><a
			href="http://${project.url}">${project.url}</a>
		<li id="projectDetailsVersion"><b>Version: </b>${project.version}</li>
		<li id="projectDetailsOwner"><b>Product owner: </b>${project.owner}</li>
	</ul>
</div>
<div class="sixcol clear last spacerT">
	<h2>Project specific attributes:</h2>
	<jsp:include page="attributeTable.jsp"></jsp:include>
</div>
<input type="checkbox" name="ids" value="${project.idProject}"
	checked="checked" class="hidden" />
