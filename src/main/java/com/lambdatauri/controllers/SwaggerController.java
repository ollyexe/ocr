package com.lambdatauri.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SwaggerController {




    @Controller
    public class WebController {

        @RequestMapping(value = "/apidoc", method = RequestMethod.GET)
        public String index(HttpServletRequest request) {
            return "redirect:/swagger-ui.html";
        }

    }
}
