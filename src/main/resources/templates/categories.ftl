<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hour reporting | User configuration</title>
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
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h2>Please define the categories that should be mapped to Jira stories</h2>
                <form action="/categories" method="post">
                    <#list categories as category>
                        <div class="row mb-3">
                            <label for="category_${category.id}" class="col-sm-2 col-form-label">${category.displayName}</label>
                            <div class="col-sm-10">
                                <input
                                    type="text"
                                    class="form-control"
                                    id="category_${category.id}"
                                    name="categories[${category.id}]"
                                >
                                <input class="form-check-input" type="radio" value="${category.id}" id="default" name="default">
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
