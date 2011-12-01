<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<div id="filterOptions" class="hidden">
	<h3>Choose filter</h3>
	<select id="filterList">
		<option value="0">New filter...</option>
		<c:forEach var="filter" items="${filters}">
			<option value="${filter.idFilter}">${filter.name}</option>
		</c:forEach>
	</select>
	<h3>Create/edit filter</h3>
	<table id="filterTable" class="itemList grid">
		<thead>
			<tr>
				<th id="filterTableLogic">And/or</th>
				<th id="filterTableColumn">Column</th>
				<th id="filterTableOperator">Operator</th>
				<th id="filterTableValue">Value</th>
				<th id="filterTableControls"></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td></td>
				<td><select class="filterColumns">
						<option value="title">PBI title</option>
						<option value="priority">Priority</option>
						<option value="storyPoints">Story points</option>
						<option value="team">Team</option>
						<option value="sprint">Sprint</option>
						<option value="status">Status</option>
				</select></td>
				<td><select class="filterOperators">
						<option value="eq">=</option>
						<option value="neq">!=</option>
				</select></td>
				<td><input class="filterValue"></td>
				<td>
					<button class="filter-addColumn controls add nt">+</button>
				</td>
			</tr>
		</tbody>
	</table>
	<h3>Save filter</h3>
	<div id="filterWindowActionMenu">
		<div class="row">
			<div class="twocol last">
				<label for="newFilterName" id="newFilterLabel"><b>Save
						as: </b></label>
			</div>
			<div class="sevencol last">
				<div class="outer-input">
					<div class="inner-input">
						<input id="newFilterName"></input>
					</div>
				</div>
			</div>
			<div class="onecol">
				<button id="filter-save" class="controls savenew nt">Save as new</button>
			</div>
			<div class="twocol last right">
				<button id="filter-delete" class="controls delete nt right">Delete</button>
				<button id="filter-update" class="controls save nt right">Save and update</button>
			</div>
		</div>
		<div class="row">
			<div class="fourcol last">
				<input type="checkbox" id="filterPublic" name="filterPublic"
					class="controls" /> <label for="filterPublic"
					class="controls chkBox"></label> for public use
			</div>
		</div>
	</div>

	<button id="filter-run" class="controls right clear">OK</button>
</div>