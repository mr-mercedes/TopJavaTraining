const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
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

function toggleEnabled(el, id) {
    const url = `rest/${ctx.ajaxUrl}${id}`
    const needToOn = !Boolean(el.closest('input').attr("checked"))
    if (needToOn) {
        enable(url);
    } else {
        disable(url);
    }
}

function enable(url) {
    $.post({url, data: {enable: true}}).done(function () {
        updateTable();
        successNoty("User enable");
    })
}

function disable(url) {
    $.post({url, data: {enable: false}}).done(function () {
        updateTable();
        successNoty("User disable");
    })
}
