Feature: CRUD operations on recipes API
  I want to test whether all CRUD operations works properly on recipes API

  Background: Auth
    Given Authorize a user with
      | username | password   |
      | emilys   | emilyspass |

  Scenario Outline: Creating a recipe
    And Create a new recipe with
      | name          | tags                 | mealType            | reviewCount |
      | <recipe_name> | [Eggs, Bread, Bacon] | [Breakfast, Supper] | 5           |
      | <recipe_name> | [Potatoes, Cheese]   | [Dinner]            | 5           |
    When Make a request with the new recipe
    Then Response should be "<response_code>"

    Examples:
      | recipe_name   | response_code |
      | Scramble eggs | 200           |
      | Dumplings     | 200           |

  Scenario Outline: Reading a recipe
    Given Get a recipe id "<id>"
    When Make a request for a recipe
    Then The recipe name should match "<name>"
    And Response should be "<response_code>"

    Examples:
      | id | name                     | response_code |
      | 1  | Classic Margherita Pizza | 200           |

  Scenario Outline: Updating a recipe
    Given Create a new recipe model with
      | name          | tags                 | mealType            | reviewCount |
      | <recipe_name> | [Eggs, Bread, Bacon] | [Breakfast, Supper] | 5           |
      | <recipe_name> | [Potatoes, Cheese]   | [Dinner]            | 5           |
    When Make a update request withe the recipe model for "<id>"
    Then Response should be "<response_code>"

    Examples:
      | id | response_code | recipe_name |
      | 1  | 200           | Hard-boiled eggs |
      | 2  | 200           | Potato gratin    |

  Scenario: Delete a recipe
    When Make a delete request for recipe 1
    Then Response should be 200