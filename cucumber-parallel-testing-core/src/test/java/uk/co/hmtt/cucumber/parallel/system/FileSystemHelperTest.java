package uk.co.hmtt.cucumber.parallel.system;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class FileSystemHelperTest {

    private static final String FOLDER = System.getProperty("user.dir") + "/target/parallel/";
    private static final String FILE = "lock.txt";

    @Before
    public void init() {
        deleteFolder();
    }

    @Test
    public void shouldCreateFolderIfItDoesNotAlreadyExist() throws IOException {
        final File file = new File(FOLDER);
        assertThat(file.exists(), is(false));
        FileSystemHelper.createFolder(FOLDER);
        assertThat(file.exists(), is(true));
    }

    @Test
    public void shouldNotCreateFolderIfItAlreadyExists() {
        final File fileBefore = createFolder(FOLDER);
        FileSystemHelper.createFolder(FOLDER);
        final File fileAfter = new File(FOLDER);
        assertThat("Created date should not have changed", fileBefore.lastModified(), is(equalTo(fileAfter.lastModified())));
    }

    @Test
    public void shouldDeleteFileIfItExists() throws IOException {
        FileSystemHelper.createFolder(FOLDER);
        final File file = new File(FOLDER + FILE);
        final boolean newFileCreated = file.createNewFile();
        assertThat(newFileCreated, is(true));
        FileSystemHelper.deleteFile(FOLDER + FILE);
        assertThat(file.exists(), is(false));
    }

    private File createFolder(final String path) {
        final File fileBefore = new File(path);
        final boolean directoryCreatedByTest = fileBefore.mkdirs();
        assertThat(directoryCreatedByTest, is(true));
        return fileBefore;
    }

    @AfterClass
    public static void tearDown() {
        deleteFolder();
    }

    private static void deleteFolder() {
        new File(FOLDER).delete();
    }

}