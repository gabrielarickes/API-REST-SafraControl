
package br.com.gabriela.safra_control.service;

import br.com.gabriela.safra_control.database.model.GastosEntity;
import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.database.repository.IGastosRepository;
import br.com.gabriela.safra_control.database.repository.ISafraRepository;
import br.com.gabriela.safra_control.dto.GastosDto;
import br.com.gabriela.safra_control.dto.GastosResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastosService {

    private final IGastosRepository gastosRepository;
    private final ISafraRepository safraRepository;

    // Lista todos os gastos
    @Transactional(readOnly = true)
    public List<GastosResponseDto> findAll() {
        return gastosRepository.findAll().stream()
                .map(this::converter)
                .toList();
    }

    // Lista os gastos de uma safra
    @Transactional(readOnly = true)
    public List<GastosResponseDto> findBySafra(Integer safraId) {

        if (!safraRepository.existsById(safraId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Safra não encontrada"
            );
        }
        return gastosRepository.findBySafra_Id(safraId)
                .stream()
                .map(this::converter)
                .toList();
    }
    // Converte a Entity para DTO de resposta
    private GastosResponseDto converter(GastosEntity gasto) {

        return new GastosResponseDto(
                gasto.getId(),
                gasto.getTipoGasto(),
                gasto.getDescricao(),
                gasto.getValor(),
                gasto.getDataGasto(),
                gasto.getSafra().getId()
        );
    }
    // Cadastra um gasto
    @Transactional
    public void save(Integer safraId, GastosDto gastosDto) {

        SafraEntity safra = safraRepository.findById(safraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Safra " + safraId + " não encontrada"
                ));

        gastosRepository.save(GastosEntity.builder()
                .tipoGasto(gastosDto.getTipoGasto())
                .descricao(gastosDto.getDescricao())
                .valor(gastosDto.getValor())
                .dataGasto(gastosDto.getDataGasto())
                .safra(safra)
                .build());
    }
    //Atualiza um gasto inteiro
    @Transactional
    public GastosResponseDto update (Integer gastoId, GastosDto gastosDto){
        GastosEntity gastos = gastosRepository.findById(gastoId)
                .orElseThrow(() -> new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Gasto não encontrado"));

        gastos.setTipoGasto(gastosDto.getTipoGasto().trim());
        gastos.setDescricao(gastosDto.getDescricao().trim());
        gastos.setValor(gastosDto.getValor());
        gastos.setDataGasto(gastosDto.getDataGasto());

        return converter(gastosRepository.save(gastos));
    }

    //Atualiza apenas alguns campos do gasto
    @Transactional
    public GastosResponseDto updateParcial(Integer gastoId, GastosDto gastosDto){
        GastosEntity gastos = gastosRepository.findById(gastoId)
                .orElseThrow(() -> new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Gasto não encontrado"));

        if(gastosDto.getTipoGasto() != null){
            if(gastosDto.getTipoGasto().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo do gasto é obrigatório");
            }
            gastos.setTipoGasto(gastosDto.getTipoGasto());
        }
        if(gastosDto.getDescricao() != null){
            if(gastosDto.getDescricao().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A descrição é obrigatória");
            }
            gastos.setDescricao(gastosDto.getDescricao());
        }
        if(gastosDto.getValor() != null){
            if(gastosDto.getValor().compareTo(BigDecimal.ZERO) <= 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do gasto é obrigatório");
            }
            gastos.setValor(gastosDto.getValor());
        }
        if(gastosDto.getDataGasto() != null){
            gastos.setDataGasto(gastosDto.getDataGasto());
        }

        return converter(gastosRepository.save(gastos));
    }

    //Deleta um gasto
    @Transactional
    public void delete(Integer gastoId){
        GastosEntity gastos = gastosRepository.findById(gastoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Gasto não encontrado"));

        gastosRepository.delete(gastos);
    }

}
