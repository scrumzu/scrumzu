<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty param.error}">
	<div class="row">
		<div class="ui-state-error" id="errorMsg">
			<p>
				<span class="ui-icon ui-icon-alert"></span>${SPRING_SECURITY_LAST_EXCEPTION.message}
			</p>
		</div>
	</div>
</c:if>