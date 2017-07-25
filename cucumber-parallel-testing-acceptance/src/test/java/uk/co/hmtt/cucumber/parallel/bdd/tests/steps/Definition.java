package uk.co.hmtt.cucumber.parallel.bdd.tests.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class Definition {

    private Long delay;
    private Date startTime;
    private Date finishTime;

    @Given("^a test that takes (\\d+) seconds to execute$")
    public void a_test_that_takes_seconds_to_execute(int second) throws Throwable {
        delay = (long) second;
    }

    @When("^I execute the test$")
    public void i_execute_the_test() throws Throwable {
        startTime = Calendar.getInstance().getTime();
        Thread.sleep(delay * 1000); // NOSONAR
        finishTime = Calendar.getInstance().getTime();
    }

    @Then("^it should take at least the execution time to complete$")
    public void it_should_take_at_least_the_execution_time_to_complete() throws Throwable {
        final long startTimeSeconds = startTime.getTime() / 1000;
        final long finishTimeSeconds = finishTime.getTime() / 1000;
        assertThat(finishTimeSeconds - startTimeSeconds, is(greaterThanOrEqualTo(delay)));
    }


}
