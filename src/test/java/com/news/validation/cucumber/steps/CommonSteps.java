package com.news.validation.cucumber.steps;

import com.news.validation.analysis.Analysis;
import com.news.validation.analysis.TestData;
import com.news.validation.annotations.LazyAutowired;
import com.news.validation.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.internal.collections.Pair;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonSteps {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Analysis analysis;

    @Autowired
    private TestData testData;

    @LazyAutowired
    private HomePage homePage;

    @Given("I have navigated to the guardian homepage")
    public void iHaveNavigatedToTheGuardianHomepage() {
        homePage.goToTheGuardian();
    }

    @And("I have recorded all the article titles")
    public void iHaveRecordedAllTheArticleTitles() {
        homePage.recordAllArticleTitles();
    }

    @When("I store google search results for each article title")
    public void iStoreGoogleSearchResultsForEachArticleTitle() {
         homePage.searchGoogleForEachArticleTitle();
    }

    @Then("at least {int} keywords are confirmed {int} times in space of {int} articles")
    public void atLeastKeywordsAreConfirmedTimesInSpaceOfArticles(int keywordsNo, int confirmationsNo, int articlesNo) {
        analysis.analyseGoogleSearchResults(articlesNo);
        Pair<Boolean, String> result = analysis.validateArticleMatchCount(keywordsNo, confirmationsNo);
        assertEquals(result.first(), true, result.second());
        logger.info(result.second());
    }

    @When("I store IsThisCredible.com results for this article title")
    public void iStoreIsThisCredibleComResultsForThisArticleTitle() {
        homePage.searchIsThisCredibleForThisArticleTitle();
    }

    @And("I have recorded first article title")
    public void iHaveRecordedFirstArticleTitle() {
        homePage.recordFirstArticleTitle();
    }

    @Then("at least {int} articles are Moderately Credible \\(50-75% match)")
    public void atLeastArticlesAreModeratelyCredibleMatch(int i) {
        // assert that homePage.credibleScoreGradesList contains at least i elements higher than 50
        assertTrue(testData.credibleScoreGradesList.stream().filter(x -> x > 50).count() >= i);
    }

    @And("at least {int} article is Highly Credible \\(76-100% match)")
    public void atLeastArticleIsHighlyCredibleMatch(int i) {
        // assert that homePage.credibleScoreGradesList contains at least i elements higher than 75
        assertTrue(testData.credibleScoreGradesList.stream().filter(x -> x > 75).count() >= i);
    }
}
