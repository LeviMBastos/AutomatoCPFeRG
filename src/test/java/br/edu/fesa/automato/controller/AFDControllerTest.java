package br.edu.fesa.automato.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AFDControllerTest {

    private final AFDController afdController = new AFDController();

    @Test
    void testCPFValidoSemSeparadores() {
        assertTrue(afdController.validarDocumento("12345678901"), "CPF válido sem separadores falhou.");
    }

    @Test
    void testCPFValidoComPontosETraço() {
        assertTrue(afdController.validarDocumento("123.456.789-01"), "CPF válido com pontos e traço falhou.");
    }

    @Test
    void testCPFValidoComTraçoSemPontos() {
        assertTrue(afdController.validarDocumento("123456789-01"), "CPF válido com traço mas sem pontos falhou.");
    }

    @Test
    void testCPFInvalidoTamanhoErrado() {
        assertFalse(afdController.validarDocumento("12345678"), "CPF inválido (tamanho incorreto) foi aceito.");
    }

    @Test
    void testCPFInvalidoCaracteresInvalidos() {
        assertFalse(afdController.validarDocumento("123a5678b901"), "CPF inválido com caracteres não numéricos foi aceito.");
    }

    @Test
    void testRGValidoSemSeparadores() {
        assertTrue(afdController.validarDocumento("387123119"), "RG válido sem separadores falhou.");
    }

    @Test
    void testRGValidoComPontosETraço() {
        assertTrue(afdController.validarDocumento("38.712.311-9"), "RG válido com pontos e traço falhou.");
    }

    @Test
    void testRGValidoComTraçoSemPontos() {
        assertTrue(afdController.validarDocumento("38712311-9"), "RG válido com traço mas sem pontos falhou.");
    }

    @Test
    void testRGInvalidoTamanhoErrado() {
        assertFalse(afdController.validarDocumento("1234"), "RG inválido (tamanho incorreto) foi aceito.");
    }

    @Test
    void testRGInvalidoComLetrasInvalidas() {
        assertFalse(afdController.validarDocumento("38A12311B"), "RG inválido com caracteres não numéricos foi aceito.");
    }

    @Test
    void testRGValidoComXNoFinal() {
        assertTrue(afdController.validarDocumento("38712311X"), "RG válido com 'X' no final falhou.");
    }

    @Test
    void testDocumentoInvalidoIndefinido() {
        assertFalse(afdController.validarDocumento("ABCDEFG"), "Documento indefinido foi aceito erroneamente.");
    }
    
    @Test
    void testCPFInvalidoNumerosRepetidos() {
        assertFalse(afdController.validarDocumento("00000000000"), "Documento indefinido foi aceito erroneamente.");
    }
    
    @Test
    void testRGInvalidoNumerosRepetidos() {
        assertFalse(afdController.validarDocumento("111111111"), "Documento indefinido foi aceito erroneamente.");
    }
}
