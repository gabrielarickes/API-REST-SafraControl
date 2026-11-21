package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.dto.GastosDto;
import br.com.gabriela.safra_control.dto.GastosResponseDto;
import br.com.gabriela.safra_control.handler.ErroResposta;
import br.com.gabriela.safra_control.service.GastosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Gastos", description = "Cadastro, consulta, atualização e exclusão de gastos das safras")
@ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor",
        content = @Content(schema = @Schema(implementation = ErroResposta.class)))
@RestController
@RequiredArgsConstructor
public class GastosController {
    private final GastosService gastosService;

    //GET mostra gastos
    @Operation(summary = "Lista todos os gastos", description = "Retorna os gastos de todas as safras.")
    @ApiResponse(responseCode = "200", description = "Lista de gastos (vazia se não houver nenhum)")
    @GetMapping("/gastos")
    @ResponseStatus(HttpStatus.OK)
    public List<GastosResponseDto> findAll() {
        return gastosService.findAll();
    }

    // GET mostra os gastos de uma safra específica
    @Operation(summary = "Lista os gastos de uma safra")
    @ApiResponse(responseCode = "200", description = "Lista de gastos da safra (vazia se não houver nenhum)")
    @ApiResponse(responseCode = "400", description = "Id da safra com valor não numérico",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/safra/{safraId}/gastos")
    @ResponseStatus(HttpStatus.OK)
    public List<GastosResponseDto> findBySafra(
            @PathVariable ("safraId") Integer safraId) {

        return gastosService.findBySafra(safraId);
    }

    //POST cria um gasto
    @Operation(summary = "Cadastra um gasto em uma safra",
            description = "O valor deve ser maior que zero e a data deve estar no formato yyyy-MM-dd.")
    @ApiResponse(responseCode = "201", description = "Gasto cadastrado (resposta sem corpo)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (detalhes em campos), id não numérico ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PostMapping("/safra/{safraId}/gastos")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveGastos(@PathVariable("safraId") Integer safraId,
                           @Validated @RequestBody GastosDto gastosDto) {
        gastosService.save(safraId, gastosDto);
    }

    //PUT atualiza um gasto inteiro
    @Operation(summary = "Atualiza todos os campos de um gasto",
            description = "Todos os campos são obrigatórios.")
    @ApiResponse(responseCode = "200", description = "Gasto atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (detalhes em campos), id não numérico ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Gasto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PutMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.OK)
    public GastosResponseDto update(@PathVariable ("gastoId") Integer gastoId, @Valid @RequestBody GastosDto gastosDto){
        return gastosService.update(gastoId, gastosDto);
    }

    //PATCH atualiza apenas alguns campos do gasto
    @Operation(summary = "Atualiza campos de um gasto",
            description = "Altera somente os campos enviados no corpo da requisição.")
    @ApiResponse(responseCode = "200", description = "Gasto atualizado")
    @ApiResponse(responseCode = "400", description = "Valor inválido em algum campo enviado, id não numérico ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Gasto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PatchMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.OK)
    public GastosResponseDto updateParcial (@PathVariable ("gastoId") Integer gastoId, @RequestBody GastosDto gastosDto){
        return gastosService.updateParcial(gastoId, gastosDto);
    }

    //DELETE delata um gasto
    @Operation(summary = "Exclui um gasto")
    @ApiResponse(responseCode = "204", description = "Gasto excluído (resposta sem corpo)")
    @ApiResponse(responseCode = "400", description = "Id com valor não numérico",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Gasto não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @DeleteMapping("/gastos/{gastoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer gastoId){
        gastosService.delete(gastoId);
    }


}