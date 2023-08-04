<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
            crossorigin="anonymous"></script>
</head>
<body>
<header>
    <#include "navbar.ftl">
</header>
<main role="main">
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
                            <th>Jira task ID</th>
                            <th>Suggested Minutes</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list suggestedEntries as suggestedEntry>
                            <tr>
                                <td>${suggestedEntry.date.format('MEDIUM_DATE')}</td>
                                <td>${suggestedEntry.projectDescription}</td>
                                <td>${suggestedEntry.taskId}</td>
                                <td>
                                    <input
                                        type="number"
                                        value="${suggestedEntry.duration.toMinutes()}"
                                        name="hours[${suggestedEntry.date}][hours]"
                                    >
                                    <input type="hidden" value="${suggestedEntry.taskId}"
                                           name="hours[${suggestedEntry.date}][taskId]">
                                    <input type="hidden" value="${suggestedEntry.projectDescription}"
                                           name="hours[${suggestedEntry.date}][projectDescription]">
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
