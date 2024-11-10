<h1 align="center">Система управления новостями</h1>

<details>
 <summary><strong>
  Техническое задание
</strong></summary>

#### ЗАДАНИЕ:

* Разработать RESTful платформу, реализующую функционал для работы с системой управления новостями.
</details>

<details>
 <summary><strong>
  Стек
</strong></summary>

#### При разработке были использованы:

* Java 21
* Gradle 8.5
* Spring Boot 3.x
* PostgreSQL
* Mapstruct
* Jsonwebtoken
* Liquibase
* Testcontainers
* Junit 5
* SpringDoc Open API

</details>

<details>
 <summary><strong>
  Запуск проекта
</strong></summary>

* Скачайте проект с gitHub:
    * https://github.com/Nikolay1992167/NMS/tree/feature/userservice
    * 
#### Запуск приложения локально(dev profile):

1. На машине должна быть установлена Java 21,
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   [Postgresql](https://www.postgresql.org/download/) и [Redis](https://redis.io/) установлены
   (P.S.: вы также можете развернуть postgresql и redis в [DockerPostgres](https://hub.docker.com/_/postgres) и
   [DockerRedis](https://hub.docker.com/_/redis) контейнерах).
2. Вам нужен [Docker](https://www.docker.com/products/docker-desktop/) для интеграционных тестов с использованием тест контейнеров.
3. В PostgreSQL вам нужно создать базу данных users. При запуске приложения с помощью docker будет выполнен [db](db/db.sql).
4. Первоначально нужно развернуть стартеры логгирования и обработки ошибок с помощью команд publishingToMavenLocal.
5. Затем нужно запустить spring-cloud-service, который предоставляет application.yml согласно профиля.
6. В news-service-application.yaml установите профиль dev в строке №5 и укажите localhost для правильного запуска сервиса,
   и user-service-application.yaml также установите dev в строкеe №5.
7. Теперь можно запустить user-service и news-service.
   Liquibase создаст таблицы и заполнит их данными согласно скриптам.
8. Приложение готово к работе.

#### Запуск приложения в Docker(prod profile):

1. Необходимо выплнить build сервисов.
2. Затем запустить выполнение docker-compose.yaml в файле или с помощью команды ```docker compose up```
</details>

<details>
 <summary><strong>
  Тесты
</strong></summary>

* В сервисах реализованы модульные и интеграционные тесты. 
* Также реализованы тесты для стартера логирования.
* Запустить тесты можно с помощью команды ```./gradlew test```.
</details>

<details>
 <summary><strong>
  Документация и функциональность
</strong></summary>

* Вы можете просмотреть документацию API Swagger, выполнив команды:

* [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)
* [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
</details>