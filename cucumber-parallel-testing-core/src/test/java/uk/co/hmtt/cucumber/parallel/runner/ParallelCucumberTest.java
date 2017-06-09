package uk.co.hmtt.cucumber.parallel.runner;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.co.hmtt.cucumber.parallel.system.FeatureManager;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FeatureManager.class)
public class ParallelCucumberTest {

    @Mock
    private FeatureRunner featureRunner;

    private ParallelCucumber parallelCucumber;

    @Test
    public void shouldRunTheTestIfItHasNotBeenRunAlready() throws IOException, InitializationError, NoSuchFieldException, IllegalAccessException {
        mockStatic(FeatureManager.class);
        given(FeatureManager.featureNotAlreadyRun(anyString())).willReturn(true);
        parallelCucumber.runChild(featureRunner, new RunNotifier());
        verify(featureRunner, times(1)).run(any(RunNotifier.class));
    }

    @Test
    public void shouldNotRunTheTestIfItHasBeenRunAlready() throws IOException, InitializationError, IllegalAccessException, NoSuchFieldException {
        mockStatic(FeatureManager.class);
        given(FeatureManager.featureNotAlreadyRun(anyString())).willReturn(false);
        parallelCucumber.runChild(featureRunner, new RunNotifier());
        verify(featureRunner, never()).run(any(RunNotifier.class));
    }

    @Before
    public void init() throws IOException, InitializationError {
        parallelCucumber = new ParallelCucumber(Cucumber.class);
        initMocks(parallelCucumber);
    }

}