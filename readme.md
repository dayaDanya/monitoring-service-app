Требования к запуску: 
- jdk 17
    
- docker engine(docker compose v2)

- компилятор ajc

Запуск:

- для запуска приложения, нужно предварительно запустить docker-compose.yml файл.
- приложение запускается на сервере Tomcat 10.1.18

Взаимодействие: 

- регистрация: POST /registration  
  json:
    - { "email" : "string",

      "password" : "string" }
- авторизация: POST /authentication  
    json: 
  - { "email" : "string",
  
    "password" : "string" }
- добавление измерения : POST /measurements
    
    json: 
     -    {"amount" : double, 
  "counterType" : "HOT"/"COLD"/"HEAT"}
- просмотр всех своих измерений:  GET /measurements?action=all
- просмотр последнего измерения для выбранного типа счетчика: 
GET /measurements?action=last&type=(HOT/COLD/HEAT)
- просмотр измерения за конкретный месяц для выбранного типа счетчика:
GET /measurements?action=month&number=(1..12)&type=(HOT/COLD/HEAT)
- просмотр своих счетчиков: GET /measurements?action=counters
- админ: просмотр всех измерений: GET /admin-panel/measurements
- админ: просмотр аудита: GET /admin-panel/operations
- админ: добавление нового типа счетчика: POST /admin-panel/counters
  json:
    - { "name" : "string"
  }
- админ: выдача нового счетчика пользователю: POST /admin-panel/counter-types
- json:
    - { "personId" : "string",
        "counterType" : "string"
      }
 
- стандартные типы счетчиков: HOT, COLD, HEAT
- email и пароль админ аккаунта: admin и admin.
