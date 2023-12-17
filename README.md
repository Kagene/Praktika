# Praktika
Краткое описание работы кода
Чтение данных из CSV файла
1. parseCSV(filename): Метод читает данные из CSV файла. Он принимает путь к файлу в качестве параметра, читает содержимое CSV файла и создает объекты типа SalesData для каждой строки в файле. Затем он добавляет эти объекты в список "salesDataList".
Работа с базой данных SQLite
2. Создание подключения к базе данных: После чтения данных из CSV файла, код устанавливает подключение к базе данных SQLite.
3. Создание таблицы в базе данных: Если таблицы "Sales" не существует в базе данных, код создает новую таблицу с полями: "id", "region", "country", "itemType", "salesChannel", "orderPriority", "orderDate", "unitsSold", "totalProfit".
4. Вставка данных в таблицу: Далее, код проходит через данные из "salesDataList" и вставляет новые записи в таблицу "Sales" только в том случае, если данной комбинации "orderDate" и "region" еще нет в базе данных (проверка осуществляется посредством выполнения запроса "SELECT COUNT(*) AS count FROM Sales WHERE orderDate = ? AND region = ?").
5. Выполнение SQL запросов:
Запрос "query1": Выполняется запрос для подсчета общего количества проданных товаров по регионам из таблицы "Sales".
Запрос "query3": Выполняется запрос для поиска страны с наибольшим общим доходом в регионах "Europe" и "Asia" на основе суммы "TotalProfit" для каждой страны.
Запрос "query4": Выполняется запрос для поиска страны с общим доходом от 420 000 до 440 000 в регионах "Middle East and North Africa" и "Sub-Saharan Africa".
Вывод результатов
6. Вывод на консоль: Результаты SQL-запросов выводятся на консоль в формате, показывая общее количество проданных единиц для каждого региона, страну с наибольшим общим доходом в "Europe" и "Asia", а также страну с общим доходом между 420 000 и 440 000 в "Middle East and North Africa" и "Sub-Saharan Africa".
Обработка ошибок
7. Обработка исключений: Код также содержит обработку исключений для обработки возможных ошибок, таких как ошибки при чтении файла, ошибки подключения к базе данных и другие SQL-исключения.

Здесь отображаются:
1) Страны общему количеству проданных товаров объединив их по регионам.
2) Страна с самым высоким общим доходом среди  регионов Европы и Азии
   ![ce27cab4-e286-4228-8ba0-325b2c638b39](https://github.com/Kagene/Praktika/assets/147978324/7fbd261e-7913-4bb3-ab58-df0837b65a49)


