<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список книг</title>
    <link rel="stylesheet" href="/static/css/forms.css">
    <link rel="stylesheet" href="/static/css/freemarker.css">
    <link rel="stylesheet" href="/static/css/book-details.css">
</head>
<body>
<main>
    <h1>Библиотека</h1>

    <#if success?? && success == "taken">
        <p style="color: green;">Вы успешно взяли книгу!</p>
    </#if>
    <#if success?? && success == "returned">
        <p style="color: green;">Книга успешно возвращена.</p>
    </#if>
    <#if error?? && error == "max2">
        <p style="color: red;">Вы не можете взять больше двух книг.</p>
    </#if>
    <#if success?? && success == "1">
        <p style="color: green;">Книга успешно выдана.</p>
    </#if>

    <#if error?? && error == "alreadyTaken">
        <p style="color: red;">Вы уже взяли эту книгу.</p>
    </#if>

    <#if books?size == 0>
        <p>Нет доступных книг.</p>
    <#else>
        <ul>
            <#list books as book>
                <li>
                    <h3>${book.title}</h3>
                    <p>Автор: ${book.author}</p>
                    <#if book.image??>
                        <img src="/static/images/${book.image}" alt="Обложка" width="100">
                    </#if>

                    <p>Статус:
                      <#if book.status?string == "AVAILABLE">
                          Доступна
                      <#else>
                          Выдана
                      </#if>
                    </p>

                    <p>
                        <a href="/book?id=${book.id}">Подробнее</a>
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