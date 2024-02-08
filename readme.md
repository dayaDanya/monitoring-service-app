Требования к запуску: 
- jdk 17
    
- docker engine(docker compose v2)

Запуск:

- В случае, если проект запускается на ОС Windows, 
требуется изменить в файле src/main/java/org/ylab/infrastructure/in/db/ConnectionAdapter.java путь, 
а именно путь к файлу application.properties, нужно заменить "/" на "\\"
- для запуска приложения, нужно предварительно запустить docker-compose.yml файл.

Взаимодействие: 
- осуществляется через консоль.
    
- стандартные типы счетчиков: HOT, COLD, HEAT
- email и пароль админ аккаунта: admin и admin.
