package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.dto.SafraDto;
import br.com.gabriela.safra_control.service.SafraService;
import jakarta.validation.Valid;
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

    //GET mostra a lista de safra cadastradas
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SafraEntity> findAll(){
        return safraService.findAll();
    }

    //GET mostra apenas uma safra
    @GetMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity findBySafraId (
            @PathVariable("safraId") Integer safraId){
        return safraService.findBySafra(safraId);
    }

    //POST cria uma safra
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSafra (@Validated @RequestBody SafraDto safraDto){
        safraService.save(safraDto);
    }

    //PUT atualiza a safra inteira
    @PutMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity update (@PathVariable ("safraId") Integer safraId, @Valid @RequestBody SafraDto safraDto){
        return safraService.update(safraId, safraDto);
    }

    //PATCH atualiza apenas alguns campos da safra
    @PatchMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity updateParcial(@PathVariable ("safraId") Integer safraId, @RequestBody SafraDto safraDto) {
        return safraService.updateParcial(safraId, safraDto);
    }

    //DELETE deleta uma safra
    @DeleteMapping("/{safraId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ("safraId") Integer safraId) {
        safraService.delete(safraId);
    }



}
