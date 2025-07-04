<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
</head>
<body>
    <h2>Регистрация нового сотрудника</h2>
    <img src="/static/images/1.jpg" alt="logo" width="150">
    <form action="/register" method="post">
        <label>Email:
            <input type="email" name="email" required>
        </label><br>

        <label>ФИО:
            <input type="text" name="fullname" required>
        </label><br>

        <label>Пароль:
            <input type="password" name="password" required>
        </label><br>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <#if error??>
        <p style="color:red;">Ошибка: пользователь уже существует.</p>
    </#if>
</body>
</html>