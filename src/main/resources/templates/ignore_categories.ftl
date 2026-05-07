<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hour reporting | Ignored categories</title>
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
                <h2>Please define the categories that should be ignored</h2>
                <form action="/categories/ignored" method="post">
                    <#list categories as category>
                        <div class="row">
                            <div class="input-group mb-3">
                                <div class="form-check">
                                    <input
                                        class="form-check-input"
                                        type="checkbox"
                                        value="${category.displayName}"
                                        id="category_${category.id}"
                                        <#if category.getIgnored()>checked="checked"</#if>
                                        name="categories"
                                    >
                                    <label class="form-check-label" for="category_${category.id}">
                                        ${category.displayName}
                                    </label>
                                </div>
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
