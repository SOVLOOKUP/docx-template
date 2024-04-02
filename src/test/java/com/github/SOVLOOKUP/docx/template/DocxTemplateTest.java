package com.github.SOVLOOKUP.docx.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

public class DocxTemplateTest {
    DocxTemplate dt;
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    {
        this.dt = new DocxTemplate();
    }

    @Test
    public void test() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/data.json")));
        this.dt.renderFile("src/test/resources/template.docx", "target/out.docx", content);
    }

    @Test
    public void test2() throws IOException, InterruptedException,
            ExecutionException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/data.json")));
        String file = encoder.encodeToString(Files.readAllBytes(Paths.get("src/test/resources/template.docx")));
        String out = this.dt.render(file, content);
        Files.write(Paths.get("target/out2.docx"), decoder.decode(out));
    }
}
