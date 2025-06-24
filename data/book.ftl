<h1>${book.title}</h1>
<p>Автор: ${book.author}</p>
<p>Статус: ${book.status}</p>
<#if book.issuedTo??>
    <p>Выдано: ${book.issuedTo}</p>
</#if>
<img src="/images/${book.image}" alt="${book.title}" width="150">