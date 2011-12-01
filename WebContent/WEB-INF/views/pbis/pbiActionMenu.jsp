<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<security:authentication property="principal.username" var="userLogin" />

<security:authorize
	access="hasAnyRole('ROLE_PRODUCT_OWNER', 'ROLE_SCRUM_MASTER')">
	<c:choose>
		<c:when test="${op == 'list' }">
			<div class="row" id="actionMenu">
				<div class="right">
					<button id="am-markAsDonePBIs" class="controls done">Mark PBI as done</button>
					<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
						<button id="am-addPBI" class="controls add spacerL">Add
							new PBI</button>
					</security:authorize>

					<button id="am-editPBI" class="controls edit">Edit PBI</button>

					<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
						<button id="am-deletePBIs" class="controls delete">Remove PBI</button>
					</security:authorize>

					<input type="checkbox" id="am-selectAllPBIs"
						class="controls selectAll"> <label
						class="controls selectAll spacerL" for="am-selectAllPBIs">Select
						all</label>
				</div>
			</div>
		</c:when>
		<c:when test="${op == 'add' or op == 'edit' }">
			<div class="row" id="actionMenu">
				<div class="right">
					<button id="am-clearPBIForm" class="controls clearForm spacerL">Clear
						form</button>
				</div>
			</div>
		</c:when>
		<c:when test="${op == 'details'}">
			<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
				<div class="row" id="actionMenu">
					<div class="right">
						<button id="am-editPBI" class="controls edit">Edit PBI</button>
						<button id="am-deletePBI" class="controls delete">Remove
							PBI</button>
					</div>
				</div>
			</security:authorize>
			<security:authorize
				access="hasRole('ROLE_SCRUM_MASTER') and not hasRole('ROLE_PRODUCT_OWNER')">
				<c:if
					test="${not empty pbi.workItems[0].team and pbi.workItems[0].team.user.username == userLogin }">
					<div class="row" id="actionMenu">
						<div class="right">
							<button id="am-editPBI" class="controls edit">Edit PBI</button>
						</div>
					</div>
				</c:if>
			</security:authorize>
		</c:when>
	</c:choose>
</security:authorize>