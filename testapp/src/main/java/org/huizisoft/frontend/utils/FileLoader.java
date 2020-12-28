package org.huizisoft.frontend.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoader.class);
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.

    public InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public StringBuilder printInputStream(InputStream is) {
        StringBuilder page = new StringBuilder();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                page.append(line);
            }
            LOGGER.info("Gathered info \n\r{}", page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }
}