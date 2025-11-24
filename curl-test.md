### MealRestController IDEA Test RESTful Web Service

1. curl -X GET --location "http://localhost/rest/meals"
2. curl -X POST --location "http://localhost/rest/meals" \
   -H "Content-Type: application/json" \
   -d '{
   "dateTime" : "",
   "description" : "",
   "calories" : 0,
   "user" : { },
   "id" : 0
   }'
3. curl -X GET --location "http://localhost/rest/meals/between?startDate=string&startTime=string&endDate=string&endTime=string"
4. curl -X GET --location "http://localhost/rest/meals/0"
5. curl -X PUT --location "http://localhost/rest/meals/0" \
   -H "Content-Type: application/json" \
   -d '{
   "dateTime" : "",
   "description" : "",
   "calories" : 0,
   "user" : { },
   "id" : 0
   }'
6. curl -X DELETE --location "http://localhost/rest/meals/0"
