package com.news.validation.configuration;

import com.news.validation.analysis.Analysis;
import com.news.validation.analysis.TestData;
import com.news.validation.annotations.LazyConfiguration;
import com.news.validation.annotations.WebdriverScopeBean;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@LazyConfiguration
public class OtherBeansConfigs {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JavascriptExecutor javascriptExecutor(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    @Bean
    @Scope("cucumber-glue")
    public Analysis analysis() {
        return new Analysis();
    }

    @Bean
    @Scope("cucumber-glue")
    public TestData testData() {
        return new TestData();
    }
}