### MealRestController IDEA Test RESTful Web Service

1. curl -X GET --location "http://localhost:8080/topjava_war_exploded/rest/meals"
2. curl -X POST --location "http://localhost:8080/topjava_war_exploded/rest/meals" \
   -H "Content-Type: application/json" \
   -d '{
   "dateTime" : "2020-01-18T18:00",
   "description" : "Созданный ужин",
   "calories" : 300
   }'
3. curl -X GET --location "http://localhost:8080/topjava_war_exploded/rest/meals/between?startDate=2020-01-30&startTime=10:00&endDate=2020-01-30&endTime=10:10"
4. curl -X GET --location "http://localhost:8080/topjava_war_exploded/rest/meals/100007"
5. curl -X PUT --location "http://localhost:8080/topjava_war_exploded/rest/meals/100007" \
   -H "Content-Type: application/json" \
   -d '{
   "dateTime" : "2020-01-20T18:00",
   "description" : "Обновленный завтрак",
   "calories" : 200,
   "id" : 100007
   }'
6. curl -X DELETE --location "http://localhost:8080/topjava_war_exploded/rest/meals/100007"
