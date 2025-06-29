<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
    <link rel="stylesheet" href="/css/forms.css">
</head>
<body>
<main>

    <#if message??>
        <p style="color:red;">${message}</p>
    </#if>

    <#if !user??>
        <form action="/register" method="post">
            <fieldset>
                <legend>New employee</legend>

                <label>e-mail
                    <input type="email" name="email" required autofocus>
                </label>

                <label>full name
                    <input type="text" name="fullname" required>
                </label>

                <label>password
                    <input type="password" name="password" required>
                </label>

                <button type="submit">Register</button>
            </fieldset>
        </form>
    <#else>
        <p>You are already registered as ${user.fullName}</p>
    </#if>

</main>
</body>
</html>