package com.news.validation.pages;

import com.news.validation.analysis.Analysis;
import com.news.validation.analysis.TestData;
import com.news.validation.annotations.LazyAutowired;
import com.news.validation.annotations.LazyComponent;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@LazyComponent
public class HomePage extends BasePage {

    @Autowired
    private Analysis analysis;
    @Autowired
    private TestData testData;

    @Value("${application.url}")
    private String baseURL;

    //*********Web Elements By Using Page Factory*********
    @FindBy(how = How.CLASS_NAME, using = "btnSignIn")
    public WebElement signInButton;

    By homePageLogo = By.cssSelector(".logo.home");
    By allArticleTitles = By.cssSelector("div > div > div > ul > li > div > div > a");
    By googleSearchResults = By.cssSelector(" div > div > div > div > div > div.g > div > div > div > a > h3");
    By googleSearchAcceptAll = By.cssSelector("div[aria-label=\"Before you continue to Google Search\"] button:nth-child(2):not([role=\"link\"]) > div[role=\"none\"]");

    By credibleSearchBox = By.cssSelector("input[placeholder=\"Search Topic or type a URL\"]");
    By credibleSearchButton = By.cssSelector("button.search-button");

    By credibleScoreGrades = By.cssSelector("button.score-button");

    By credibleResultsLegend = By.cssSelector("div.legend > div.legend-row");

    //*********Page Methods*********
    //Go to Homepage
    public HomePage goToHomePage() {
        driver.get(baseURL);
        return this;
    }
    public HomePage goToTheGuardian() {
        driver.get("https://www.theguardian.com/tone/news");
        return this;
    }

    public HomePage recordAllArticleTitles() {
        waitElements(allArticleTitles);
        driver.findElements(allArticleTitles).forEach(element -> {
            String text = element.getAccessibleName();
            logger.info(text);
            analysis.articleTitles.add(text);
        });
        System.out.println(analysis.articleTitles);
        return this;
    }
    public HomePage recordFirstArticleTitle() {
        waitElements(allArticleTitles);
        String text = driver.findElements(allArticleTitles).get(0).getAccessibleName();
        logger.info(text);
        analysis.articleTitles.add(text);
        return this;
    }

    public HomePage searchGoogleForEachArticleTitle() {
        logger.info("Searching Google for each article title: " + analysis.articleTitles);
        driver.get("https://www.google.com");
        try {
            handlePopup(googleSearchAcceptAll);
            logger.info("Accepted Google Search Popup");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        analysis.articleTitles.forEach(title -> {
            driver.get("https://www.google.com/search?q=" + title);
            waitElements(googleSearchResults);
            ArrayList<String> googleSearchResultsTitles = new ArrayList<String>();
            driver.findElements(googleSearchResults).forEach(element -> {
                String text = element.getText();
                logger.info(text);
                googleSearchResultsTitles.add(text);
            });
            analysis.storeGoogleSearchResult(title, googleSearchResultsTitles);
        });
        return this;
    }

    //Go to LoginPage
    public HomePage goToLoginPage() {
        click(signInButton);
        return this;
    }

    @Override
    public boolean isAt() {
        return this.wait.until((d) -> this.signInButton.isDisplayed());
    }

    public HomePage verifyThatIAmAtHomePage() {
        Assertions.assertTrue(driver.findElement(homePageLogo).isDisplayed());
        return this;

    }

    public void searchIsThisCredibleForThisArticleTitle() {
        driver.get("https://www.isthiscredible.com/");
        waitElement(credibleSearchBox);
        writeText(credibleSearchBox, analysis.articleTitles.get(analysis.articleTitles.size() - 1));
        // test
//        writeText(credibleSearchBox, "Dover travel chaos enters third day with long queues for Eurotunnel");
        click(credibleSearchButton);

        waitElement(credibleResultsLegend);
        List<WebElement> scoreGradeButtons = driver.findElements(credibleScoreGrades);
        if (scoreGradeButtons.size() == 0) {
            logger.info("No credible results found");
        } else {
            driver.findElements(credibleScoreGrades).forEach(element -> {
                int score = Integer.parseInt(
                        element.getText()
                                .replaceAll("[^0-9]", ""));
                testData.credibleScoreGradesList.add(score);
            });
            logger.info(testData.credibleScoreGradesList.toString());
        }
    }
}