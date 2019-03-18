## Payment Service
**Тут будет описание системы**

 Первоначальная настройка:
- Создаем 3 БД PostgreSQL, настройки можно посмотреть в application.yml
- В каждой БД создаем схему payment 

##### В роли базы был выбран postgeSQL

**Тестирование**

Для тестирования вставки записей
необходимо тело вида:
[
    {
        "senderId": "03f112f4-48e2-11e9-8646-d663bd873d93",
        "receiverId": "11f118b2-48e2-11e9-8646-d663bd873d93",
        "amount": 111
    },
    {
        "senderId": "03f112f4-48e2-11e9-8646-d663bd873d93",
        "receiverId": "11f118b2-48e2-11e9-8646-d663bd873d93",
        "amount": 211
    },
    {
        "senderId": "03f112f4-48e2-11e9-8646-d663bd873d93",
        "receiverId": "11f118b2-48e2-11e9-8646-d663bd873d93",
        "amount": 311
    },
    {
        "senderId": "03f112f4-48e2-11e9-8646-d663bd873d93",
        "receiverId": "11f118b2-48e2-11e9-8646-d663bd873d93",
        "amount": 411
    }
]

Можно добавить поле id - но есть риск что такой id уже есть в бд и возникнет ошибка.

##TODO
Что не успел или нужно доделать:
1) Тесты - юнит, интеграционные
2) При выборе ТОП-10 нужно будет учитывать то что получатель тратил
3) Добавить таблицы пользователей и работу с ними (пока много вопросов как это сделать)
4) Переделать под jooq (убил пол дня, не удалось)
5) Глубоко поработать с транзакционностью