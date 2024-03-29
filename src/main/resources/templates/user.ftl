<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hour reporting | User configuration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
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
                <h2>Please define the categories that should be mapped</h2>
                <form action="/user/mapping" method="post">
                    <div class="form-group row">
                        <label id="default" class="col-sm-2 col-form-label">Default</label>
                        <div class="col-sm-9">
                            <select id="default" name="categories[default]" class="form-control">
                                <#list projects as project>
                                    <#list project.tasks as task>
                                        <option value="${task.id}">${project.name} - ${task.name}</option>
                                    </#list>
                                </#list>
                            </select>
                        </div>
                    </div>

                    <#list categories as category>
                        <div class="form-group row">
                            <label id="${category.id}" class="col-sm-2 col-form-label">${category.name}</label>
                            <div class="col-sm-9">
                                <select id="${category.id}" name="categories[${category.id}]" class="form-control">
                                    <option>Do not map</option>
                                    <#list projects as project>
                                        <#list project.tasks as task>
                                            <option value="${task.id}">${project.name} - ${task.name}</option>
                                        </#list>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </#list>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="submit" value="Send">
                </form>
            </div>
        </div>
    </div>
</main>
</body>
</html>
