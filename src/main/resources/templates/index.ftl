<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <script src=" https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js "></script>
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
                    <table id="suggestedEntries" class="table table-striped">
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
                                <td>${suggestedEntry.taskKey}</td>
                                <td>
                                    <input
                                        type="number"
                                        value="${suggestedEntry.duration.toMinutes()}"
                                        name="hours[${suggestedEntry.date}][hours]"
                                    >
                                    <input type="hidden" value="${suggestedEntry.taskId?c}"
                                           name="hours[${suggestedEntry.date}][taskId]">
                                    <input type="hidden" value="${suggestedEntry.projectDescription}"
                                           name="hours[${suggestedEntry.date}][projectDescription]">
                                </td>
                                <td>
                                    <button class="delete_row btn btn-outline-danger">Delete</button>
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
<script type="application/javascript">
    $(document).ready(function() {
        $("#suggestedEntries").on('click', '.delete_row', function() {
            $(this).closest('tr').remove();
        });
    });
</script>
</body>
</html>
