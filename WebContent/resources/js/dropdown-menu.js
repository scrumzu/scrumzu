$(document).ready(function() {
	$("ul.subnav").parent().hover(function() {
		$(this).find("ul.subnav").fadeIn('fast').show();

		$(this).hover(function() {
		}, function() {
			$(this).find("ul.subnav").fadeOut('fast');
		});
	})
	.append("<span></span>");
});