package com.deepoove.poi.cli;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.jsonmodel.support.DefaultGsonHandler;
import com.deepoove.poi.jsonmodel.support.GsonHandler;
import com.deepoove.poi.jsonmodel.support.GsonPreRenderDataCastor;
import com.deepoove.poi.plugin.comment.CommentRenderPolicy;
import com.deepoove.poi.plugin.highlight.HighlightRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderPolicy;
import com.deepoove.poi.plugin.markdown.FileMarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderPolicy;
import com.deepoove.poi.plugin.toc.TOCRenderPolicy;
import com.deepoove.poi.policy.AttachmentRenderPolicy;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class DocxTemplate {
    private GsonHandler gsonHandler;
    private Configure configure;
    {
        ConfigureBuilder builder = Configure.builder();
        this.gsonHandler = new DefaultGsonHandler() {
            @Override
            protected RuntimeTypeAdapterFactory<RenderData> createRenderTypeAdapter(boolean readable) {
                return super.createRenderTypeAdapter(readable).registerSubtype(MarkdownRenderData.class, "markdown")
                        .registerSubtype(HighlightRenderData.class, "code")
                        .registerSubtype(FileMarkdownRenderData.class, "markdown-file");
            }

            @Override
            protected List<RuntimeTypeAdapterFactory<?>> createTypeAdapters(boolean readable) {
                List<RuntimeTypeAdapterFactory<?>> typeAdapter = super.createTypeAdapters(readable);
                typeAdapter.add(RuntimeTypeAdapterFactory.of(MarkdownRenderData.class, "type", readable)
                        .registerSubtype(MarkdownRenderData.class, "markdown"));
                typeAdapter.add(RuntimeTypeAdapterFactory.of(HighlightRenderData.class, "type", readable)
                        .registerSubtype(HighlightRenderData.class, "code"));
                typeAdapter.add(RuntimeTypeAdapterFactory.of(MarkdownRenderData.class, "type", readable)
                        .registerSubtype(MarkdownRenderData.class, "markdown")
                        .registerSubtype(FileMarkdownRenderData.class, "markdown-file"));
                return typeAdapter;
            }
        };
        GsonPreRenderDataCastor gsonPreRenderDataCastor = new GsonPreRenderDataCastor();
        gsonPreRenderDataCastor.setGsonHandler(this.gsonHandler);
        builder.addPreRenderDataCastor(gsonPreRenderDataCastor);
        builder.addPlugin(':', new CommentRenderPolicy())
                .addPlugin(';', new AttachmentRenderPolicy())
                .addPlugin('~', new HighlightRenderPolicy())
                .addPlugin('-', new MarkdownRenderPolicy());
        builder.bind("toc", new TOCRenderPolicy());

        this.configure = builder.build();
    }

    public void run(String template, String output, String jsonStr) {
        XWPFTemplate.compile(template,
                this.configure)
                .render(this.gsonHandler.castJsonToType(jsonStr, TYPE))
                .writeToFile(output);
    }
}