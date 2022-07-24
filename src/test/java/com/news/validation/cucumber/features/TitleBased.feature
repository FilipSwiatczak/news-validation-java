Feature: Title based article validation

  Background:
    Given I have navigated to the guardian homepage

  Scenario: Google - Single Article - Title has 3 matching keywords over 2+ results
    And I have recorded first article title
    When I store google search results for each article title
    Then at least 3 keywords are confirmed 2 times in space of 10 articles

  Scenario: IsThisCredible.com - Single Article - Title has 3 matching keywords over 2+ results
    And I have recorded first article title
    When I store IsThisCredible.com results for this article title
    Then at least 2 articles are Moderately Credible (50-75% match)
    And at least 1 article is Highly Credible (76-100% match)

  Scenario: Google - Multiple Articles - Title has 3 matching keywords over 2+ results
    And I have recorded all the article titles
    When I store google search results for each article title
    Then at least 2 keywords are confirmed 2 times in space of 10 articles
