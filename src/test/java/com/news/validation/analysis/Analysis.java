package com.news.validation.analysis;

import org.junit.jupiter.api.Test;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// THIS WORKS FOR CUCUMBER INTELLIJ IDEA RUNNER BECAUSE IT USES io.cucumber.core.cli.Main
// BY CONTRAST APPLYING @Scope("cucumber-glue") AT CONFIGURATION LEVEL WORKS FOR BOTH CUCUMBER AND JUNIT/MVN CLI RUNNERS
//@Component
//@ScenarioScope
public class Analysis {
    public ArrayList<String> articleTitles = new ArrayList<String>();
    public HashMap<String, Map<String, Integer>> articleResultsMap = new HashMap<String, Map<String, Integer>>();

    public Map<String, ArrayList<String>> googleSearchResultsPerArticleMap = new HashMap<String, ArrayList<String>>();

    public void storeGoogleSearchResult(String articleTitle, ArrayList<String> googleSearchResults) {
        googleSearchResultsPerArticleMap.put(articleTitle, googleSearchResults);
    }

    public void analyseGoogleSearchResults(int includeNoOfResults) {
        for (String articleTitle : googleSearchResultsPerArticleMap.keySet().stream().limit(includeNoOfResults).toList()) {
            Map<String, Integer> keywordCountMap = new HashMap<String, Integer>();

            // Initial keyword map created by splitting the article title into keywords
            for (String word : splitTitleIntoWords(articleTitle)) {
                if (!keywordCountMap.containsKey(word)) {
                    keywordCountMap.put(word, 0);
                }
            }

            // counting the number of hits for each keyword across google search results
            ArrayList<String> googleSearchResults = googleSearchResultsPerArticleMap.get(articleTitle);
            for (String googleSearchResultTitle : googleSearchResults) {
                for (String word : splitTitleIntoWords(googleSearchResultTitle)) {
                    if (keywordCountMap.containsKey(word)) {
                        keywordCountMap.put(word, keywordCountMap.get(word) + 1);
                    }
                }
            }
            articleResultsMap.put(articleTitle, keywordCountMap);
        }
    }

    private ArrayList<String> splitTitleIntoWords(String title) {
        // split title by spaces and filter out strings with length less than 3
        return Arrays.stream(title.split(" "))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Pair<Boolean, String> validateArticleMatchCount(int keywordNo, int threshold) {
        StringBuilder logMessage = new StringBuilder();
        boolean pass = true;
        for (String articleTitle : articleResultsMap.keySet()) {
            int passingKeywordCount = 0;
            Map<String, Integer> keywordCountMap = articleResultsMap.get(articleTitle);

            logMessage.append("Article ").append(articleTitle).append("\n").append(String.format("Keyword hits across %d articles: \n", keywordCountMap.size()));
            for (String keyword : keywordCountMap.keySet()) {
                if (keywordCountMap.get(keyword) >= threshold) {
                    logMessage.append("✔ \"").append(keyword)
                            .append("\" ")
                            .append(keywordCountMap.get(keyword))
                            .append(" > ")
                            .append(threshold)
                            .append("\n");
                    passingKeywordCount++;
                } else {
                    logMessage.append("❌ \"").append(keyword)
                            .append("\" ")
                            .append(keywordCountMap.get(keyword))
                            .append(" < ")
                            .append(threshold)
                            .append("\n");
                }
            }
            if (passingKeywordCount >= keywordNo) {
                logMessage.append(String.format("PASS: \"%s\"\n %d/%d keywords >= %d hits each\n", articleTitle, passingKeywordCount, keywordNo, threshold));
            } else {
                pass = false;
                logMessage.append(String.format("FAIL: \"%s\"\n %d/%d keywords >= %d hits each\n", articleTitle, passingKeywordCount, keywordNo, threshold));
            }
        }
        if (articleResultsMap.keySet().size() == 0) {
            logMessage.append("No results to analyse");
        }
        return new Pair<Boolean, String>(pass, logMessage.toString());
    }

}
