const mealsAjaxUrl = "meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl,
    datatableApi: null,
    updateTable: () => {
        const searchParams = new URLSearchParams()
        const params = $("#form").serializeArray();
        params.forEach(({name, value}) => {
            searchParams.set(name, value);
        });
        const url = `${ctx.ajaxUrl}filter?${searchParams.toString()}`;
        $.get(url, function (data) {
            updateRow(ctx.datatableApi, data)
        });
    }
};

// $(document).ready(function () {
$(function () {
    const table = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                data: "dateTime",
            },
            {
                data: "description"
            },
            {
                data: "calories"
            },
            {
                orderable: false,
                defaultContent: "Edit"
            },
            {
                orderable: false,
                defaultContent: "Delete"
            }
        ],
        order: [[0, "desc"]]
    });
    ctx.datatableApi = table
    makeEditable(table);
});

$(function () {
    $("#dateTime").datetimepicker({
        format: 'Y-m-d H:i',
    });
    $("#startDate, #endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
    });
    $("#startTime, #endTime").datetimepicker({
        datepicker: false,
        format: 'H:i',
        step: 5,
        scrollInput: false
    });
});

function cancelFilter() {
    $("#form").find(":input").each(function () {
        $(this).val("");
    });
    $.get(ctx.ajaxUrl + "all", function (data) {
        updateRow(ctx.datatableApi, data)
    });
}