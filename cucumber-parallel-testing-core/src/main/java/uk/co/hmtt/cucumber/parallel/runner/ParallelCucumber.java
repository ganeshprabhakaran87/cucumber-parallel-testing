package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.hmtt.cucumber.parallel.Constants;
import uk.co.hmtt.cucumber.parallel.model.Recorder;

import java.io.IOException;

import static uk.co.hmtt.cucumber.parallel.system.FeatureManager.featureNotAlreadyRun;
import static uk.co.hmtt.cucumber.parallel.system.FileSystemHelper.deleteFile;

public class ParallelCucumber extends Cucumber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelCucumber.class);
    private static Boolean initialised = Boolean.FALSE;

    public ParallelCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
        removeFileLockFromPreviousRun();
    }

    private void removeFileLockFromPreviousRun() {
        synchronized (this) {
            if (!initialised) {
                deleteFile(Constants.PARALLEL_WORKING_DIR + Recorder.class.getName() + ".lock");
                initialised = Boolean.TRUE;
            }
        }
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        if (featureNotAlreadyRun(child.getName()) ) {
            LOGGER.debug("Feature file not already processed. Running feature file.");
            child.run(notifier);
        }
    }

}
