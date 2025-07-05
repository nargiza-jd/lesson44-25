<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои книги</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
</head>
<body>
<main>
    <h1>Мои книги</h1>

    <#if books?size == 0>
        <p>У вас нет выданных книг.</p>
    <#else>
        <ul>
            <#list books as book>
                <li>
                    <h3>${book.title}</h3>
                    <p>Автор: ${book.author}</p>

                    <#if book.image?has_content>
                        <img src="/static/images/${book.image}" alt="Обложка" width="100">
                    </#if>

                    <p>Статус:
                        <#switch book.status?string>
                            <#case "AVAILABLE">Доступна<#break>
                            <#case "ISSUED">Выдана<#break>
                            <#default>Неизвестно
                        </#switch>
                    </p>
                    <p>
                        <a href="/books/return?id=${book.id}">Вернуть книгу</a>
                    </p>
                </li>
                <hr>
            </#list>
        </ul>
    </#if>

    <p><a href="/profile">← В личный кабинет</a></p>
</main>
</body>
</html>