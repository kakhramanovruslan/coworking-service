## Coworking-Service 
Приложение для управления коворкинг-пространством. Приложение позволяет пользователям бронировать рабочие места, конференц-залы, а также управлять бронированиями и просматривать доступность ресурсов.

## Функциональность:
- регистрация и авторизация пользователя;
- просмотр списка всех доступных рабочих мест и конференц-залов;
- просмотр доступных слотов для бронирования на конкретную дату;
- бронирование рабочего места или конференц-зала на определённое время и дату;
- отмена бронирования;
- добавление новых рабочих мест и конференц-залов, а также управление существующими;
- просмотр всех бронирований и их фильтрация по дате, пользователю или ресурсу.

## Запуск приложения:
1. Склонируйте репозиторий на вашем компьютере.
2. Откройте проект в IntelliJ IDEA.
3. Запустите Tomcat 10 версии для начала взаимодействия с приложением в консоли.
4. В файл catalina.bat (windows) вставьте путь к библиотеке aspectjweawer. Пример: 
  set "CATALINA_OPTS=%CATALINA_OPTS% -javaagent:C:\Users\User\.m2\repository\org\aspectj\aspectjweaver\1.9.22\aspectjweaver-1.9.22.jar"

## Логин и пароль для администратора
- **Логин:** admin
- **Пароль:** admin


***

## API

***
### Endpoint: `/auth/login`

**Method:** `POST`

**Description:** Handles authentication requests.

### Request

**Content-Type:** `application/json`

**Body:**
```json
{
  "username": "string",
  "password": "string"
 }
```

### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
{
  "token": "string"
 }
```

**Error Response:**
- Invalid Arguments or Authentication Failure
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`



***



### Endpoint: `/auth/registration`

**Method:** `POST`

**Description:** Handles registration requests.

### Request

**Content-Type:** `application/json`

**Body:**
```json
{
  "username": "string",
  "password": "string"
 }
```

### Response

**Success Response:**
- Code: `201 Created`
- Content-Type: `application/json`
- **Body:**
```json
{
  "message": "string"
 }
```

**Error Response:**
- Invalid Arguments or Registration Failure
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`



***



### Endpoint: `/workspaces/book`

**Method:** `POST`

**Description:** Handles booking workspaces requests.

### Request

**Content-Type:** `application/json`

**Date-Format:** `yyyy-MM-dd'T'HH:mm:ss`

**Body:**
```json
{
  "workspaceName": "string",
  "startTime": "LocalDateTime",
  "endTime": "LocalDateTime"
 }
```

### Response

**Success Response:**
- Code: `201 Created`
- Content-Type: `application/json`
- Date-Format: `yyyy-MM-dd'T'HH:mm:ss`
- **Body:**
```json
{
  "id": "Long",
  "workspaceId": "Long",
  "userId": "Long",
  "startTime": "LocalDateTime",
  "endTime": "LocalDateTime"
 }
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace/User Not Found, Workspace Already Booked
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`




***




### Endpoint: `/workspaces/bookings`

**Method:** `GET`

**Description:** Handles getting booked workspaces requests.

### Request

| Parameter | Type   | Description                                                                                            |
|-----------|--------|--------------------------------------------------------------------------------------------------------|
| startTime | String | Start time in ISO_LOCAL_DATE_TIME format. Used in conjunction with endTime to filter bookings by time. |
| endTime   | String | End time in ISO_LOCAL_DATE_TIME format. Used in conjunction with startTime to filter bookings by time. |
| username  | String | Username to filter bookings by user.                                                                   |
| name      | String | Workspace name to filter bookings by workspace.                                                        |

> > **Note:** You can use only one of the following filter parameters at a time: `startTime` + `endTime`, `username`, or `name`. If more than one filter is provided, the server will return an error.
> >

### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
[
    {
        "id": 1,
        "username": "ruslan",
        "workspaceName": "MeetingRoom1",
        "startTime": "2023-07-01T09:00:00",
        "endTime": "2023-07-01T11:00:00"
    },
    {
        "id": 2,
        "username": "vasiliy",
        "workspaceName": "ConferenceRoom2",
        "startTime": "2023-07-02T10:00:00",
        "endTime": "2023-07-02T12:00:00"
    }
 ]
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace/User Not Found
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`



***




### Endpoint: `/workspaces`

**Method:** `GET`

**Description:** Retrieve a list of all workspaces.

### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
[
    {
        "id": 1,
        "name": "MeetingRoom1"
    },
    {
        "id": 2,
        "name": "ConferenceRoom2"
    }
 ]
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`


***



### Endpoint: `/workspaces/available-for-period`

**Method:** `GET`

**Description:** Retrieve a list of available workspaces for the specified time period.

### Request

| Parameter | Type   | Description                               |
|-----------|--------|-------------------------------------------|
| startTime | String | Start time in ISO_LOCAL_DATE_TIME format. |
| endTime   | String | End time in ISO_LOCAL_DATE_TIME format.   |

> > **Note:** startTime and endTime parameters are required. If they are not provided or the format is invalid, the server will return an error.
> >

### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
[
    {
        "id": 1,
        "name": "MeetingRoom1"
    },
    {
        "id": 2,
        "name": "ConferenceRoom2"
    }
]
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`





***


### Endpoint: `/workspaces/available`

**Method:** `GET`

**Description:** Retrieve a list of available workspaces at the current time.

### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
[
    {
        "id": 1,
        "name": "MeetingRoom1"
    },
    {
        "id": 2,
        "name": "ConferenceRoom2"
    }
]
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`



***



### Endpoint: `/workspace`

**Method:** `GET`

**Description:** Retrieve a workspace by specifying either its ID or name.


### Request

| Parameter | Type   | Description            |
|-----------|--------|------------------------|
| id        | Long   | ID of the workspace.   |
| name      | String | Name of the workspace. |

> > **Note:** Either id or name parameter is required. If both are provided, the server will return an error.
> >
### Response

**Success Response:**
- Code: `200 OK`
- Content-Type: `application/json`
- **Body:**
```json
    {
        "id": 1,
        "name": "MeetingRoom1"
    }
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace Not Found
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`




***





### Endpoint: `/admin/workspaces`

**Method:** `POST`

**Description:** Create a new workspace.

### Request

**Content-Type:** `application/json`

**Body:**
```json
{
  "name": "string"
 }
```

### Response

**Success Response:**
- Code: `201 Created`
- Content-Type: `application/json`
- **Body:**
```json
{
  "id": "Long",
  "name": "string"
 }
```

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace Already Exist
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`



***




### Endpoint: `/admin/workspaces?name=MeetingRoom1`

**Method:** `PUT`

**Description:** Update existing workspace.

### Request

**Content-Type:** `application/json`

**Body:**
```json
{
  "name": "string"
 }
```

### Response

**Success Response:**
- Code: `200 OK`

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace Already Exist, Workspace Not Found
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`





***





### Endpoint: `/admin/workspaces?name=MeetingRoom1`

**Method:** `DELETE`

**Description:** Delete workspace.

### Response

**Success Response:**
- Code: `200 OK`

**Error Response:**
- Access Denied
  - Code: `403 Forbidden`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Validation Errors, Workspace Not Found
  - Code: `400 Bad Request`
  - Content-Type: `application/json
  - **Body:**
  ```json
  {
    "message": "string"
   }
  ```
- Server Error
  - Code: `500 Internal Server Error`

 ***


































#### Связаться со мной:
- kahramanovruslan085@gmail.com
- telegram: @kakhramanovruslan
