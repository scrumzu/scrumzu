<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<security:authentication property="principal.username" var="userLogin" />


<security:authorize access="hasRole('ROLE_SCRUM_MASTER')">
	<div class="row" id="actionMenu">
		<c:choose>
			<c:when test="${op == 'listByProject' or op == 'listAll' }">
				<div class="right">
					<button id="am-addTeam" class="controls add">Add new team</button>
					<button id="am-editTeam" class="controls edit">Edit team</button>
					<button id="am-deleteTeams" class="controls delete">Remove
						team</button>
					<input type="checkbox" id="am-selectAllTeams"
						class="controls selectAll"> <label
						class="controls selectAll spacerL" for="am-selectAllTeams">Select
						all</label>
				</div>
			</c:when>
			<c:when test="${op == 'add' or op == 'edit' }">
				<div class="right">
					<button id="am-clearTeamForm" class="controls clearForm spacerL">Clear
						form</button>
				</div>
			</c:when>
			<c:when
				test="${op == 'details' and (team.user.username eq userLogin or empty team.user.username)}">
				<div class="right">
					<button id="am-editTeam" class="controls edit">Edit team</button>
					<button id="am-deleteTeam" class="controls delete">Remove
						team</button>
				</div>
			</c:when>
		</c:choose>
	</div>
</security:authorize>


