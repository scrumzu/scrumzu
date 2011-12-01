<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="insidecol ui-state-highlight left">
	<div id="statusesExplanation">
		<h3>
			<span class="left ui-icon ui-icon-info"></span>Project specific attributes
		</h3>
		<div id="submenuInfo" class="hidden">
			<p>As a Product Owner you can easily customize presentation of PBIs
				in particular project. You can add additional, specific for every project,
				attributes of a type:</p>
			<ul class="clear list" id="attributesExplanationList">
				<li><b>String</b> - text attribute, digits and special
					characters allowed.</li>
				<li><b>Double</b> - numeric attribute.</li>
			</ul>
			<br />
		</div>
		<a href="#" id="submenuInfoToggle">Show details</a>
	</div>
</div>
