<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Home page</title>
    </head>
    <body>
        <h1>Something</h1>
        <#include "time_entry.ftl">
        <h2>Suggested time entries</h2>
        <form action="/enter" method="post">
            <table>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Suggested</th>
                </tr>
                <#list suggestedEntries as suggestedEntry>
                    <tr>
                        <td>${suggestedEntry.date.format('MEDIUM_DATE')}</td>
                        <td>${suggestedEntry.projectDescription}</td>
                        <td><input type="number" value="${suggestedEntry.duration.toHours()}"></td>
                    </tr>
                </#list>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="submit" value="Send">
        </form>
    </body>
</html>
