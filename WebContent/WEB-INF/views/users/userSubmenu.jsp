<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="insidecol ui-state-highlight left">
	<div id="statusesExplanation">
		<h3>
			<span class="left ui-icon ui-icon-info"></span>User authorities
		</h3>
		<div id="submenuInfo" class="hidden">
			<p>
				There are three main authorities which are directly connected with <i>Scrum</i>
				methodology:
			</p>
			<ul class="clear list" id="authoritiesExplanationList">
				<li><b>Product Owner</b> - user responsible for project and
					it's realization. He can manage PBIs, releases and sprints.</li>
				<li><b>Scrum Master</b> - user responsible for teams management
					and work progress logging.</li>
				<li><b>Team Member</b> - member of a team involved in project
					developement. He can only view information, without rights to edit
					them.</li>
			</ul>
			<br />
			<p>
				Also, there is an <b>Admin</b> authority, which allows
				administrators to manage logins, passwords and authorities of other
				users.
			</p>
		</div>
		<a href="#" id="submenuInfoToggle">Show details</a>
	</div>
</div>
