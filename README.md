Абдуллаев Егор. @EA7Z7

Задачи проекта:

1. (citizen): отправка обращения гражданина.\
В консоль считываются контакты и обращение гражданина. Сервис создаёт в БД запись с клиентом, если с таким номером ещё не существует и отправляет обращение в БД.\
Выводит id обращения.

2. (citizen): узнать статус обращения и текст ответа на обращение гражданина по id обращения.\
В консоль считывается идентификатор обращения.\
Выводит статус обращения и текст ответа на обращение.

3. (citizen): узнать статусы и id обращений по номеру телефона гражданина.\
В консоль считывается номер телефона.\
Выводит статусы и id обращений.

4. (employee): просмотреть все обращения со статусом “CREATED”.\
Выводит все обращения со статусом “CREATED”.

5. (employee): просмотреть историю рассмотренных обращений(со статусом PROCESSED или REJECTED) за последние n дней.\
Выводит историю рассмотренных обращений за последние n дней.

6. (employee): просмотреть информацию об обращении.\
Выводит информацию об обращении (статус, текст, дата обращения).

7. (employee): просмотреть контакты автора обращения.\
Выводит контакты автора обращения по идентификатору обращения.

8. (employee): ответить на обращение.\
Передаётся идентификатор обращения, текст ответа (если новый статус ставится сотрудником как REJECTED, то текст ответа автоматически пустой) и новый статус обращения.\
Отвечает на обращение.

Требования:

1. liberica-21 jdk
2. IDE - IntelliJ Idea

Инструкция к запуcку:

1. Клонировать репозиторий
2. Открыть проект в IntelliJ Idea
3. Добавить h2 БД в библиотеки (нажать ПКМ на папке lib и нажать "Add as Library")
4. Запустить Application.main()

Использованные темы:

1. Базовый синтаксис языка: объявления переменных и констант, логические конструкции, Switch Expressions.
2. ООП: наследование, инкапсуляция.
3. Обработка ошибок: в пакет exception было вынесено несколько специализированных исключений для работы с DAO, сервисом, созданием таблиц и чтением файла.
4. Библиотека классов Java: обработка строк, регулярные выражения, даты.
5. Java IO: консоль приложения считывает из System.in, ответ выводится через System.out, так же путь к бд, логин и пароль к бд читаются из файла с помощью BufferedReader.
6. Коллекции: для вывода списков обращений использован ArrayList
7. Лямбда выражения: используются часто вместе с forEach
8. Работа с базами данных: была спроектирована и создана база данных и тремя таблицами, для которых были написаны DAO
9. Аннотации: Override

