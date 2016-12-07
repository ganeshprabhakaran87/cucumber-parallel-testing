package uk.co.hmtt.parallel.bdd.runner;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import uk.co.hmtt.cucumber.parallel.runner.ParallelCucumber;

@RunWith(ParallelCucumber.class)
@CucumberOptions(glue = {"uk.co.hmtt.parallel.bdd.tests"},
        format = {"pretty", "html:target/cucumber/cucumber-html-report-1", "json:target/cucumber/json/cucumber-1.json"},
        features = {"src/test/resources/features"},
        tags = {"~@wip"})
public class FirstParallelTestRunner {}
