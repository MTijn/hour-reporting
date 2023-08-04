<nav class="navbar navbar-expand-lg bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">
            <h1 style="color: #FFFFFF">Hour reporting tool</h1>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse w-100 order-3 dual-collapse2" id="navbarSupportedContent">
            <ul class="navbar-nav ms-auto dropstart">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <img
                            src="data:image/jpeg;base64,${user.photo}"
                            alt="${user.name}"
                            width="40"
                            height="40"
                            class="rounded-circle"
                        >
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="/categories">Configure categories</a></li>
                        <li><a class="dropdown-item" href="/categories/ignored">Configure ignored categories</a></li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li><a class="dropdown-item" href="/logout">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
