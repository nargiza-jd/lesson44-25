<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${book.title}</title>
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>
<main>
    <h1>${book.title}</h1>
    <p><strong>Автор:</strong> ${book.author}</p>

    <#if book.image?has_content>
        <img src="/static/images/${book.image}" alt="Обложка" width="100">
    </#if>

    <p><strong>Описание:</strong>
        <#if book.description?has_content>
            ${book.description}
        <#else>
            Нет описания
        </#if>
    </p>


    <p><strong>Статус:</strong>
        <#switch book.status?string>
            <#case "AVAILABLE">Доступна<#break>
            <#case "ISSUED">Выдана<#break>
            <#default>Неизвестно
        </#switch>
    </p>

    <#if book.status?string == "ISSUED" && book.holderEmail?has_content>
        <p><strong>Выдана пользователю:</strong> ${book.holderEmail}</p>
    </#if>

    <div>

        <#if book.status?string == "AVAILABLE">
            <a href="/books/take?id=${book.id}">Взять книгу</a>
        <#elseif book.status?string == "ISSUED"
                 && book.holderEmail?has_content
                 && book.holderEmail == email>
            <a href="/books/return?id=${book.id}">Вернуть книгу</a>
        </#if>

        <#if returned??>
            <p style="color: green; margin-top: 10px;">Книга успешно возвращена!</p>
        </#if>
    </div>

    <p><a href="/books">← Назад к списку книг</a></p>
</main>
</body>
</html>