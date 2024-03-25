package com.democrata.tecnica.controller;

import com.democrata.tecnica.service.IAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerguntaController {
    @Autowired
    private IAService iaService;

    @PostMapping("/pergunta")
    public String processarPergunta(@RequestBody String pergunta) {
        return iaService.processarPergunta(pergunta);
    }
}
