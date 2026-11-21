package br.com.gabriela.safra_control.service;

import br.com.gabriela.safra_control.database.model.GastosEntity;
import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.database.repository.IGastosRepository;
import br.com.gabriela.safra_control.database.repository.ISafraRepository;
import br.com.gabriela.safra_control.dto.SafraDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SafraService {
    private final ISafraRepository safraRepository;
    private final IGastosRepository gastosRepository;

    //Mostra a lista inteira de safras cadastradas
    public List<SafraEntity> findAll(){
        return safraRepository.findAll();
    }

    //Lista apenas uma safra
    public SafraEntity findBySafra(Integer safraId) {

        return safraRepository.findById(safraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Safra "+safraId+ " não encontrada"
                ));
    }

    private void verificarDuplicidade(
            Integer ano, String cultura, Integer idIgnorado) {

        boolean existe;

        if (idIgnorado == null) {
            existe = safraRepository
                    .existsByAnoAndTipoCulturaIgnoreCase(
                            ano, cultura);
        }
        else {
            existe = safraRepository
                    .existsByAnoAndTipoCulturaIgnoreCaseAndIdNot(
                            ano, cultura, idIgnorado);
        }
        if (existe) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma safra dessa cultura nesse ano");
        }
    }

    //Cria uma safra
    public void save(SafraDto safraDto){

        String cultura = safraDto.getTipoCultura().trim();

        verificarDuplicidade(safraDto.getAno(), cultura, null);

        safraRepository.save(SafraEntity.builder()
                        .ano(safraDto.getAno())
                        .tipoCultura(cultura)
                        .quantidade(safraDto.getQuantidade())
                        .unidadeMedida(safraDto.getUnidadeMedida().trim())
                        .valorVendaUnidade(safraDto.getValorVendaUnidade())
                .build());
    }

    //Atualiza a safra inteira
    public SafraEntity update (Integer safraId, SafraDto safraDto){
                SafraEntity safra = safraRepository.findById(safraId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Safra "+safraId+ " não encontrada"
                        ));

                String cultura = safraDto.getTipoCultura().trim();

                verificarDuplicidade(
                        safraDto.getAno(), cultura, safraId
                );

                safra.setAno(safraDto.getAno());
                safra.setTipoCultura(cultura);
                safra.setQuantidade(safraDto.getQuantidade());
                safra.setUnidadeMedida(safraDto.getUnidadeMedida().trim());
                safra.setValorVendaUnidade(safraDto.getValorVendaUnidade());

                return safraRepository.save(safra);
    }

    //Atualiza apenas alguns campos da safra
    public SafraEntity updateParcial(Integer safraId, SafraDto safraDto) {
        SafraEntity safra = safraRepository.findById(safraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Safra " + safraId + " não encontrada"));

        if (safraDto.getAno() != null) {
            if (safraDto.getAno() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ano inválido");
            }
            safra.setAno(safraDto.getAno());
        }
        if (safraDto.getTipoCultura() != null) {
            if (safraDto.getTipoCultura().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de cultura não pode ser vazio");
            }
            safra.setTipoCultura(safraDto.getTipoCultura().trim());
        }
        if (safraDto.getQuantidade() != null) {
            if (safraDto.getQuantidade() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser maior que zero");
            }
            safra.setQuantidade(safraDto.getQuantidade());
        }
        if (safraDto.getUnidadeMedida() != null) {
            if(safraDto.getUnidadeMedida().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A unidade de medida é obrigatória");
            }
            safra.setUnidadeMedida(safraDto.getUnidadeMedida().trim());
        }
        if (safraDto.getValorVendaUnidade() != null) {
            if (safraDto.getValorVendaUnidade().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de venda inválido");
            }
            safra.setValorVendaUnidade(safraDto.getValorVendaUnidade());
        }

        verificarDuplicidade(
                safra.getAno(),
                safra.getTipoCultura(),
                safraId
        );

        return safraRepository.save(safra);
    }

    //Deleta uma safra
    @Transactional
    public void delete(Integer safraId) {

        SafraEntity safra = safraRepository.findById(safraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Safra " + safraId + " não encontrada"));
        List<GastosEntity> gastos =
                gastosRepository.findBySafra_Id(safraId);

        gastosRepository.deleteAll(gastos);
        safraRepository.delete(safra);
    }




}
