<#import "template.ftl" as layout />

<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                An example application using Kotlin and Ktor.
            </p>
        </div>
    </section>

    <section>
        <div class="container">
            <form id="cityForm">
                <label for="source">Available Cities:</label>
                <select id="source" multiple>
                    <option value="London">London</option>
                    <option value="New York">New York</option>
                    <option value="Lagos">Lagos</option>
                    <option value="California">California</option>
                    <option value="Vancouver">Vancouver</option>
                    <option value="Amsterdam">Amsterdam</option>
                    <option value="Berlin">Berlin</option>
                    <option value="Cape Town">Cape Town</option>
                </select>

                <button type="button" onclick="moveCities()">Move Selected</button>

                <label for="destination">Selected Cities:</label>
                <select id="destination" multiple></select>
            </form>
        </div>
    </section>

</@layout.noauthentication>