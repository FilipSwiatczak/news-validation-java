package com.news.validation.cucumber.steps;

import com.news.validation.analysis.Analysis;
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

public class CommonSteps {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @LazyAutowired
    Analysis analysis;

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

    @And("I have recorded first article title")
    public void iHaveRecordedFirstArticleTitle() {
        homePage.recordFirstArticleTitle();
    }
}
