# News Validation Test Project

To run Cucumber tests:
```shell
mvn -Dtest="com.news.validation.cucumber.RunCucumberTest" test
```

## Objective
To read news articles from the Guardian news page (https://www.theguardian.com/tone/news)

and validate if the news are valid or fake

It does so by running checks against search engines and validating number of keyword hits against desired thresholds
## Features
- Java 18
- Cucumber
- Spring Boot
- Dockerized Selenium Grid


IntelliJ Cucumber Java configuration Run looks good too.