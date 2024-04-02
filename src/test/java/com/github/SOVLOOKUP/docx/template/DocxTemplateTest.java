package com.github.SOVLOOKUP.docx.template;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

public class DocxTemplateTest {
    DocxTemplate dt;
    {
        this.dt = new DocxTemplate();
    }

    @Test
    public void test() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/data.json")));
        this.dt.run("src/test/resources/template.docx", "target/out.docx", content);
    }

    @Test
    public void test2() throws IOException, InterruptedException, ExecutionException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/data.json")));
        File file = new File("src/test/resources/template.docx");
        File out = new File("target/out2.docx");
        OutputStream output = new FileOutputStream(out);

        try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
            try (InputStream input = new FileInputStream(file)) {
                byte[] byt = new byte[input.available()];
                input.read(byt);

                bufferedOutput.write(this.dt.run_byte(byt, content));
            }
        }
    }
}
