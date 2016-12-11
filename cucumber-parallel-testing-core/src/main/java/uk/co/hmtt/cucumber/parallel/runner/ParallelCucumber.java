package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static uk.co.hmtt.cucumber.parallel.system.FeatureManager.featureNotAlreadyRun;

public class ParallelCucumber extends Cucumber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCucumber.class);

    public ParallelCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        if (featureNotAlreadyRun(child.getName()) ) {
            LOGGER.debug("Feature file not already processed. Running feature file.");
            child.run(notifier);
        }
    }

}
