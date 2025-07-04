<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${book.title} - Детали книги</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
    <link rel="stylesheet" href="/static/css/book-details.css">
</head>
<body>
    <div class="container">
        <h1>${book.title}</h1>

        <div class="book-details">
            <div class="book-cover-container">
                <img src="/static/images/${book.image}" alt="Обложка книги: ${book.title}" class="book-cover">
            </div>
            <div class="book-info">
                <p><strong>Автор:</strong> ${book.author}</p>
                <p><strong>Описание:</strong> ${book.description}</p>
                <p><strong>Статус:</strong> ${book.status}</p>
                <#if book.holderEmail?? && book.holderEmail?has_content>
                    <p><strong>Выдана пользователю:</strong> ${book.holderEmail}</p>
                </#if>
            </div>
        </div>

        <div class="action-links">
            <#if book.status == "ISSUED">
                <a href="/books/return?id=${book.id}">Вернуть книгу</a>
            <#else>
                <a href="/books/take?id=${book.id}">Взять книгу</a>
            </#if>
            <p><a href="/books">← Назад к списку книг</a></p>
        </div>

        <#if returned??>
            <p class="success-message">Книга успешно возвращена!</p>
        </#if>
    </div>
</body>
</html>