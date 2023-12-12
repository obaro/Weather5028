<#import "template.ftl" as layout />
<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                CSCA 5028 Final Project.
            </p>
            <p>
                Return to <a href="/">Choose City</a> .
            </p>
        </div>
    </section>

    <section>
        <div class="container">
            <p>
                Showing data for: ${city}
            </p>
        </div>
    </section>

</@layout.noauthentication>