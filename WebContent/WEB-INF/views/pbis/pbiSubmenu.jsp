<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="insidecol ui-state-highlight left">
	<div id="statusesExplanation">
		<h3>
			<span class="left ui-icon ui-icon-info"></span>PBI work statuses
		</h3>
		<div id="submenuInfo" class="hidden">
			<ul class="clear list" id="statusesExplanationList">
				<li><b>New</b> - PBI recently added, a default state.</li>
				<li><b>Proposed for sprint</b> - PBI proposed for 
					an implementation in a given sprint. 
					Set without choosing a team to work on it.</li>
				<li><b>Preassigned</b> - PBI preassigned for a team to 
					work on it in a given sprint.-.</li>
				<li><b>Committed</b> - confirms a team commitment in a given
					sprint.</li>
				<li><b>Work in progress</b> - PBI is under
					development.</li>
				<li><b>Done</b> - PBI has been already implemented 
					and is being tested.</li>
				<li><b>Dropped</b> - PBI excluded from development in a given
					sprint.</li>
			</ul>
			<br />
			<p>Note that setting the PBI's status without choosing any 
			team or sprint can be managed only by Product Owner.</p>
		</div>
		<a href="#" id="submenuInfoToggle">Show
			details</a>
	</div>
</div>
