<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Home page</title>
        <link rel="stylesheet"
              href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
              integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
              crossorigin="anonymous"
        >
        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
                integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
                crossorigin="anonymous"
        ></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
                integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
                crossorigin="anonymous"
        ></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
                integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
                crossorigin="anonymous"
        ></script>
    </head>
    <body>
        <header>
            <nav class="navbar navbar-expand-lg bg-dark">
                <a class="navbar-brand" href="/">
                    <h1 style="color: #FFFFFF">Hour reporting tool</h1>
                </a>
            </nav>
        </header>
        <main role="main">
            <#include "time_entry.ftl">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <h2>Suggested time entries</h2>
                        <form action="/enter" method="post">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Description</th>
                                    <th>Suggested Minutes</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list suggestedEntries as suggestedEntry>
                                    <tr>
                                        <td>${suggestedEntry.date.format('MEDIUM_DATE')}</td>
                                        <td>${suggestedEntry.projectDescription}</td>
                                        <td>
                                            <input
                                                    type="number"
                                                    value="${suggestedEntry.duration.toMinutes()}"
                                                    name="hours[${suggestedEntry.date}][hours]"
                                            >
                                            <input type="hidden" value="${suggestedEntry.taskId}" name="hours[${suggestedEntry.date}][taskId]">
                                        </td>
                                    </tr>
                                </#list>
                                </tbody>
                            </table>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <input type="submit" value="Send">
                        </form>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>
