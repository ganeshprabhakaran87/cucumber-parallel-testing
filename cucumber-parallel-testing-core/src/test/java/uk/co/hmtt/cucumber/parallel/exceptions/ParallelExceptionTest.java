package uk.co.hmtt.cucumber.parallel.exceptions;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ParallelExceptionTest {

    public static final String AN_EXCEPTION = "an exception";

    @Test
    public void shouldCreateAnExceptionUsingAStringDescription() {
        final ParallelException parallelException = new ParallelException(AN_EXCEPTION);
        assertThat(parallelException.getMessage(), is(AN_EXCEPTION));
    }

    @Test
    public void shouldCreateAnExceptionUsingAThrowable() {
        final ParallelException parallelException = new ParallelException(new RuntimeException(AN_EXCEPTION));
        assertThat(parallelException.getCause().getMessage(), is(AN_EXCEPTION));
    }

}