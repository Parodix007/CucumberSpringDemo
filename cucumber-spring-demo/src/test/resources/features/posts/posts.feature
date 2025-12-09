Feature: CRUD Operations for posts API
  I want to test whether all CRUD operations works properly on posts API

  Background: Auth
    Given Authorize a user with
      | username | password   |
      | emilys   | emilyspass |

  Scenario Outline: I want to get a post
    Given Create a request data with "<id>"
    When Make a request for a post
    Then Response should has "<id>" and "<title>"
    And Response should be "<response_code>"
    Examples:
      | id | title                            | response_code |
      | 1  | His mother had always taught him | 200           |

  Scenario: I want to create a post
    Given Create a post metadata with
    """json
    {
      "title":"Some-title",
      "reactions": {
        "likes":  300,
        "dislikes":  9
      },
     "views": 1000
    }
    """
    When Make a request to create a post
    Then Respones metadata should match created post
    And Response should be 201

  Scenario Outline: I want to update a post
    Given Create a post metadata with
    """json
    {
      "title":"Some-title",
      "reactions": {
        "likes":  300,
        "dislikes":  9
      },
     "views": 1000
    }
    """
    When Make a request to update a post "<id>"
    Then Respones metadata should match updated post
    And Response should be "<response_code>"

    Examples:
    | id | response_code |
    | 1  | 200           |

  Scenario Outline: I want to delete a post
    When Make a request to delete a post "<id>"
    Then Response should be "<response_code>"
    Examples:
      | id | response_code |
      | 1  | 200           |

  Scenario Outline: I want to get all posts for specific users

  Scenario Outline: I want to get posts with a pagination