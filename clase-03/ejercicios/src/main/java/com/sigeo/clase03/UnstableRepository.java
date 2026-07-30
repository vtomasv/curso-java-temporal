package com.sigeo.clase03;

public class UnstableRepository {
    private int callCount = 0;

    public String fetchData() {
        callCount++;
        if (callCount % 3 == 0) {
            throw new RuntimeException("Fallo de red simulado");
        }
        return "Data OK";
    }
}
