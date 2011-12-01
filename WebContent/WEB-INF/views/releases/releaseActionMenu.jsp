<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
    prefix="security"%>

<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
    <div class="row" id="actionMenu">
        <div class="right">
			<c:choose>
				<c:when test="${op == 'list' }">
					<button id="am-addRelease" class="controls add spacerL">Add new release</button>
					<button id="am-editRelease" class="controls edit">Edit release</button>
					<button id="am-deleteReleases" class="controls delete">Remove release</button>
					<input type="checkbox" id="am-selectAllReleases" class="controls selectAll">
					<label class="controls selectAll spacerL" for="am-selectAllReleases">Select all</label>
				</c:when>
			    <c:when test="${op == 'add' or op == 'edit' }">
		            <button id="am-clearReleaseForm" class="controls clearForm spacerL">Clear form</button>
		        </c:when>
		        <c:when test="${op == 'details'}">
					<button id="am-editRelease" class="controls edit">Edit release</button>
		            <button id="am-deleteRelease" class="controls delete">Remove release</button>
		        </c:when>
			</c:choose>
		</div>
    </div>
</security:authorize>
