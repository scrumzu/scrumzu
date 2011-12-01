<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="loginPanel" class="right last">
	<security:authorize access="isAuthenticated()">
		<security:authentication property="principal.username" var="userLogin" />
		<security:authentication property="principal.authorities"
			var="userAuthorities" />
		<p>You are logged as <b>${userLogin}</b><a href="/scrumzu/static/j_spring_security_logout" class="spacerL">Logout</a></p>
	</security:authorize>
	<security:authorize access="not isAuthenticated()">
		<spring:url var="authUrl" value="/static/j_spring_security_check" />
		<form:form method="POST" action="${authUrl}" class="form"
			id="loginForm">
			<div class="sixcol">
				<div class="outer-input">
					<label>Login</label> <br />
					<div class="inner-input">
						<input type="text" name="j_username" id="loginInput" value="">
					</div>
				</div>
			</div>
			<div class="sixcol last">
				<div class="outer-input">
					<label>Password</label> <br />
					<div class="inner-input">
						<input type="password" name="j_password" id="passwordInput" />
					</div>
				</div>
			</div>
			<div class="twelvecol clear last">
				<input type="submit" class="controls right" value="Login"
					id="loginButton" name="submit">
			</div>
		</form:form>
	</security:authorize>
</div>