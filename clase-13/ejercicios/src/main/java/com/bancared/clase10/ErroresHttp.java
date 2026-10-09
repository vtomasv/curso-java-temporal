package com.bancared.clase10;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ErroresHttp {
    @ExceptionHandler(ErrorNegocio.class)
    public ResponseEntity<Map<String,String>> negocio(ErrorNegocio e){
        HttpStatus estado=switch(e.tipo()){
            case "BANCO_NO_DISPONIBLE"->HttpStatus.SERVICE_UNAVAILABLE;
            case "CUENTA_INEXISTENTE","TRANSFERENCIA_INEXISTENTE"->HttpStatus.NOT_FOUND;
            case "COMANDO_DUPLICADO","CLAVE_REUTILIZADA","EFECTO_DUPLICADO","REVERSA_NO_DISPONIBLE","OPERACION_NO_ELEGIBLE"->HttpStatus.CONFLICT;
            case "NO_AUTORIZADO"->HttpStatus.FORBIDDEN;
            default->HttpStatus.UNPROCESSABLE_ENTITY;
        };
        return ResponseEntity.status(estado).body(Map.of("tipo",e.tipo(),"mensaje",e.getMessage()));
    }
}
