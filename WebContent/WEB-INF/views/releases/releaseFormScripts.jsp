<script type="text/javascript">
	$(function() {
		$('input[name=pbis]:checkbox').change();
        $('input[name=pbis]:checkbox').button('refresh');
		initShowInfo();
        initDataTable();
        initSearch();
	});
</script>
<script type="text/javascript">
    function initDataTable() {
        table = $('#pbisInReleaseFormTable')
                .dataTable(
                        {
                            "sDom" : '<<"eightcol right last"f><t><"threecol clear"li><"ninecol last"p>>',
                            "bDestroy" : true,
                            "sPaginationType" : "links",
                            "oSearch" : {
                                "sSearch" : "",
                                "bRegex" : false,
                                "bSmart" : false
                            },
                            "oLanguage" : {
                                "sInfoEmpty" : "No PBIs found.",
                                "sEmptyTable" : "No PBIs found."
                            },
                        });
    }
    function initSearch() {
        columns = {
            "_all" : "All columns"
        };
        $.each(table.fnSettings().aoColumns, function(index, col) {
            if (col.sTitle != '') {
                columns[index] = col.sTitle;
            }
        });
        $("#pbisInReleaseFormTable_filter").append(
                createSelectInput(columns, "id", "searchByColumnSelect"));

        $("#pbisInReleaseFormTable_filter input").keyup(function() {
            var c = $("#searchByColumnSelect").val();
            if (c != "_all") {
                table.fnFilter(this.value, c);
            } else {
                table.fnFilter(this.value);
            }
        });

        $("#searchByColumnSelect").change(function() {
            var c = $("#searchByColumnSelect").val();
            var text = $("#pbisInReleaseFormTable_filter input").val();

            $.each(table.fnSettings().aoColumns, function(index, col) {
                if (index != c) {
                    table.fnFilter("", index);
                }
            });
            if (c != "_all") {
                table.fnFilter(text, c);
            } else {
                table.fnFilter(text);
            }
        });
    };
</script>