package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.dto.SafraDto;
import br.com.gabriela.safra_control.service.SafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safra")
@RequiredArgsConstructor
public class SafraController {
    private final SafraService safraService;

    //Mostra a lista de safra cadastradas
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SafraEntity> findAll(){
        return safraService.findAll();
    }

    //Cria uma safra
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSafra (@Validated @RequestBody SafraDto safraDto){
        safraService.save(safraDto);
    }
}
