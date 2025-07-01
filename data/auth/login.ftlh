<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход в библиотеку</title>
    <link rel="stylesheet" href="/static/css/forms.css">
</head>
<body>
<main>
    <form method="post" action="/login">
        <fieldset>
            <div class="legend">
                <p>Добро пожаловать!</p>
                <img src="/static/images/1.jpg" alt="logo" width="150">
            </div>

            <#if error??>
                <div style="color:red; margin-bottom: 10px;">${error}</div>
            </#if>
            <#if success??>
                <div style="color:green; margin-bottom: 10px;">${success}</div>
            </#if>

            <div class="form-element">
                <label for="email">E-mail</label>
                <input type="email" name="email" id="email" placeholder="Введите email" required>
            </div>
            <div class="form-element">
                <label for="password">Пароль</label>
                <input type="password" name="password" id="password" placeholder="Введите пароль" required>
            </div>
            <div class="form-element">
                <button type="submit">Войти</button>
            </div>
        </fieldset>
    </form>

    <p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
</main>
</body>
</html>