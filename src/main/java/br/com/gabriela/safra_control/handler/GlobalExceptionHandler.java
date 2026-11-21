package br.com.gabriela.safra_control.handler;

import br.com.gabriela.safra_control.controller.GastosController;
import br.com.gabriela.safra_control.controller.RelatorioController;
import br.com.gabriela.safra_control.controller.SafraController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {
        SafraController.class,
        GastosController.class,
        RelatorioController.class
})
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400  falha no @Valid (campos inválidos)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ErroResposta(400, "Dados inválidos", campos));
    }

    // 404, 409 e 400 de negócio lançados nos services
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResposta> handleStatus(ResponseStatusException ex) {
        ErroResposta corpo = new ErroResposta(
                ex.getStatusCode().value(), ex.getReason(), null);
        return ResponseEntity.status(ex.getStatusCode()).body(corpo);
    }

    // 400  JSON malformado ou formato errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErroResposta(
                400, "Corpo da requisição inválido. Verifique o JSON e os formatos (datas: yyyy-MM-dd).", null));
    }

    // 400  parâmetro de URL com tipo errado
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> handleTipoParametro(MethodArgumentTypeMismatchException ex) {
        String mensagem = "Parâmetro '" + ex.getName() + "' com valor inválido";
        return ResponseEntity.badRequest().body(new ErroResposta(400, mensagem, null));
    }

    // 409  violação de constraint no banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> handleIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade no banco", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResposta(
                409, "Registro duplicado ou em conflito com dados existentes", null));
    }

    // Demais erros do Spring (405, 415, etc.) mantêm o status original
    // e qualquer erro inesperado vira 500 com mensagem genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGenerico(Exception ex) {
        if (ex instanceof ErrorResponse er) {
            int status = er.getStatusCode().value();
            return ResponseEntity.status(er.getStatusCode())
                    .body(new ErroResposta(status, er.getBody().getDetail(), null));
        }

        log.error("Erro inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResposta(
                500, "Erro interno no servidor", null));
    }
}