<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.ru/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<style>
    <%@include file="/WEB-INF/css/style.css" %>
</style>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<div class="from-wrapper">
    <%
        String action = request.getParameter("action");
        boolean closeId = true;
        if (action.equals("create")) {
            closeId = false;
    %>
    <h2>Create meal</h2>
    <% } else {
    %>
    <h2>Edit meal</h2>
    <%
        }
    %>
    <form class="form" method="post" action="meals" name="form-add-meal">
        <% if (closeId) { %>
        <label>
            <span>Meal ID:</span>
            <input
                    class="form-input"
                    id="meal-id"
                    type="text"
                    name="mealId"
                    readonly="readonly"
                    value="<c:out value="${meal.id}" />"/></label>

        <% } %>
        <label>
            <span>DateTime:</span>
            <input
                    class="form-input"
                    id="date-time"
                    type="datetime-local"
                    name="dateTime"
                    value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}"/></label>


        <label>
            <span>Description:</span>
            <input
                    class="form-input"
                    id="description"
                    type="text"
                    name="description"
                    value="<c:out value="${meal.description}" />"/></label>


        <label>
            <span>Calories:</span>
            <input
                    class="form-input"
                    id="calories"
                    type="number"
                    name="calories"
                    value="<c:out value="${meal.calories}" />"/>
        </label>


        <div class="form-btn">
            <button type="submit">Save</button>
            <button type="reset" onclick="window.history.back()">Cancel</button>
        </div>
    </form>
</div>
</body>
</html>
