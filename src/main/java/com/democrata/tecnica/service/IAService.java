package com.democrata.tecnica.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.democrata.tecnica.domain.model.Resposta;
import com.democrata.tecnica.domain.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IAService {
    @Autowired
    private RespostaRepository respostaRepository;

    public String processarPergunta(String jsonPergunta) {
        try {
            if (jsonPergunta == null) {
                throw new IllegalArgumentException("O JSON de pergunta não pode ser nulo.");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonPergunta);

            String pergunta = rootNode.get("pergunta").asText();

            // Realizar a pesquisa por palavra-chave nas respostas
            List<Resposta> respostas = respostaRepository.findAll();

            // Buscar frases que contenham pelo menos uma palavra-chave
            List<Resposta> respostasEncontradas = buscarRespostasComPalavraChave(respostas, pergunta);

            if (!respostasEncontradas.isEmpty()) {
                // Montar a resposta para o usuário
                StringBuilder respostaParaUsuario = new StringBuilder();
                respostaParaUsuario.append("Respostas encontradas:\n");
                for (Resposta resposta : respostasEncontradas) {
                    respostaParaUsuario.append("- ").append(resposta.getDescricao()).append("\n");
                }
                return respostaParaUsuario.toString();
            } else {
                // Se não houver respostas semelhantes, criar uma nova e salvar no banco de dados
                Resposta novaResposta = new Resposta();
                novaResposta.setDescricao(pergunta);
                respostaRepository.save(novaResposta);

                return "A pergunta foi registrada. Aguarde uma resposta.";
            }
        } catch (IllegalArgumentException e) {
            return "Erro ao processar a pergunta: " + e.getMessage();
        } catch (JsonProcessingException e) {
            return "Erro ao processar o JSON de pergunta.";
        } catch (Exception e) {
            return "Erro ao processar a pergunta.";
        }
    }

    // Função para buscar frases que contenham pelo menos uma palavra-chave
    private List<Resposta> buscarRespostasComPalavraChaveOld(List<Resposta> respostas, String pergunta) {
        List<Resposta> respostasEncontradas = new ArrayList<>();

        // Dividir a pergunta em palavras individuais
        String[] palavras = pergunta.toLowerCase().split("\\s+");

        // Verificar cada frase para ver se contém pelo menos uma palavra-chave
        for (Resposta resposta : respostas) {
            String descricao = resposta.getDescricao().toLowerCase();
            // Verificar se a descrição contém pelo menos uma das palavras-chave
            boolean contemPalavraChave = false;
            for (String palavra : palavras) {
                if (descricao.contains(palavra)) {
                    contemPalavraChave = true;
                    break;
                }
            }
            // Se a frase contém pelo menos uma palavra-chave, adicione-a à lista de frases encontradas
            if (contemPalavraChave) {
                respostasEncontradas.add(resposta);
            }
        }

        return respostasEncontradas;
    }

    private List<Resposta> buscarRespostasComPalavraChave(List<Resposta> respostas, String pergunta) {
        List<Resposta> respostasEncontradas = new ArrayList<>();

        // Dividir a pergunta em palavras individuais
        String[] palavrasChave = pergunta.toLowerCase().split("\\s+");

        // Verificar cada frase para ver se contém todas as palavras-chave
        for (Resposta resposta : respostas) {
            String descricao = resposta.getDescricao().toLowerCase();
            // Verificar se a descrição contém todas as palavras-chave
            boolean contemTodasPalavrasChave = true;
            for (String palavraChave : palavrasChave) {
                if (!descricao.contains(palavraChave)) {
                    contemTodasPalavrasChave = false;
                    break;
                }
            }
            // Se a frase contém todas as palavras-chave, adicione-a à lista de frases encontradas
            if (contemTodasPalavrasChave) {
                respostasEncontradas.add(resposta);
            }
        }

        return respostasEncontradas;
    }


}