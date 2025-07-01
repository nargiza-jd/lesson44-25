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

        <#if book.image??>
            <img src="/static/images/${book.image}" alt="Обложка" width="200">
        </#if>

        <p><strong>Описание:</strong>
            <#if book.description??>
                ${book.description}
            <#else>
                Нет описания
            </#if>
        </p>

        <p><strong>Статус:</strong>
            <#if book.status == "AVAILABLE">
                Доступна
            <#else>
                Выдана
            </#if>
        </p>

        <#if book.holderEmail??>
            <p><strong>Выдана пользователю:</strong> ${book.holderEmail}</p>
        </#if>

        <div>

            <#if book.status == "AVAILABLE">
                <a href="/take?id=${book.id}">Взять книгу</a>
            <#else>
                <a href="/return?id=${book.id}">Вернуть книгу</a>
            </#if>
        </div>

        <p><a href="/books">← Назад к списку книг</a></p>
    </main>
</body>
</html>