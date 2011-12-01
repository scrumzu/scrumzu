<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
    prefix="security"%>

<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
    <div class="row" id="actionMenu">
        <div class="right">
			<c:choose>
				<c:when test="${op == 'list' }">
				    <button id="am-startSprint" class="controls start spacerL">Start sprint</button>
				    <button id="am-endSprint" class="controls stop">End sprint</button>
					<button id="am-addSprint" class="controls add spacerL">Add new sprint</button>
					<button id="am-editSprint" class="controls edit">Edit sprint</button>
					<button id="am-deleteSprints" class="controls delete">Remove sprint</button>
					<input type="checkbox" id="am-selectAllSprints" class="controls selectAll">
					<label class="controls selectAll spacerL" for="am-selectAllSprints">Select all</label>
				</c:when>
			    <c:when test="${op == 'add' or op == 'edit' }">
		            <button id="am-clearSprintForm" class="controls clearForm spacerL">Clear form</button>
		        </c:when>
		        <c:when test="${op == 'details'}">
					<button id="am-editSprint" class="controls edit">Edit sprint</button>
		            <button id="am-deleteSprint" class="controls delete">Remove sprint</button>
		        </c:when>
			</c:choose>
		</div>
    </div>
</security:authorize>
