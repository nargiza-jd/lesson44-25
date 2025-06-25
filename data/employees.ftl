<h1>Список сотрудников</h1>
<ul>
<#list employees as emp>
    <li>
        <a href="/employee?id=${emp.id}">
            ${emp.firstName} ${emp.lastName}
        </a>
        <#if emp.issuedBookIds?size gt 0>
            — взял книг: ${emp.issuedBookIds?size}
        <#else>
            — нет взятых книг
        </#if>
    </li>
</#list>
</ul>