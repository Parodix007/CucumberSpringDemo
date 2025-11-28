Feature: Authorization using JWT
  I want to know whether authorization complains business requirements

  Scenario Outline: Are my credentials correct?
    Given Create a user data using "<username>" and "<password>"
    When Make a request for JWT
    Then Response should be "<response_code>"

    Examples:
      | username | password     | response_code |
      | emilys   | emilyspass   | 200           |
      | emilys   | incorrect    | 400           |
      | michaelw | michaelwpass | 200           |
      | michaelw | incorrect    | 400           |

  Scenario Outline: What happens when I use someone else credentials to get a token?
    Given Create a user data using "<username>" and "<password>"
    When Make a request for JWT
    Then Response should be "<response_code>"

    Examples:
      | username | password     | response_code |
      | emilys   | michaelwpass | 400           |
      | michaelw | emilyspass   | 400           |

  Scenario Outline: Can I get my correct data with JWT?
    Given Create a user data using "<username>" and "<password>"
    When Make a request for JWT and user metadata
    Then Response should contains "<username>" and "<password>" and "<id>"

    Examples:
      | username | password     | id |
      | emilys   | emilyspass   | 1  |
      | michaelw | michaelwpass | 2  |