package br.com.gabriela.safra_control.service;

import br.com.gabriela.safra_control.database.model.SafraEntity;
import br.com.gabriela.safra_control.database.repository.ISafraRepository;
import br.com.gabriela.safra_control.dto.SafraDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SafraService {
    private final ISafraRepository safraRepository;

    //Mostra a lista de safra cadastradas
    public List<SafraEntity> findAll(){
        return safraRepository.findAll();
    }

    //Cria uma safra
    public void save(SafraDto safraDto){
        safraRepository.save(SafraEntity.builder()
                        .ano(safraDto.getAno())
                        .tipoCultura(safraDto.getTipoCultura())
                        .quantidade(safraDto.getQuantidade())
                        .unidadeMedida(safraDto.getUnidadeMedida())
                        .valorVendaUnidade(safraDto.getValorVendaUnidade())
                .build());
    }
}
