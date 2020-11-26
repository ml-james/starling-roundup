package com.starling.roundupservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

@AutoConfigureJson
@ExtendWith(SpringExtension.class)
public class BaseTest {

    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired
    protected ObjectMapper jsonMapper;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    protected Resource loadResource(String path) {
        return resourceLoader.getResource(CLASSPATH_URL_PREFIX + path);
    }

    @SneakyThrows
    protected File loadResourceAsFile(String path) {
        return loadResource(path).getFile();
    }

    @SneakyThrows
    protected Path loadResourceAsPath(String path) {
        return loadResource(path).getFile().toPath();
    }

    @SneakyThrows
    protected String loadResourceAsString(String path, Charset charset) {
        return Files.readString(ResourceUtils.getFile(CLASSPATH_URL_PREFIX + path).toPath(), charset);
    }

    protected String loadResourceAsString(String path) {
        return loadResourceAsString(path, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    protected <T> T loadResourceAsObject(String path, Class<T> clazz) {
        return jsonMapper.readValue(loadResource(path).getInputStream(), clazz);
    }

    @SneakyThrows
    protected <T> List<T> loadResourceAsList(String path, Class<T> clazz) {
        return getAsList(loadResourceAsString(path), clazz);
    }

    @SneakyThrows
    protected <T> List<T> getAsList(String json, Class<T> clazz) {
        return jsonMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
    }
}
