package uk.co.hmtt.cucumber.parallel.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.hmtt.cucumber.parallel.Constants;
import uk.co.hmtt.cucumber.parallel.exceptions.ParallelException;
import uk.co.hmtt.cucumber.parallel.model.Recorder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SynchronisedFileTest {

    final static String DIR = Constants.PARALLEL_WORKING_DIR + Recorder.class.getName() + ".lock";

    @Mock
    private RandomAccessFile randomAccessFile;

    @Mock
    private FileChannel fileChannel;

    @InjectMocks
    private SynchronisedFile<Recorder> synchronizedWithMocks;

    @Test
    public void shouldCreateANewFileAndReturnItAsAnObjectIfItDoesNotAlreadyExist() {
        final SynchronisedFile<Recorder> recorderSynchronisedFile = new SynchronisedFile<>();
        final Recorder read = recorderSynchronisedFile.read(Recorder.class);
        assertThat(read.getFeatures(), is(Collections.<String>emptySet()));
    }

    @Test
    public void shouldReadAnExistingFileAndReturnItAsAnObject() throws IOException {
        final RandomAccessFile file = new RandomAccessFile(DIR, "rw");
        final Recorder recorder = new Recorder();
        recorder.getFeatures().add("name");
        file.writeUTF(new ObjectMapper().writeValueAsString(recorder));

        final SynchronisedFile<Recorder> recorderSynchronisedFile = new SynchronisedFile<>();
        final Recorder read = recorderSynchronisedFile.read(Recorder.class);

        assertThat(read, is(notNullValue()));
        assertThat(read.getFeatures().size(), is(equalTo(1)));

    }

    @Test(expected = ParallelException.class)
    public void shouldThrowAParallelExceptionIfAFileCannotBeRead() throws IOException {
        when(randomAccessFile.length()).thenReturn(1L);
        when(randomAccessFile.readUTF()).thenThrow(IOException.class);
        synchronizedWithMocks.read(Recorder.class);
    }

    @Test
    public void shouldWriteDataToTheFile() throws IOException {
        final SynchronisedFile<Recorder> recorderSynchronisedFile = new SynchronisedFile<>();
        final Recorder read = recorderSynchronisedFile.read(Recorder.class);
        read.getFeatures().add("name");
        recorderSynchronisedFile.write(read);
        final RandomAccessFile file = new RandomAccessFile(DIR, "rw");
        assertThat(file.readUTF(), is(equalTo("{\"features\":[\"name\"]}")));
    }

    @Before
    public void init() {
        delete();
    }

    @AfterClass
    public static void tearDown() {
        delete();
    }

    private static void delete() {
        final File file = new File(DIR);
        file.delete();
    }

}
