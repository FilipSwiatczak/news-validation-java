![Build status](https://github.com/FilipSwiatczak/news-validation-java/actions/workflows/maven.yml/badge.svg?event=push)
# News Validation Test Project


Unit Tests:

```shell
mvn -Dtest="com.news.validation.analysis.**" test
```
To run Cucumber tests:
```shell
mvn -Dtest="com.news.validation.cucumber.RunCucumberTest" test
```

IntelliJ Cucumber Java configuration Run looks good too as it separates agent logs

## Objective
To read news articles from the Guardian news page (https://www.theguardian.com/tone/news)

and validate if the news are valid or fake

It does so by running checks against search engines and validating number of keyword hits against desired thresholds
## Features
- Java 17
- Cucumber
- Spring Boot
- Dockerized Selenium Grid (currently broken)

## TODO Improvements list
- fix selenium grid
- scrape article bodies and store alongside
- connect an open-source API endpoint for non web based validation
- connect actual NLP model for real time analysis
- add semantic versioning and CHANGELOG.md generation
- add vulnerability scanning

