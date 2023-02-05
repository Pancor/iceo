Feature: API tests for obtaining latest rate data from https://api.exchangeratesapi.io/v1

  Scenario: Making HTTPS request with free plan returns 403 status code
    Given proper access key
    And request over HTTPS
    When getting latest rates
    Then should return 403 status code

  Scenario: Making HTTPS request with free plan returns https_access_restricted code
    Given proper access key
    And request over HTTPS
    When getting latest rates
    Then should return https_access_restricted code

  Scenario: Making HTTPS request with free plan returns message about restricted access
    Given proper access key
    And request over HTTPS
    When getting latest rates
    Then message containing "Access Restricted - Your current Subscription Plan does not support HTTPS Encryption."

  Scenario: Response has 401 status code when provided empty access key
    Given "" as access key
    When getting latest rates
    Then should return 401 status code

  Scenario: Response has missing_access_key code when provided empty access key
    Given "" as access key
    When getting latest rates
    Then should return missing_access_key code

  Scenario: Response has message about missing access key when provided empty access key
    Given "" as access key
    When getting latest rates
    Then message containing "You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]"

  Scenario: Response has 401 status code when provided wrong access key
    Given "unknown" as access key
    When getting latest rates
    Then should return 401 status code

  Scenario: Response has invalid_access_key code when provided wrong access key
    Given "unknown" as access key
    When getting latest rates
    Then should return invalid_access_key code

  Scenario: Response has message about invalid access key when provided wrong access key
    Given "unknown" as access key
    When getting latest rates
    Then message containing "You have not supplied a valid API Access Key."

  @failing
  Scenario Outline: Ignore request different than GET
    It's good practise to allow only one HTTP method for given endpoint when given endpoint serves only one purpose
    Given proper access key
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
    Given proper access key
    When getting latest rates
    Then response should contain header named "ETag"

  Scenario: Response is wrapped around callback when provided callback name
    Given proper access key
    And callback named "testCallback"
    When getting latest rates
    Then response should be wrapped "testCallback"

  Scenario: Response has 200 status code when providing only access key
    Given proper access key
    When getting latest rates
    Then should return 200 status code

  Scenario: Response contains success set to true
    Given proper access key
    When getting latest rates
    Then should return success with true

  Scenario: Response contains timestamp which is lower than current time
    Given proper access key
    When getting latest rates
    Then should return proper timestamp

  Scenario: Response contains date which is lower than current time
    Given proper access key
    When getting latest rates
    Then should return proper date

  Scenario: Response contains date with format YYYY-MM-DD
    Given proper access key
    When getting latest rates
    Then should return date in YYY-MM-DD format

  Scenario: Default base currency is euro
    Given proper access key
    And preparing request without provided base currency
    When getting latest rates
    Then should use "EUR" as base currency

  Scenario: Provided euro as base currency is used in response
    Given proper access key
    And setting base currency to "EUR"
    When getting latest rates
    Then should use "EUR" as base currency

  @failing
  Scenario Outline: Base currency different than euro fails for free plan
    Typo in API response, should be occurred, not ocurred
    Given proper access key
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
    Given proper access key
    And setting base currency to "unknown"
    When getting latest rates
    Then should return 400 status code

  Scenario: Response contains invalid_base_currency code when provided unknown base currency
    Given proper access key
    And setting base currency to "unknown"
    When getting latest rates
    Then should return invalid_base_currency code

  Scenario: Default list of exchange rates currencies contains all available currencies
    Given proper access key
    And preparing request without provided currencies to receive
    When getting latest rates
    Then should return all available currencies

  Scenario: Each returned exchange currency contains proper rate
    Given proper access key
    And preparing request without provided currencies to receive
    When getting latest rates
    Then should return exchange rate for all available currencies

  Scenario: Returns only required currency codes
    Given proper access key
    And setting currency codes to receive to "USD,PLN"
    When getting latest rates
    Then should return exchange rate only for "USD,PLN" currencies

  Scenario: Response has 400 status code when provided unknown exchange currency
    Given proper access key
    And setting currency codes to receive to "UNKNOWN"
    When getting latest rates
    Then should return 400 status code

  Scenario: Response contains invalid_currency_codes code when provided unknown exchange currency
    Given proper access key
    And setting currency codes to receive to "UNKNOWN"
    When getting latest rates
    Then should return invalid_currency_codes code

  Scenario: Returns only known exchange currencies when among provided currencies is unknown symbol
    Given proper access key
    And setting currency codes to receive to "GBP,BOB,UNKNOWN"
    When getting latest rates
    Then should return exchange rate only for "GBP,BOB" currencies