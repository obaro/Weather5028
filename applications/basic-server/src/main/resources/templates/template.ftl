<#macro noauthentication title="Welcome">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name=viewport content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/style/reset.css">
        <link rel="stylesheet" href="/style/style.css">
        <link rel="icon" type="image/png" href="/style/favicon.png">
        <script src="/js/site.js"></script>
        <title>CSCA 5028 MVP</title>
    </head>
    <body>
    <header>
        <div class="container">
            <h1>Weather Monitor MVP</h1>
        </div>
    </header>
    <section class="callout">
        <div class="container">
            Weather Monitor application FTW!!
        </div>
    </section>
    <main>
        <#nested>
    </main>
    <footer>
        <div class="container">
            <script>document.write("©" + new Date().getFullYear());</script>
            CSCA 5028 Final Project
        </div>
    </footer>
    </body>
    </html>
</#macro>