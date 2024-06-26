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
                <h2>Welcome ${userName}</h2>
                <p>In order for this application to work you will need to provide an API key from Jira</p>
                <p>Please create one and copy it into the input field</p>
                <p>The API key itself is stored in the app's database in an encrypted way</p>
                <form action="/user" method="post">
                    <div class="form-group">
                        <label for="jiraApiKey">Jira API key</label>
                        <input type="password" class="form-control" id="jiraApiKey" name="jiraApiKey">
                    </div>
                    <div class="form-group">
                        <label for="tempoApiKey">Tempo API key</label>
                        <input type="password" class="form-control" id="tempoApiKey" name="tempoApiKey">
                    </div>
                    <div class="form-group">
                        <label for="jira_user_name">Jira user name</label>
                        <input type="text" class="form-control" id="jira_user_name" name="jiraUserName">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="submit" value="Send">
                </form>
            </div>
        </div>
    </div>
</main>
</body>
</html>
