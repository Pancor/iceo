Feature: API tests for obtaining latest rate data from https://api.exchangeratesapi.io/v1

  Background:
    Given proper access key

  @failing
  Scenario Outline: Ignore request different than GET
    It's good practise to allow only one HTTP method for given endpoint when given endpoint serves only one purpose
    And HTTP method set to "<method>"
    When getting latest rates
    Then should return 404 status code

    Examples:
      | method  |
      | PUT     |
      | HEAD    |
      | POST    |
      | PATCH   |
      | DELETE  |
      | OPTIONS |

  @failing
  Scenario: Response should contain ETag
    Response does not contain ETag header as specified in documentation
    When getting latest rates
    Then response should contain header named "ETag"

  Scenario: Response is wrapped around callback when provided callback name
    And callback named "testCallback"
    When getting latest rates
    Then response should be wrapped "testCallback"

  Scenario: Response has 200 status code when providing only access key
    When getting latest rates
    Then should return 200 status code

  Scenario: Response contains success set to true
    When getting latest rates
    Then should return success with true

  Scenario: Response contains timestamp which is lower than current time
    When getting latest rates
    Then should return proper timestamp

  Scenario: Response contains date which is lower than current time
    When getting latest rates
    Then should return proper date

  Scenario: Response contains date with format YYYY-MM-DD
    When getting latest rates
    Then should return date in YYY-MM-DD format

  Scenario: Default base currency is euro
    And preparing request without provided base currency
    When getting latest rates
    Then should use "EUR" as base currency

  Scenario: Provided euro as base currency is used in response
    And setting base currency to "EUR"
    When getting latest rates
    Then should use "EUR" as base currency

  @failing
  Scenario Outline: Base currency different than euro fails for free plan
    Typo in API response, should be occurred, not ocurred
    And setting base currency to "<currency>"
    When getting latest rates
    Then should return 400 status code
    And should return base_currency_access_restricted code
    And message containing "An unexpected error occurred. [Technical Support: support@apilayer.com]"

    Examples:
      | currency |
      | USD      |
      | GBP      |
      | PLN      |

  Scenario: Response has 400 status code when provided unknown base currency
    And setting base currency to "unknown"
    When getting latest rates
    Then should return 400 status code

  Scenario: Response contains invalid_base_currency code when provided unknown base currency
    And setting base currency to "unknown"
    When getting latest rates
    Then should return invalid_base_currency code

  Scenario: Default list of exchange rates currencies contains all available currencies
    And preparing request without provided currencies to receive
    When getting latest rates
    Then should return all available currencies

  Scenario: Each returned exchange currency contains proper rate
    And preparing request without provided currencies to receive
    When getting latest rates
    Then should return exchange rate for all available currencies

  Scenario: Returns only required currency codes
    And setting currency codes to receive to "USD,PLN"
    When getting latest rates
    Then should return exchange rate only for "USD,PLN" currencies

  Scenario: Response has 400 status code when provided unknown exchange currency
    And setting currency codes to receive to "UNKNOWN"
    When getting latest rates
    Then should return 400 status code

  Scenario: Response contains invalid_currency_codes code when provided unknown exchange currency
    And setting currency codes to receive to "UNKNOWN"
    When getting latest rates
    Then should return invalid_currency_codes code

  Scenario: Returns only known exchange currencies when among provided currencies is unknown symbol
    And setting currency codes to receive to "GBP,BOB,UNKNOWN"
    When getting latest rates
    Then should return exchange rate only for "GBP,BOB" currencies