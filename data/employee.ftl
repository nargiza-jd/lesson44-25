<#if employee??>
<h2>Информация о сотруднике</h2>
<p><strong>Имя:</strong> ${employee.firstName} ${employee.lastName}</p>
<p><strong>Взятые книги:</strong></p>
<ul>
<#list employee.issuedBookIds as bookId>
    <li>ID книги: ${bookId}</li>
</#list>
</ul>
<#else>
<p>Сотрудник не найден</p>
</#if>