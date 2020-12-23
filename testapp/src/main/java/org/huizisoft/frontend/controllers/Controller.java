package org.huizisoft.frontend.controllers;

import org.huizisoft.frontend.utils.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @RequestMapping("/")
    public String index() {
        return loadStaticPage().toString();
    }

    private StringBuilder loadStaticPage() {
        FileLoader fileLoader = new FileLoader();
        InputStream input = fileLoader.getFileFromResourceAsStream("index.jsp");
        LOGGER.info("File input stream is returned");
        return fileLoader.printInputStream(input);
    }
}