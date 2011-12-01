<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />


<table class="itemList attributes grid" id="attributeListTable">
	<thead>
		<tr>
			<th scope="col" id="nameCol">Attribute name<span></span></th>
			<th scope="col" id="attributeTypeCol">Type<span></span></th>
			<security:authorize
				access="hasRole('ROLE_PRODUCT_OWNER')">
				<th scope="col" class="checkboxCol"></th>
			</security:authorize>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty attributes}">
				<c:forEach var="a" items="${attributes }">
					<tr id="attribute${a.idAttribute}">
						<td>${a.name}</td>
						<td>${a.type.value }</td>
						<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
							<td><input type="checkbox" id="chk${a.idAttribute}" name="idsAttribute"
								value="${a.idAttribute}" class="controls" /><label
								for="chk${a.idAttribute}" class="controls chkBox"></label></td>
						</security:authorize>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr id="emptyList">
					<td colspan="3">No Attributes set for this project.</td>
					<td class="hidden"></td>
					<td class="hidden"></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>