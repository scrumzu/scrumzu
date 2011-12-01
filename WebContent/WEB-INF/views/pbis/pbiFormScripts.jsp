<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
    $(function() {
        var sprintLastSel = $("#pbiFormSprint").val();
        var statusLastSel = $("#pbiFormWorkStatus").val();

        $("#pbiFormSprint").change(function() {
            if ($(this).find("option:selected").hasClass('dis')) {
                $(this).val(sprintLastSel);
            } else {
                sprintLastSel = $(this).find("option:selected").val();
            }
        });

        $("#pbiFormWorkStatus").change(function() {
            if ($(this).find("option:selected").hasClass('dis')) {
                $(this).val(statusLastSel);
            } else {
            	statusLastSel = $(this).find("option:selected").val();
            }
        });
    });
</script>

