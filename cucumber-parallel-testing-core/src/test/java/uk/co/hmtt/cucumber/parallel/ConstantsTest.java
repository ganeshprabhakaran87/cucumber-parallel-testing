package uk.co.hmtt.cucumber.parallel;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static uk.co.hmtt.cucumber.parallel.utils.TestHelper.coverPrivateConstructor;

public class ConstantsTest {

    @Test
    public void shouldHaveAPrivateConstructor() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        coverPrivateConstructor(Constants.class);
    }

}