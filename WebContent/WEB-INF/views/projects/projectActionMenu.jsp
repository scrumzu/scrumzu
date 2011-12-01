<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
    prefix="security"%>
    
<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
    <div class="row" id="actionMenu">
		<div class="right">
		    <c:choose>
		        <c:when test="${op == 'listByProject' or op == 'listAll' }">
		            <button id="am-addProject" class="controls add">Add new project</button>
		            <button id="am-editProject" class="controls edit">Edit project</button>
		            <button id="am-deleteProjects" class="controls delete">Remove project</button>
		            <input type="checkbox" id="am-selectAllProjects" class="controls selectAll">
		            <label class="controls selectAll spacerL" for="am-selectAllProjects">Select all</label>
		        </c:when>
		        <c:when test="${op == 'add' or op == 'edit' }">
		            <button id="am-clearProjectForm" class="controls clearForm spacerL">Clear form</button>
		        </c:when>
		        <c:when test="${op == 'details'}">
		            <button id="am-addAttribute" class="controls add">Add new attribute</button>
		            <button id="am-deleteAttributes" class="spacerR controls delete">Remove attribute</button>
		            <button id="am-editProject" class="controls edit">Edit project</button>
		            <button id="am-deleteProject" class="controls delete">Remove project</button>
		            <input type="checkbox" id="am-selectAllProjects" class="controls selectAll">
		        </c:when>
		    </c:choose>
		</div>
	</div>
</security:authorize>
