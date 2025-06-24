<!-- data/employee.ftlh -->
<h2>${employee.firstName} ${employee.lastName}</h2>
<p>Email: ${employee.email}</p>

<h3>Книги, которые он взял:</h3>
<ul>
<#list employee.books as book>
    <li>${book.title} — ${book.author}</li>
</#list>
</ul>