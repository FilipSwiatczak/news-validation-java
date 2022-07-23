Feature: Title based article validation

  Background:
    Given I have navigated to the guardian homepage

  Scenario: Main Task - Single Article - Google - Title has 3 matching keywords over 2+ results
    And I have recorded first article title
    When I store google search results for each article title
    Then at least 3 keywords are confirmed 2 times in space of 10 articles

  Scenario: Main Task - Multiple Articles - Google - Title has 3 matching keywords over 2+ results
    And I have recorded all the article titles
    When I store google search results for each article title
    Then at least 3 keywords are confirmed 2 times in space of 10 articles
