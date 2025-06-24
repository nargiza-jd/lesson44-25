<h1>Список книг</h1>
<ul>
<#list books as book>
    <li>
        <a href="/book?id=${book.id}">${book.title}</a>
        — ${book.author} — <b>${book.status}</b>
    </li>
</#list>
</ul>