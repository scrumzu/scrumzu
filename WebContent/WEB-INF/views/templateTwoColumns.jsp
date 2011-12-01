<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<div class="row" id="contentBody">
	<div class="threecol" id="subMenu">
		<tiles:insertAttribute name="subMenu" />
	</div>
	<div class="ninecol last" id="content">
		<tiles:insertAttribute name="content" />
	</div>
</div>