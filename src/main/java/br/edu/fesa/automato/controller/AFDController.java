package br.edu.fesa.automato.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class AFDController {

    enum TipoDocumento {
        CPF, RG, INDEFINIDO
    }   
    
    enum Estado { 
        Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, FINAL, ERRO 
    }

    private static final Map<Estado, Map<Character, Estado>> transicoes = new HashMap<>();

    static {
        for (Estado estado : Estado.values()) {
            transicoes.put(estado, new HashMap<>());
        }

        // CPF sem separadores (XXXXXXXXXXX)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q2).put(c, Estado.Q3); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q3).put(c, Estado.Q4); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q6).put(c, Estado.Q7); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q7).put(c, Estado.Q8); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q8).put(c, Estado.Q9); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q9).put(c, Estado.Q10); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q10).put(c, Estado.FINAL); }

        // CPF com pontos e traço (XXX.XXX.XXX-XX)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q2).put(c, Estado.Q3); }
        transicoes.get(Estado.Q3).put('.', Estado.Q4); // Primeiro ponto encontrado

        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q6).put(c, Estado.Q7); }
        transicoes.get(Estado.Q7).put('.', Estado.Q8); // Segundo ponto obrigatório se o primeiro foi usado

        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q8).put(c, Estado.Q9); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q9).put(c, Estado.Q10); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q10).put(c, Estado.Q11); }
        transicoes.get(Estado.Q11).put('-', Estado.Q12);
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q12).put(c, Estado.Q13); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q13).put(c, Estado.FINAL); }

        // CPF com traço mas sem pontos (XXXXXXXXX-XX)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q2).put(c, Estado.Q3); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q3).put(c, Estado.Q4); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q6).put(c, Estado.Q7); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q7).put(c, Estado.Q8); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q8).put(c, Estado.Q9); }
        transicoes.get(Estado.Q9).put('-', Estado.Q10); // Traço pode aparecer sem pontos
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q10).put(c, Estado.Q11); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q11).put(c, Estado.FINAL); }

        // RG sem separadores (XXXXXXXXX)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q2).put(c, Estado.Q3); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q3).put(c, Estado.Q4); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q6).put(c, Estado.Q7); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q7).put(c, Estado.Q8); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q8).put(c, Estado.FINAL); }
        transicoes.get(Estado.Q8).put('X', Estado.FINAL); // Último pode ser X

        // RG com pontos e traço (XX.XXX.XXX-X)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        transicoes.get(Estado.Q2).put('.', Estado.Q3); // Primeiro ponto obrigatório

        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q3).put(c, Estado.Q4); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        transicoes.get(Estado.Q6).put('.', Estado.Q7); // Segundo ponto obrigatório se o primeiro foi usado

        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q7).put(c, Estado.Q8); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q8).put(c, Estado.Q9); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q9).put(c, Estado.Q10); }
        transicoes.get(Estado.Q10).put('-', Estado.Q11); // Traço antes do último dígito
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q11).put(c, Estado.FINAL); }
        transicoes.get(Estado.Q11).put('X', Estado.FINAL);

        // RG sem pontos, mas com traço (XXXXXXXX-X)
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q0).put(c, Estado.Q1); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q1).put(c, Estado.Q2); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q2).put(c, Estado.Q3); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q3).put(c, Estado.Q4); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q4).put(c, Estado.Q5); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q5).put(c, Estado.Q6); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q6).put(c, Estado.Q7); }
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q7).put(c, Estado.Q8); }
        transicoes.get(Estado.Q8).put('-', Estado.Q9); // Pode ter apenas traço antes do último dígito
        for (char c = '0'; c <= '9'; c++) { transicoes.get(Estado.Q9).put(c, Estado.FINAL); }
        transicoes.get(Estado.Q9).put('X', Estado.FINAL);

    }

    public boolean validarDocumento(String documento) {
        Estado estadoAtual = Estado.Q0;
        TipoDocumento tipoDocumento = identificarTipoDocumento(documento);
        System.out.println("Documento recebido: " + documento);

        boolean pontoEmQ3 = false;
        
        boolean pontoEmQ2 = false;
        
        Set<Character> digitosUnicos = new HashSet<>();
        
        for (char c : documento.toCharArray()) {
            System.out.println("Caractere: " + c + " | Estado atual: " + estadoAtual);

            if (estadoAtual == Estado.FINAL) {
                break;
            }
            
            if (Character.isDigit(c) || c == 'X') {
                digitosUnicos.add(c);
            }
            
            // Validação específica de CPF
            if (tipoDocumento == TipoDocumento.CPF) {
                if (estadoAtual == Estado.Q3 && c == '.') {
                    pontoEmQ3 = true;
                }
                
                if (pontoEmQ3 && estadoAtual == Estado.Q7 && c != '.') {
                    return false;
                }
                
                if (pontoEmQ3 && estadoAtual == Estado.Q11 && c != '-') {
                    return false;
                }
                
                if (!pontoEmQ3 && estadoAtual == Estado.Q6 && c == '.'){
                    return false;
                }
            }
            
            if(tipoDocumento == TipoDocumento.RG) {
                if (estadoAtual == Estado.Q2 && c == '.') {
                    pontoEmQ2 = true;
                }
                
                if(pontoEmQ2) {
                    
                    if (estadoAtual == Estado.Q6 && c != '.') {
                        return false;
                    }

                    if (estadoAtual == Estado.Q10 && c != '-') {
                        return false;
                    }
                }
                else {
                    if (estadoAtual == Estado.Q5 && c == '.'){
                        return false;
                    }
                    
                    if (estadoAtual == Estado.Q8)
                    {
                        if (digitosUnicos.size() == 1)
                            return false;
                                    
                        return true;
                    }
                }
            }

            estadoAtual = transicoes.getOrDefault(estadoAtual, new HashMap<>()).getOrDefault(c, Estado.ERRO);

            System.out.println("Novo estado: " + estadoAtual);

            if (estadoAtual == Estado.ERRO) {
                return false;
            }
        }
        
        if (digitosUnicos.size() == 1) {
            System.out.println("Documento inválido: todos os números são idênticos.");
            return false;
        }

        System.out.println("Estado final: " + estadoAtual);
        return estadoAtual == Estado.FINAL;
    }

    private TipoDocumento identificarTipoDocumento(String documento) {
        String docNumerico = documento.replaceAll("[^0-9X]", ""); 

        if (docNumerico.replaceAll("X", "").length() == 11) {
            return TipoDocumento.CPF;
        } else if (docNumerico.length() >= 7 && docNumerico.length() <= 9) {
            return TipoDocumento.RG;
        }
        return TipoDocumento.INDEFINIDO;
    }

    @GetMapping("/")
    public String formulario(Model model) {
        model.addAttribute("mensagem", "");
        return "index";
    }
    
    @PostMapping("/validar")
    public String validar(@RequestParam("documento") String documento, Model model) {
        if (documento == null || documento.trim().isEmpty()) {
            model.addAttribute("mensagem", "Documento não pode estar vazio.");
            return "index";
        }
        
        TipoDocumento tipo = identificarTipoDocumento(documento);
        boolean valido = tipo != TipoDocumento.INDEFINIDO ? validarDocumento(documento) : false;

        model.addAttribute("mensagem", valido ? "O " + tipo + " é válido." : "Documento inválido.");
        
        return "index";
    }
}
