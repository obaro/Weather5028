<#import "template.ftl" as layout />
<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                CSCA 5028 Final Project.
            </p>
        </div>
    </section>

    <section>
        <div class="container">
            <p>
                The text entered is: ${userInput}
            </p>
        </div>
    </section>

</@layout.noauthentication>