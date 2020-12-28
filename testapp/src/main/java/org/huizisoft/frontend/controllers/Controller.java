package org.huizisoft.frontend.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.huizisoft.frontend.utils.FileLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class Controller {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

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