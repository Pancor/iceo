Feature: API tests for obtaining latest rate data from https://api.exchangeratesapi.io/v1

  Scenario: Response has 200 status code when providing only access key
    Given proper access key
    When getting latest rates
    Then should return 200 status code
