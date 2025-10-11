<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .wrapper {
            width: 100%;
            padding: 2rem 1rem;
        }

        .container {
            box-sizing: border-box;
            max-width: 1140px;
            padding: 0 15px 0 15px;
            margin: 0 auto 0 auto;
        }

        .form {
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            min-width: 0;
            word-wrap: break-word;
        }
        .some {
            display: block;
            margin-top: 0;
            unicode-bidi: isolate;
            margin-block-end: 1em;
        }

        .form-hero {
            flex: 1 1 auto;
            min-height: 1px;
            padding: 1.25rem 1.25rem 0 1.25rem;
        }

        .row {
            box-sizing: border-box;
            display: flex;
            flex-wrap: wrap;
            margin: 0 -15px 0 -15px;
        }

        .col {
            flex: 0 0 16.6666%;
            max-width: 16.6666%;
        }

        .footer-hero {
            text-align: end;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <div class="wrapper">
        <div class="container">
            <h2>Meals</h2>
            <div class="form">
                <div class="form-hero">
                    <form method="get" action="meals" id="filter" class="some">
                        <div class="row">
                            <div class="col">
                                <label for="startDate">От даты (включая)</label>
                                <input id="startDate" type="date" value="${filter.startDate}" name="startDate">
                            </div>

                            <div class="col">
                                <label for="endDate">До даты (включая)</label>
                                <input id="endDate" type="date" value="${filter.endDate}" name="endDate">
                            </div>

                            <div class="col">
                                <label for="startTime">От времени (включая)</label>
                                <input id="startTime" type="time" value="${filter.startTime}" name="startTime">
                            </div>

                            <div class="col">
                                <label for="endTime">До времени (исключая)</label>
                                <input id="endTime" type="time" value="${filter.endTime}" name="endTime">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="footer-hero">
                    <button form="filter" type="reset">Отменить</button>
                    <button form="filter" type="submit" name="action" value="filter">Отфильтровать</button>
                </div>
            </div>
            <a href="meals?action=create">Add Meal</a>
            <br><br>
            <table border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Calories</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.meals}" var="meal">
                    <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                    <tr class="${meal.excess ? 'excess' : 'normal'}">
                        <td>
                                <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                                <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                                ${fn:formatDateTime(meal.dateTime)}
                        </td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                        <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</section>
</body>
</html>