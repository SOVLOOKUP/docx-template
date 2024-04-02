package com.github.SOVLOOKUP.docx.template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class DocxTemplateTest {
    @Test
    public void test() throws IOException {
        DocxTemplate dt = new DocxTemplate();
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/data.json")));

        dt.run("src/test/resources/template.docx", "target/out.docx", content);
    }
}
