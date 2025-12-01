const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function toggleEnabled(cb, id) {
    const url = `${ctx.ajaxUrl}${id}`
    const oldState = cb.checked;

    $.ajax({
        type: "PATCH",
        url: url + "?enable=" + oldState
    }).done(function () {
        successNoty("User " + (oldState ? "enabled" : "disabled"));
        $(`#${id}`).attr('data-user-enabled', oldState);
    }).fail(function (jqXHR) {
        failNoty(jqXHR);
        cb.checked = !oldState;
    });
}
