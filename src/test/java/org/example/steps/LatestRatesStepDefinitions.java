package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.example.helpers.ConfigHelper;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Suite
@SelectClasspathResource("features")
public class LatestRatesStepDefinitions {

    private Response response;
    private final HashMap<String, String> requestParameters = new HashMap<>();

    @Before
    public void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); //TODO: remove me
        RestAssured.baseURI = ConfigHelper.getBaseUrl();
    }

    @After
    public void cleanUp() {
        requestParameters.clear();
    }

    @Given("proper access key")
    public void properApiKey() {
        requestParameters.put("access_key", ConfigHelper.getApiKey());
    }

    @When("getting latest rates")
    public void getLatestRates() {
        response = RestAssured.given()
                .params(requestParameters)
                .get("/latest");
    }

    @Then("should return {int} status code")
    public void shouldReturnExpectedStatusCode(int expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }
}
