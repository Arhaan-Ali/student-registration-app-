package com.student;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
class StudentServerTest {
    @Test void formHasAllRequiredFields() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/index.html")) {
            assertNotNull(in, "index.html must be on classpath");
            String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(html.contains("name=\"studentName\""));
            assertTrue(html.contains("name=\"rollNumber\""));
            assertTrue(html.contains("name=\"email\""));
            assertTrue(html.contains("name=\"course\""));
            assertTrue(html.contains("type=\"submit\""));
        }
    }
    @Test void parseReadsSubmittedValues() throws Exception {
        var f = StudentServer.parse("studentName=Asha+Patel&rollNumber=101&email=asha%40test.com&course=BCA");
        assertEquals("Asha Patel", f.get("studentName"));
        assertEquals("asha@test.com", f.get("email"));
    }
}
