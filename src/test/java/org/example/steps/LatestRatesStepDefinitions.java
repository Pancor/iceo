package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.example.helpers.ConfigHelper;
import org.example.helpers.UrlHelper;
import org.example.model.Currency;
import org.example.model.RatesError;
import org.example.model.RatesErrorCode;
import org.example.model.RatesResponse;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Suite
@SelectClasspathResource("features")
public class LatestRatesStepDefinitions {

    private String requestMethod = "GET";
    private Response response;
    private final HashMap<String, String> requestParameters = new HashMap<>();

    @Before
    public void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); //TODO: remove me
        RestAssured.baseURI = ConfigHelper.getBaseUrl();
        requestMethod = "GET";
    }

    @After
    public void cleanUp() {
        requestParameters.clear();
    }

    @ParameterType("true|True|TRUE|false|False|FALSE")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    @ParameterType("[^\\s]*")
    public RatesErrorCode ratesErrorCode(String value) {
        return RatesErrorCode.json(value);
    }

    @Given("request over HTTPS")
    public void requestOverPlainHTTP() {
        String baseUrl = ConfigHelper.getBaseUrl();
        RestAssured.baseURI = UrlHelper.obtainHttpsUrl(baseUrl);
    }

    @Given("proper access key")
    public void properApiKey() {
        requestParameters.put("access_key", ConfigHelper.getApiKey());
    }

    @Given("{string} as access key")
    public void wrongApiKey(String apiKey) {
        requestParameters.put("access_key", apiKey);
    }

    @Given("preparing request without provided base currency")
    public void requestWithoutProvidedBaseCurrency() {
        //intentionally empty
    }

    @Given("preparing request without provided currencies to receive")
    public void preparingRequestWithoutProvidedCurrenciesToReceive() {
        //intentionally empty
    }

    @Given("setting base currency to {string}")
    public void settingBaseCurrencyToCurrency(String baseCurrency) {
        requestParameters.put("base", baseCurrency);
    }

    @Given("setting currency codes to receive to {string}")
    public void settingCurrencyCodesToReceiveTo(String currencies) {
        requestParameters.put("symbols", currencies);
    }

    @Given("message containing {string}")
    public void messageContaining(String expectedMessage) {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);

        assertThat(ratesResponse.getError()).isNotNull()
                .extracting(RatesError::getMessage)
                .isEqualTo(expectedMessage);
    }

    @Given("callback named {string}")
    public void callbackNamed(String callback) {
        requestParameters.put("callback", callback);
    }

    @Given("HTTP method set to {string}")
    public void httpMethodSetTo(String method) {
        requestMethod = method;
    }

    @When("getting latest rates")
    public void getLatestRates() {
        response = RestAssured.given()
                .params(requestParameters)
                .request(requestMethod, "/latest");
    }

    @Then("should return {int} status code")
    public void shouldReturnExpectedStatusCode(int expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("should not return {int} status code")
    public void shouldNotReturnProvidedStatusCode(int statusCode) {
        assertThat(response.getStatusCode()).isNotEqualTo(statusCode);
    }

    @Then("should return success with {booleanValue}")
    public void shouldReturnExpectedStatusCode(boolean expectedSuccessfulness) {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);

        assertThat(ratesResponse.getSuccess()).isEqualTo(expectedSuccessfulness);
    }

    @Then("should use {string} as base currency")
    public void shouldUseGivenCurrency(String baseCurrency) {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);

        assertThat(ratesResponse.getBase()).isEqualTo(baseCurrency);
    }

    @Then("should return {ratesErrorCode} code")
    public void shouldReturnGivenCode(RatesErrorCode ratesErrorCode) {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        response.getBody().prettyPrint();
        assertThat(ratesResponse.getError()).isNotNull()
                .extracting(RatesError::getCode)
                    .isEqualTo(ratesErrorCode);
    }

    @Then("should return all available currencies")
    public void shouldReturnExchangeRateForAllCurrencies() {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        String[] expectedCurrencies = Arrays.stream(Currency.values())
                .filter(currency -> currency != Currency.UNKNOWN)
                .map(Enum::name)
                .toArray(String[]::new);

        assertThat(ratesResponse.getRates()).containsOnlyKeys(expectedCurrencies);
    }

    @Then("should return exchange rate for all available currencies")
    public void shouldReturnExchangeRateForAllAvailableCurrencies() {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);

        assertThat(ratesResponse.getRates()).doesNotContainValue(null)
                .doesNotContainValue(0.0);
    }


    @Then("should return exchange rate only for {string} currencies")
    public void shouldReturnExchangeRateForCurrencies(String currencies) {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        String[] expectedCurrencies = currencies.split(",");

        assertThat(ratesResponse.getRates()).containsOnlyKeys(expectedCurrencies);
    }

    @Then("should return proper timestamp")
    public void shouldReturnCurrentTimestamp() {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        long currentTimestamp = System.currentTimeMillis() / 1000;

        assertThat(ratesResponse.getTimestamp()).isLessThan(currentTimestamp);
    }

    @Then("should return proper date")
    public void shouldReturnProperDate() {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        LocalDate responseDate = LocalDate.parse(ratesResponse.getDate());

        assertThat(responseDate).isBeforeOrEqualTo(LocalDate.now());
    }

    @Then("response should be wrapped {string}")
    public void responseShouldBeWrapped(String callback) {
        String rawResponse = response.asString();

        assertThat(rawResponse).startsWith(callback + "(")
                .endsWith(")");
    }

    @Then("response should contain header named {string}")
    public void responseShouldContainHeaderNamed(String header) {
        assertThat(response.header(header)).isNotEmpty();
    }

    @Then("should return date in YYY-MM-DD format")
    public void shouldReturnDateInFormat() {
        RatesResponse ratesResponse = response.getBody().as(RatesResponse.class);
        String dateRegex = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";

        assertThat(ratesResponse.getDate()).matches(dateRegex);
    }
}
