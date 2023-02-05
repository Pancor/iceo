Feature: Tests involving authorization and authentication features of /latest endpoint

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

  Scenario: Response contains Access-Control-Allow-Origin header
    Given proper access key
    When getting latest rates
    Then response should contain header named "Access-Control-Allow-Origin"

  @failing
  Scenario: Response contains Access-Control-Allow-Headers header
    According to CORS Access-Control-Allow-Headers header should be included
    Given proper access key
    When getting latest rates
    Then response should contain header named "Access-Control-Allow-Headers"

  Scenario: Response contains Access-Control-Allow-Methods header
    Given proper access key
    When getting latest rates
    Then response should contain header named "Access-Control-Allow-Methods"

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
