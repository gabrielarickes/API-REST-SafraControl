package br.com.gabriela.safra_control.controller;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.dto.SafraDto;
import br.com.gabriela.safra_control.handler.ErroResposta;
import br.com.gabriela.safra_control.service.SafraService;
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

@Tag(name = "Safras", description = "Cadastro, consulta, atualização e exclusão de safras")
@ApiResponse(responseCode = "500", description = "Erro interno inesperado no servidor",
                content = @Content(schema = @Schema(implementation = ErroResposta.class)))
@RestController
@RequestMapping("/safra")
@RequiredArgsConstructor
public class SafraController {
    private final SafraService safraService;

    //GET mostra a lista de safra cadastradas
    @Operation(summary = "Lista as safras",
            description = "Retorna todas as safras cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de safras (vazia se não houver nenhuma)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SafraEntity> findAll(){
        return safraService.findAll();
    }

    //GET mostra apenas uma safra
    @Operation(summary = "Busca uma safra pelo id")
    @ApiResponse(responseCode = "200", description = "Safra encontrada")
    @ApiResponse(responseCode = "400", description = "Id com valor não numérico",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity findBySafraId (
            @PathVariable("safraId") Integer safraId){
        return safraService.findBySafra(safraId);
    }

    //POST cria uma safra
    @Operation(summary = "Cadastra uma safra",
            description = "Não é permitido cadastrar duas safras da mesma cultura no mesmo ano.")
    @ApiResponse(responseCode = "201", description = "Safra cadastrada (resposta sem corpo)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (detalhes em campos) ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Já existe uma safra dessa cultura nesse ano",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSafra (@Validated @RequestBody SafraDto safraDto){
        safraService.save(safraDto);
    }

    //PUT atualiza a safra inteira
    @Operation(summary = "Atualiza todos os campos de uma safra",
            description = "Todos os campos são obrigatórios.")
    @ApiResponse(responseCode = "200", description = "Safra atualizada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos (detalhes em campos), id não numérico ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Já existe outra safra dessa cultura nesse ano",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PutMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity update (@PathVariable ("safraId") Integer safraId, @Valid @RequestBody SafraDto safraDto){
        return safraService.update(safraId, safraDto);
    }

    //PATCH atualiza apenas alguns campos da safra
    @Operation(summary = "Atualiza campos de uma safra",
            description = "Altera somente os campos enviados no corpo da requisição.")
    @ApiResponse(responseCode = "200", description = "Safra atualizada")
    @ApiResponse(responseCode = "400", description = "Valor inválido em algum campo enviado, id não numérico ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Já existe outra safra dessa cultura nesse ano",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @PatchMapping("/{safraId}")
    @ResponseStatus(HttpStatus.OK)
    public SafraEntity updateParcial(@PathVariable ("safraId") Integer safraId, @RequestBody SafraDto safraDto) {
        return safraService.updateParcial(safraId, safraDto);
    }

    //DELETE deleta uma safra
    @Operation(summary = "Exclui uma safra",
            description = "Os gastos vinculados à safra também são excluídos.")
    @ApiResponse(responseCode = "204", description = "Safra excluída (resposta sem corpo)")
    @ApiResponse(responseCode = "400", description = "Id com valor não numérico",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Safra não encontrada",
            content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    @DeleteMapping("/{safraId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ("safraId") Integer safraId) {
        safraService.delete(safraId);
    }



}
