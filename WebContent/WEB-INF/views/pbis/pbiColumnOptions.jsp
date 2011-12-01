<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<div id="columnOptions" class="hidden">
	<table class="itemList pbis grid" id="pbiColumnsTable">
		<thead>
			<tr>
				<th scope="col" id="columnName">Column</th>
				<th scope="col">Visible</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>PBI title</td>
				<td><input type="checkbox" id="chkColumn-title"
					class="controls" /> <label for="chkColumn-title"
					class="controls chkBox"></label></td>
			</tr>
			<tr>
				<td>Priority</td>
				<td><input type="checkbox" id="chkColumn-priority"
					class="controls" /> <label for="chkColumn-priority"
					class="controls chkBox"></label></td>
			</tr>
			<tr>
				<td>Story points</td>
				<td><input type="checkbox" id="chkColumn-storyPoints"
					class="controls" /> <label for="chkColumn-storyPoints"
					class="controls chkBox"></label></td>
			</tr>
			<c:forEach var="a" items="${attributes }">
				<tr>
					<td>${a.name}</td>
					<td><input type="checkbox" id="chkColumn-${a.camelName }"
						class="controls" /> <label for="chkColumn-${a.camelName }"
						class="controls chkBox"></label></td>
				</tr>
			</c:forEach>
			<tr>
				<td>Team</td>
				<td><input type="checkbox" id="chkColumn-team"
					class="controls" /> <label for="chkColumn-team"
					class="controls chkBox"></label></td>
			</tr>
			<tr>
				<td>Sprint</td>
				<td><input type="checkbox" id="chkColumn-sprint"
					class="controls" /> <label for="chkColumn-sprint"
					class="controls chkBox"></label></td>
			</tr>
			<tr>
				<td>Status</td>
				<td><input type="checkbox" id="chkColumn-status"
					class="controls" /> <label for="chkColumn-status"
					class="controls chkBox"></label></td>
			</tr>
		</tbody>
	</table>
	<button id="columns-ok" class="controls right clear">OK</button>
</div>