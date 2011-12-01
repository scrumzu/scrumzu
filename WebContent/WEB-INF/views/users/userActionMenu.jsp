
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
    prefix="security"%>
    
<security:authorize access="hasRole('ROLE_ADMIN')">
    <div class="row" id="actionMenu">
        <div class="right">
            <c:choose>
                <c:when test="${op == 'list'}">
                    <button id="am-addUser" class="controls add">Add new user</button>
                    <button id="am-editUser" class="controls edit">Edit user</button>
                    <button id="am-deleteUsers" class="spacerR controls delete">Remove user</button>
                    <input type="checkbox" id="am-selectAllUsers" class="controls selectAll">
                    <label class="controls selectAll spacerL" for="am-selectAllUsers">Select all</label>
                </c:when>
                <c:when test="${op == 'add' or op == 'edit' }">
                    <button id="am-clearUserForm" class="controls clearForm spacerL">Clear form</button>
                </c:when>
            </c:choose>
        </div>
    </div>
</security:authorize>