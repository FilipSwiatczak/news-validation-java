package com.news.validation.analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisTest {

    Analysis analysis;

    @BeforeEach
    void setUp() {
        analysis = new Analysis();
    }

    // KEYWORD MAPPING
    @Test
    void emptySearchResults() {
        analysis.googleSearchResultsPerArticleMap.put("Sample title", new ArrayList<String>());
        analysis.analyseGoogleSearchResults(1);
        assertEquals(1, analysis.articleResultsMap.size());
        assertEquals(2, analysis.articleResultsMap.get("Sample title").size());
    }
    @Test
    void basicMapping() {
        String articleTitle = "Sample title Sun Moon Planet";
        analysis.googleSearchResultsPerArticleMap.put(articleTitle, new ArrayList<String>());
        analysis.analyseGoogleSearchResults(1);

        assertEquals(Set.of(articleTitle), analysis.articleResultsMap.keySet() );
        assertEquals(5, analysis.articleResultsMap.get(articleTitle).size());
        assertEquals(0, analysis.articleResultsMap.get(articleTitle).get("Sun"));
    }
    @Test
    void keywordsShorterThenTwoCharsAreSkipped() {
        String articleTitle = "Sample AA title Sun Moon Planet BB to me";
        analysis.googleSearchResultsPerArticleMap.put(articleTitle, new ArrayList<String>());
        analysis.analyseGoogleSearchResults(1);
        assertEquals(5, analysis.articleResultsMap.get(articleTitle).size());
    }
    @Test
    void basicMappingWithResults() {
        String articleTitle = "Sample title Sun Moon Planet";
        ArrayList<String> googleSearchResults = new ArrayList<String>();
        googleSearchResults.add("Sun is a Planet - should yield 2 hits");
        googleSearchResults.add("Moon is a Planet - should yield 2 hits");
        googleSearchResults.add("Planet is a Planet - should yield a double hit");

        analysis.storeGoogleSearchResult(articleTitle, googleSearchResults);
        analysis.analyseGoogleSearchResults(1);
        assertEquals(1, analysis.articleResultsMap.get(articleTitle).get("Sun"));
        assertEquals(1, analysis.articleResultsMap.get(articleTitle).get("Moon"));
        assertEquals(4, analysis.articleResultsMap.get(articleTitle).get("Planet"));
    }
    @Test
    void multipleMappingsWithResults() {
        String articleTitle1 = "Sample title Sun Moon Planet";
        String articleTitle2 = "Dog Cat little zoo";
        ArrayList<String> googleSearchResults1 = new ArrayList<String>();
        googleSearchResults1.add("Sun is a Planet - should yield 2 hits");
        googleSearchResults1.add("Moon is a Planet - should yield 2 hits");
        googleSearchResults1.add("Planet is a Planet - should yield a double hit");
        ArrayList<String> googleSearchResults2 = new ArrayList<String>(); // second search query returns no results here

        analysis.storeGoogleSearchResult(articleTitle1, googleSearchResults1);
        analysis.storeGoogleSearchResult(articleTitle2, googleSearchResults2);
        analysis.analyseGoogleSearchResults(3);
        assertEquals(1, analysis.articleResultsMap.get(articleTitle1).get("Sun"));
        assertEquals(1, analysis.articleResultsMap.get(articleTitle1).get("Moon"));
        assertEquals(4, analysis.articleResultsMap.get(articleTitle1).get("Planet"));
        assertEquals(0, analysis.articleResultsMap.get(articleTitle2).get("Dog"));
        assertEquals(0, analysis.articleResultsMap.get(articleTitle2).get("Cat"));
    }
    // KEYWORD ANALYSIS
    @Test
    void analyseSkipsOnEmptyResults() {
        Pair<Boolean, String> result = analysis.validateArticleMatchCount(0, 0);
        assertEquals(result.first(), true, result.second());
        assertEquals(result.second(), "No results to analyse");
    }
}
