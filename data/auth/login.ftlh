<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login form</title>

    <link rel="stylesheet" href="/css/forms.css">
</head>

<body>
<main>

    <form action="/auth/login" method="post">
        <fieldset>
            <legend class="legend">
                <p>Welcome!</p>
                <img src="/images/1.jpg" alt="Java logo" style="display:block; width:200px; height:200px;">
            </legend>

            <div class="form-element">
                <label for="user-email">e-mail</label>
                <input type="email"
                       name="email"
                       id="user-email"
                       placeholder="your email"
                       required
                       autofocus>
            </div>

            <div class="form-element">
                <label for="user-password">password</label>

                <input type="password"
                       name="password"
                       id="user-password"
                       placeholder="your password"
                       required>
            </div>

            <div class="hr-line">
                <span class="details">one more step to go</span>
            </div>

            <div class="form-element">
                <button class="register-button" type="submit">Login!</button>
            </div>
        </fieldset>
    </form>

    <#if error??>
        <p style="color:red">${error}</p>
    </#if>
</main>
</body>
</html>