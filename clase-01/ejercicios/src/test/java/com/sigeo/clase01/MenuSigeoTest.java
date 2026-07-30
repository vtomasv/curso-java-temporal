package com.sigeo.clase01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class MenuSigeoTest {

    private MenuSigeo menu;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        menu = new MenuSigeo();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void debeAgregarRegistroConOpcion1() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("Nuevo Registro\n".getBytes()));
        menu.procesarOpcion("1", scanner);
        
        assertThat(menu.getRegistros()).containsExactly("Nuevo Registro");
    }

    @Test
    void debeTerminarEjecucionConOpcion4() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("".getBytes()));
        menu.procesarOpcion("4", scanner);
        
        assertThat(menu.isEjecutando()).isFalse();
    }

    @Test
    void debeManejarOpcionInvalida() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("".getBytes()));
        menu.procesarOpcion("99", scanner);
        
        assertThat(outContent.toString()).contains("Opción inválida");
        assertThat(menu.isEjecutando()).isTrue();
    }
}
