package org.huizisoft.frontend.controllers;

import org.huizisoft.frontend.utils.FileLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class Controller {
    @RequestMapping("/")
    public String index() {
        return loadStaticPage().toString();
    }

    private StringBuilder loadStaticPage() {
        FileLoader fileLoader = new FileLoader();
        InputStream input = fileLoader.getFileFromResourceAsStream("index.jsp");
        return fileLoader.printInputStream(input);
    }
}