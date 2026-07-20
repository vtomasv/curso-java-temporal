package com.curso.solucion07;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    @Transactional
    public void transferir() {}
}
