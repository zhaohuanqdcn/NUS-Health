package com.orbidroid.orbidroid_backend.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

        // General welcome page
        @GetMapping("/")
        public String index(){
            return "backend-index";
        }

        // Backend index page
        @GetMapping("/backend")
        public String backendIndex() {
            return "backend-index";
        }
}
