<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>История книг</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
</head>
<body>
<main>
    <h1>История книг</h1>

    <#if books?has_content>
        <ul>
            <#list books as book>
                <li>
                    <h3>${book.title}</h3>
                    <p>Автор: ${book.author}</p>
                    <#if book.image??>
                        <img src="/static/images/${book.image}" alt="Обложка" width="100">
                    </#if>
                    <p>Ранее выдана</p>
                </li>
                <hr>
            </#list>
        </ul>
    <#else>
        <p>История пуста.</p>
    </#if>

    <p><a href="/profile">← В личный кабинет</a></p>
</main>
</body>
</html>