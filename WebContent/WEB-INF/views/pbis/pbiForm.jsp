<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<c:set var="projPath"
	value="${pageContext.request.contextPath}/${chosenProjectAlias}" />
<c:set var="scrumzuPath" value="${pageContext.request.contextPath}" />

<c:choose>
	<c:when test="${op == 'add'}">
		<c:set var="opTitle" value="Add" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${projPath}/pbis/new" />
	</c:when>
	<c:when test="${op == 'edit'}">
		<c:set var="opTitle" value="Edit" />
		<c:set var="httpMethod" value="post" />
		<c:set var="actionURI" value="${projPath}/pbis/${pbi.idPBI}/edit" />
	</c:when>
</c:choose>

<h1>${opTitle} PBI</h1>
<form:form method="${httpMethod}" action="${actionURI}"
	id="${op}PbiForm" class="form" commandName="pbi">
	<security:authorize access="hasRole('ROLE_PRODUCT_OWNER')">
		<h2>PBI details</h2>
		<div class="ninecol">
			<div class="outer-input">
				<div class="inner-input">
					<form:label path="title">Title</form:label>
					<form:textarea path="title" rows="2" cols="100" id="pbiFormTitle" />
					<form:errors cssClass="error" path="title" />
				</div>
			</div>
		</div>
		<div class="threecol last">
			<form:label path="type">Type</form:label>
			<form:select path="type" items="${types}" itemLabel="value"
				id="pbiFormType" />
		</div>
		<div class="twelvecol clear last">
			<div class="outer-input">
				<div class="inner-input">
					<form:label path="description">Description</form:label>
					<form:textarea path="description" rows="4" cols="100"
						id="pbiFormDescription" />
				</div>
			</div>
		</div>
		<div class="threecol clear">
			<div class="outer-input">
				<form:label path="dateCreation">Created on</form:label>
				<br /> <span class="ui-icon ui-icon-calendar left"></span>
				<div class="inner-input date">
					<form:input path="dateCreation" readonly="${true}"
						id="pbiFormDateCreation" class="controls disabled" />
				</div>
			</div>
		</div>
		<div class="threecol clear">
			<div class="outer-input">
				<div class="inner-input">
					<form:label path="priority">Priority</form:label>
					<form:input path="priority" id="pbiFormPriority" />
					<form:errors cssClass="error" path="priority" />
				</div>
			</div>
		</div>
	</security:authorize>
	<div class="threecol last">
		<div class="outer-input">
			<div class="inner-input">
				<form:label path="workItems[0].storyPoints">Story points</form:label>
				<br />
				<form:input path="workItems[0].storyPoints" id="pbiFormStoryPoints" />
				<form:errors cssClass="error" path="workItems[0].storyPoints" />
			</div>
		</div>
	</div>
	<c:if test="${not empty attributes}">
		<div class="twelvecol clear last spacerT">
			<h2>Project specific attributes</h2>
			<c:forEach var="atrib" items="${attributes}" varStatus="aaa">
				<div class="fourcol last clear">
					<div class="outer-input">
						<div class="inner-input">
							<c:choose>
								<c:when test="${atrib.type.value == 'String'}">
									<form:label path="formStringAttributes['${atrib.name}']">${atrib.name} <span
											class="smallInfo">(${atrib.type})</span>
									</form:label>
									<form:input path="formStringAttributes['${atrib.name}']"
										id="pbiFormCustomAttr-${atrib.camelName}" />
								</c:when>
								<c:when test="${atrib.type.value == 'Double'}">
									<form:label path="formDoubleAttributes['${atrib.name}']">${atrib.name} <span
											class="smallInfo">(${atrib.type})</span>
									</form:label>
									<form:input path="formDoubleAttributes['${atrib.name}']"
										id="pbiFormCustomAttr-${atrib.camelName}" />
								</c:when>
							</c:choose>
						</div>
					</div>
				</div>
			</c:forEach>
			<form:errors cssClass="error" path="formDoubleAttributes" />
			<form:errors cssClass="error" path="formStringAttributes" />
		</div>
	</c:if>
	<div class="spacerT clear twelvecol">
		<h2>Work status</h2>
		<div class="fourcol clear">
			<form:label path="workItems[0].team">Team</form:label>
			<br />
			<form:select path="workItems[0].team" name="team" id="pbiFormTeam">
				<form:option value="">-</form:option>
				<form:options items="${teams}" itemLabel="publicName"
					itemValue="idTeam" />
			</form:select>
		</div>
		<div class="fourcol">
			<form:label path="workItems[0].sprint">Sprint</form:label>
			<br />
			<form:select path="workItems[0].sprint" name="sprint"
				id="pbiFormSprint">
				<form:option value="">-</form:option>
				<c:if test="${not empty sprintsDisabled}">
					<form:options items="${sprintsDisabled}" itemLabel="name"
                    itemValue="idSprint" cssClass="dis"/>
				</c:if>
				<form:options items="${sprints}" itemLabel="name"
					itemValue="idSprint" />
			</form:select>
		</div>
		<div class="fourcol last">
			<form:label path="workItems[0].status">Status</form:label>
			<br />
			<form:select path="workItems[0].status" name="workStatus"
				id="pbiFormWorkStatus">
				<c:forEach var="status" items="${statuses}">
					<c:choose>
						<c:when test="${availableStasuses.contains(status)}">
							<form:option value="${status.valueUpperCase}"
								label="${status.value }" />
						</c:when>
						<c:when test="${not availableStasuses.contains(status)}">
							<form:option value="${status.valueUpperCase}"
								label="${status.value }" cssClass="dis" />
						</c:when>
					</c:choose>
				</c:forEach>
			</form:select>
		</div>
		<div class="clear twelvecol last">
			<form:errors cssClass="error" path="workItems" />
		</div>
	</div>
	<input type="submit" class="controls right clear spacerT" value="Save"
        id="submitButton">
	
</form:form>
