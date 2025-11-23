### MealRestController IDEA Test RESTful Web Service

1. get()
   ``@id=100003
   GET http://localhost:8080/topjava_war_exploded/rest-meals/{{id}}``
2. delete() ``@id=100003
   DELETE http://localhost:8080/topjava_war_exploded/rest-meals/{{id}}``
3. getAll() ``GET http://localhost:8080/topjava_war_exploded/rest-meals/
   Content-Type: application/json``
4. createWithLocation() ``POST http://localhost:8080/topjava_war_exploded/rest-meals/
   Content-Type: application/json
   {
   "dateTime": "2020-01-18T18:00",
   "description": "Созданный ужин",
   "calories": 300
   }``
5. update() ``@id=100007
   PUT http://localhost:8080/topjava_war_exploded/rest-meals/{{id}}
   Content-Type: application/json
   {
   "dateTime": "2020-01-20T18:00",
   "description": "Обновленный завтрак",
   "calories": 200,
   "id" : 100007
   }``
6. getBetweenFilter() ``@startDate=2020-01-30
   @startTime=10:00:10
   @endDate=2020-01-30
   @endTime=10:10:00
   GET http://localhost:8080/topjava_war_exploded/rest-meals/between?
   startDate={{startDate}}&
   startTime={{startTime}}&
   endDate={{endDate}}&
   endTime={{endTime}}
   Content-Type: application/json``
