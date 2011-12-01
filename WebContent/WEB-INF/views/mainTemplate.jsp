<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<title><tiles:getAsString name="title" /> | scrumzu</title>
<link rel="shortcut icon" href="/scrumzu/resources/img/favico.ico" />
<link type="text/css" href="/scrumzu/resources/css/reset.css"
	rel="stylesheet" />
<link rel="stylesheet" href="/scrumzu/resources/css/1140.css"
	type="text/css" media="screen" />
<link type="text/css"
	href="/scrumzu/resources/css/scrumzu/jquery-ui-1.8.16.custom.css"
	rel="stylesheet" />
<!--[if lte IE 9]>
<link rel="stylesheet" href="/scrumzu/resources/css/ie.css" type="text/css" media="screen" />
<![endif]-->

<!-- The 1140px Grid - http://cssgrid.net/ -->
<link rel="stylesheet" href="/scrumzu/resources/css/styles.css"
	type="text/css" media="screen" />

<script type="text/javascript"
	src="/scrumzu/resources/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jquery-json-2.3.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/dropdown-menu.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/gui-controls.js"></script>
<script type="text/javascript"
	src="/scrumzu/resources/js/jquery.datables-pagination.js"></script>
<tiles:useAttribute id="scripts" name="jsScripts"
	classname="java.util.List" />
<c:forEach var="script" items="${scripts}">
	<tiles:insertAttribute value="${script}" flush="true" />
</c:forEach>
</head>
<body>
	<div class="container">
		<tiles:insertAttribute name="errorPanel" />
		<div class="row" id="header">
			<a href="/scrumzu/"><img src="/scrumzu/resources/img/logo.png" /></a>
			<tiles:insertAttribute name="loginForm" />
			<tiles:insertAttribute name="mainMenu" />
		</div>
		<tiles:insertAttribute name="actionMenu" />
		<tiles:insertAttribute name="contentColumns" />
		<div class="push"></div>
	</div>
	<div class="row" id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>