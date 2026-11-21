package br.com.gabriela.safra_control.handler;

import java.util.Map;

public record ErroResposta (int status, String mensagem, Map<String, String> campos){

}
