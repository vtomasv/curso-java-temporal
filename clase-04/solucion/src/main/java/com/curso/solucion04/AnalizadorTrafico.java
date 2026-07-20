package com.curso.solucion04;

import java.util.List;
import java.util.stream.Collectors;

public class AnalizadorTrafico {
    public List<String> filtrar(List<String> eventos) {
        return eventos.stream().filter(e -> e.contains("ERROR")).collect(Collectors.toList());
    }
}
