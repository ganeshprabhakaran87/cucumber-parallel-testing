package uk.co.hmtt.cucumber.parallel.bdd.runner;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import uk.co.hmtt.cucumber.parallel.runner.ParallelCucumber;

@RunWith(ParallelCucumber.class)
@CucumberOptions(glue = {"uk.co.hmtt.cucumber.parallel.bdd.tests"},
        format = {"pretty", "html:target/cucumber/cucumber-html-report-3", "json:target/cucumber/json/cucumber-3.json"},
        features = {"src/test/resources/features"},
        tags = {"~@wip"})
public class ThirdParallelTestRunner {
}
