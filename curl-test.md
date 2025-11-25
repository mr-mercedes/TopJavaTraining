# MealRestController API Documentation

## Overview

`MealRestController` предоставляет REST-интерфейс для управления приёмами пищи (`Meal`) текущего авторизованного
пользователя.

Базовый путь контроллера:

```
/rest/meals
```

Контроллер поддерживает операции CRUD и фильтрацию данных по дате и времени.

---

## Endpoints

### 1. Получить все приёмы пищи

**GET /rest/meals**  
Возвращает список всех приёмов пищи (`List<MealTo>`).

Пример:

```sh
curl -X GET "http://localhost:8080/topjava_war_exploded/rest/meals"
```

---

### 2. Создать новый приём пищи

**POST /rest/meals**  
Создаёт объект `Meal`, возвращает `201 Created` и `Location`.

Пример:

```sh
curl -X POST "http://localhost:8080/topjava_war_exploded/rest/meals"   -H "Content-Type: application/json"   -d '{
    "dateTime": "2020-01-18T18:00",
    "description": "Созданный ужин",
    "calories": 300
  }'
```

---

### 3. Получить приёмы пищи по диапазону даты и времени

**GET /rest/meals/between**  
Фильтрует записи по `startDate/startTime` и `endDate/endTime`.

Пример:

```sh
curl -X GET   "http://localhost:8080/topjava_war_exploded/rest/meals/between?startDate=2020-01-30&startTime=10:00&endDate=2020-01-30&endTime=10:10"
```

---

### 4. Получить приём пищи по ID

**GET /rest/meals/{id}**

Пример:

```sh
curl -X GET   "http://localhost:8080/topjava_war_exploded/rest/meals/100007"
```

---

### 5. Обновить приём пищи по ID

**PUT /rest/meals/{id}**

Пример:

```sh
curl -X PUT   "http://localhost:8080/topjava_war_exploded/rest/meals/100007"   -H "Content-Type: application/json"   -d '{
    "dateTime": "2020-01-20T18:00",
    "description": "Обновленный завтрак",
    "calories": 200,
    "id": 100007
  }'
```

---

### 6. Удалить приём пищи по ID

**DELETE /rest/meals/{id}**

Пример:

```sh
curl -X DELETE   "http://localhost:8080/topjava_war_exploded/rest/meals/100007"
```

---

## Data Models

### Meal

```json
{
  "id": 100007,
  "dateTime": "2020-01-20T18:00",
  "description": "Завтрак",
  "calories": 500
}
```

### MealTo

```json
{
  "id": 100007,
  "dateTime": "2020-01-20T18:00",
  "description": "Завтрак",
  "calories": 500,
  "excess": false
}
```

