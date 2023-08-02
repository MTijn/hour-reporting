<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hour reporting | User configuration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
            crossorigin="anonymous"></script>
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
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2>Please define the categories that should be mapped to Jira stories</h2>
                <form action="/categories" method="post">
                    <#list categories as category>
                    <div class="row mb-3">
                        <div class="input-group mb-3">
                            <div class="input-group">
                                <label for="category_${category.id}"
                                       class="col-sm-2 col-form-label">${category.displayName}</label>
                                <div class="input-group-text">
                                    <input
                                        class="form-check-input mt-0"
                                        type="radio"
                                        aria-label="Fallback category"
                                        value="${category.id}"
                                        id="default"
                                        name="default"
                                        <#if category.isDefault()>checked="checked"</#if>
                                    >
                                </div>
                                <input
                                    type="text"
                                    class="form-control"
                                    id="category_${category.id}"
                                    name="categories[${category.id}]"
                                    value="${category.jiraTicketId! ''}"
                                >
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
