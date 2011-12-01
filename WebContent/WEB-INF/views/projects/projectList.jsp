<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<h1 class="inline">Projects</h1>
<table class="itemList projects grid" id="projectListTable">
	<thead>
		<tr>
			<th scope="col" id="detailsCol"></th>
			<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
				<th scope="col" class="checkboxCol"></th>
			</security:authorize>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty projects}">
				<c:forEach var="project" items="${projects}">
					<tr id="project${project.idProject}">
						<td>
							<h3>
								<a href="${scrumzuPath}/${project.alias }"
									id="project${project.idProject}DetailsLink">${project.alias
									} - ${project.name}</a>
							</h3>
							<p>${project.description}</p>
							<ul>
								<li><b>Website: </b><a href="http://${project.url}">${project.url}</a>
								<li><b>Version: </b>${project.version}</li>
								<li><b>Product owner: </b>${project.owner}</li>
							</ul>
						</td>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<td><input type="checkbox" id="chk${project.idProject}"
								name="ids" value="${project.idProject}" class="controls" /><label
								for="chk${project.idProject}" class="controls chkBox"></label></td>
						</security:authorize>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
</table>