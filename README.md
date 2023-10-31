# job4j_cinema
Проект "Кинотеатр"

[![github actions][actions-image]][actions-url]
[![coverage][codecov-image]][codecov-url]

Веб приложение на языке Java с библиотекой Spring boot.

Сайт по покупке билетов в кинотеатр.
Главная страница форма выбор фильма. Пользователь кликает на фильм и переходит в бронирование.
Данные при переходе записываются в HttpSession. При бронирование показывается список доступных рядов и кнопка далее. Далее, отображаем список с выбором доступных мест и кнопка забронировать.
Последнее окно содержит информацию о бронирование билета.
Пользователь может не забронировать билет, потому что его выбрал другой пользователь. То есть одновременно выбрали одинаковые места.
Доступна регистрация и авторизация пользователей, а также добавление сеансов кинотеатра.

### Используемые технологии
![tech-1.png](readme/images/tech-1.png)

### Сквозная функциональность реализована при помощи AOP Spring
- Идентификация ролей пользователей
- Журналирование сервисов
- Обработка исключений 
- Функция защиты: валидация входных данных пользователя
- Мониторинг сервисов и времени выполнения событий

### Архитектура приложения трехслойное
- Слой контроллеры
- Слой сервисы
- Слой работы с БД

### Требуемое окружение
- JDK 17
- Apache Maven 3.8.5
- PostgreSQL 13
- Браузер

### Подготовка к запуску приложения
- Создать БД cinema хост `jdbc:postgresql://localhost:5432/cinema`
- Собрать jar с приложением, выполнив команду `mvn install`
- Запустить приложение из папки target, выполнив команду: `java -jar job4j_cinema-1.0-SNAPSHOT.jar`
- Перейти в браузере по ссылке `http://localhost:8080/session`

### Таблицы PostgreSQL DB
Таблицы базы данных написаны с помощью Liquibase. Схема БД:
![diagram-db.png](readme/images/diagram-db.png)

### Главная страница, форма с выбором фильма
![sessions.png](readme/images/sessions.png)

### Страница выбора ряда
![row.png](readme/images/row.png)

### Страница выбора места
![cell.png](readme/images/cell.png)

### Успешное бронироование билета
![ticket.png](readme/images/ticket.png)

### Билет не забронирован
![ticketFail.png](readme/images/ticketFail.png)

### Страница добавления нового сеанса
![addSession.png](readme/images/addSession.png)

### Страница регистрации нового пользователя
![user.png](readme/images/user.png)

### Пользователь существует
![userFail.png](readme/images/userFail.png)

### Страница авторизации
![login.png](readme/images/login.png)

### Пользователь не зарегистрирован
![loginFail.png](readme/images/loginFail.png)

### Контакты
- kanmikhaylov@gmail.com
- [telegram](https://t.me/KonstantinM1khaylov) 

[actions-image]: https://github.com/kamikhaylov/job4j_cinema/actions/workflows/maven.yml/badge.svg
[actions-url]: https://github.com/kamikhaylov/job4j_cinema/actions/workflows/maven.yml
[codecov-image]: https://codecov.io/gh/kamikhaylov/job4j_cinema/graph/badge.svg?token=I7VBVSNW7W
[codecov-url]: https://codecov.io/gh/kamikhaylov/job4j_cinema
