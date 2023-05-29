package org.kie.kogito.ansible;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import io.quarkus.test.junit.main.QuarkusMainTest;

@QuarkusMainTest
public class PlaybookWorkflowTest {

    @Test
    public void testLaunchCommand(QuarkusMainLauncher launcher) {
        LaunchResult result = launcher.launch("playbook-workflow-test.json");
        Assertions.assertEquals(0, result.exitCode());
    }


}
