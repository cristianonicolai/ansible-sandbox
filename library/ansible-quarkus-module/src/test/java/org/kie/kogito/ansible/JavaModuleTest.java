package org.kie.kogito.ansible;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.QuarkusMainTest;

@QuarkusMainTest
public class JavaModuleTest {

    @Test
    @Launch(value = {}, exitCode = 1)
    public void testLaunchCommandFailed() {
    }


}
