<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hour reporting | User configuration</title>
    <script src=" https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.min.js "></script>
    <link href=" https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css " rel="stylesheet">
</head>
<body>
<header>
    <#include "navbar.ftl">
</header>
<main role="main">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2>Please define the categories that should be mapped to Jira stories</h2>
                <form action="/categories" method="post">
                    <#list categories as category>
                    <div class="row">
                        <div class="col-md-2">
                            <label for="category_${category.id}" class="col-form-label">${category.displayName}</label>
                        </div>
                        <div class="col-md-10">
                            <div class="input-group mb-3">
                                <div class="input-group-text">
                                    <input
                                        class="form-check-input mt-0"
                                        type="radio"
                                        aria-label="Fallback category"
                                        value="${category.displayName}"
                                        id="default"
                                        name="default"
                                        <#if category.isDefault()>checked="checked"</#if>
                                    >
                                </div>
                                <input
                                    type="text"
                                    class="form-control"
                                    id="category_${category.displayName}"
                                    name="categories[${category.displayName}]"
                                    value="${category.jiraKey! ''}"
                                >
                            </div>
                        </div>
                    </div>
                    </#list>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input class="btn btn-primary" type="submit" value="Send">
                </form>
            </div>
        </div>
    </div>
</main>
</body>
</html>
