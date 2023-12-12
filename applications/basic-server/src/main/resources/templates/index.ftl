<#import "template.ftl" as layout />
<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                CSCA 5028 Final Project.
            </p>
            <p>
                MVP - Echo user input
            </p>
        </div>
    </section>

    <#-- <section>
        <div class="container">
            <p>
                Enter a string below and it will be echoed back.
            </p>
            <form action="/postmvp" method="post">
                <#-- <input type="text" id="userInput" placeholder="Enter string to be echoed...">
                    <button onclick="displayInput()">Display</button>
                    <p id="displayArea"></p> --

                    <label for="userInput">User Input:</label>
                    <input type="text" id="userinput" name="userinput" placeholder="Enter string to be echoed...">
                    <input type="submit" value="Submit">
            </form>
        </div>
    </section> -->

    <#-- <section>
        <div class="container">
            <p>
                Select the city to monitor
            </p>
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
    </section> -->

    <section>
        <div class="container">
            <p>
                Select the city to monitor
            </p>
            <form action="/viewcity" method="post" id="cityForm">
                <label for="city">Available Cities:</label>
                <select id="city" name="city">
                    <option value="London">London</option>
                    <option value="Lagos">Lagos</option>
                    <option value="New York">New York</option>
                    <option value="Tokyo">Tokyo</option>
                </select>
                <input type="submit" value="Submit">
            </form>
        </div>
    </section>
</@layout.noauthentication>