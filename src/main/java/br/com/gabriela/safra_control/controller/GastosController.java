package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.GastosDto;
import br.com.gabriela.safra_control.dto.GastosResponseDto;
import br.com.gabriela.safra_control.service.GastosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GastosController {
    private final GastosService gastosService;

    //GET mostra gastos
    @GetMapping("/gastos")
    @ResponseStatus(HttpStatus.OK)
    public List<GastosResponseDto> findAll() {
        return gastosService.findAll();
    }

    // GET mostra os gastos de uma safra específica
    @GetMapping("/safra/{safraId}/gastos")
    @ResponseStatus(HttpStatus.OK)
    public List<GastosResponseDto> findBySafra(
            @PathVariable ("safraId") Integer safraId) {

        return gastosService.findBySafra(safraId);
    }

    //POST cria um gasto
    @PostMapping("/safra/{safraId}/gastos")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveGastos(@PathVariable("safraId") Integer safraId,
                           @Validated @RequestBody GastosDto gastosDto) {
        gastosService.save(safraId, gastosDto);
    }

    //PUT atualiza um gasto inteiro
    @PutMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.OK)
    public GastosResponseDto update(@PathVariable ("gastoId") Integer gastoId, @Valid @RequestBody GastosDto gastosDto){
        return gastosService.update(gastoId, gastosDto);
    }

    //PATCH atualiza apenas alguns campos do gasto
    @PatchMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.OK)
    public GastosResponseDto updateParcial (@PathVariable ("gastoId") Integer gastoId, @RequestBody GastosDto gastosDto){
        return gastosService.updateParcial(gastoId, gastosDto);
    }

    //DELETE delata um gasto
    @DeleteMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer gastoId){
        gastosService.delete(gastoId);
    }


}