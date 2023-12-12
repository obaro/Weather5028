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
                Refresh data
            </p>
            <form action="/viewcity" method="post" id="cityForm">
                <select id="city" name="city" hidden="hidden">
                    <option value="${city}" selected="selected">${city}</option>
                </select>
                <input type="submit" value="Refresh">
            </form>
        </div>
    </section>

    <section>
        <div class="container">
            <p>
                Showing data for: <strong>${city}</strong> over the past <strong>${range}</strong> minutes
            </p>
        </div>
        <div class="container">
            <table>
                <tr>
                    <th>Data Type</th>
                    <th>Previous Values (${range} mins ago)</th>
                    <th>Latest Values</th>
                </tr>
                <tr>
                    <td>Temperature (C)</td>
                    <td>${analyzed.prev_temp_c?string("0")}</td>
                    <td>${analyzed.current_temp_c?string("0")}</td>
                </tr>
                <tr>
                    <td>Wind Speed (Kph)</td>
                    <td>${analyzed.prev_wind_kph?string("0")}</td>
                    <td>${analyzed.current_wind_kph?string("0")}</td>
                </tr>
                <tr>
                    <td>Visibility (Km)</td>
                    <td>${analyzed.prev_vis_km?string("0")}</td>
                    <td>${analyzed.current_vis_km?string("0")}</td>
                </tr>
            </table>
        </div>
    </section>

</@layout.noauthentication>