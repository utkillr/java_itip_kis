package util;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class LogTest {



    @Before
    public void setUp() {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
    }

    @Test
    public void logTest() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(bo);
        Log log = new Log("TEST", stream);

        log.info("info");
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        assertTrue(allWrittenLines.contains("[TEST]: [INFO] info"));

        log.response("response");
        bo.flush();
        allWrittenLines = new String(bo.toByteArray());
        assertTrue(allWrittenLines.contains("[TEST]: [RESPONSE] response"));

        log.warn("warn");
        bo.flush();
        allWrittenLines = new String(bo.toByteArray());
        assertTrue(allWrittenLines.contains("[TEST]: [WARN] warn"));

        log.error("error");
        bo.flush();
        allWrittenLines = new String(bo.toByteArray());
        assertTrue(allWrittenLines.contains("[TEST]: [ERROR] error"));
    }
}
