package uk.co.hmtt.cucumber.parallel.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.hmtt.cucumber.parallel.model.Recorder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static uk.co.hmtt.cucumber.parallel.system.FeatureManager.featureNotAlreadyRun;

@RunWith(MockitoJUnitRunner.class)
public class FeatureManagerTest {

    @Mock
    private SynchronisedFile<Recorder> synchronisedFile;

    @InjectMocks
    private FeatureManager testee;

    @Test
    public void shouldReturnTrueIfTheFeatureHasNotAlreadyBeenRun() throws Exception {
        mockSynchronisedField("test");
        assertThat(featureNotAlreadyRun("test_feature"), is(true));
    }

    @Test
    public void shouldReturnFalseIfTheFeatureHasNotAlreadyBeenRun() throws Exception {
        mockSynchronisedField("test_feature");
        assertThat(featureNotAlreadyRun("test_feature"), is(false));
    }

    private void mockSynchronisedField(final String feature) throws Exception {
        final Field synchronisedField = FeatureManager.class.getDeclaredField("synchronisedFile");
        setFinalStatic(synchronisedField, synchronisedFile);

        final Recorder recorder = new Recorder();
        recorder.getFeatures().add(feature);

        when(synchronisedFile.read(Recorder.class)).thenReturn(recorder);
    }

    private void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}