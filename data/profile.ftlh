<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
</head>
<body>
    <h1>Личный кабинет</h1>
    <p>Email: ${email}</p>
    <p>Имя: ${fullname}</p>
    <p><a href="/mybooks">Мои книги</a></p>
    <p><a href="/history">История книг</a></p>
    <p>Книг на руках: ${issuedCount}</p>

    <p><a href="/books">← Назад в библиотеку</a></p>
    <p><a href="/logout">Выйти</a></p>
</body>
</html>