package com.democrata.tecnica.service;

import com.democrata.tecnica.domain.model.AssociacaoPalavras;
import com.democrata.tecnica.domain.model.Rotina;
import com.democrata.tecnica.domain.repository.AssociacaoPalavrasRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.democrata.tecnica.domain.model.Resposta;
import com.democrata.tecnica.domain.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class IAService {
    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private AssociacaoPalavrasRepository associacaoPalavrasRepository; // Injete o repositório de associações de palavras

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
            List<String> respostasEncontradas = buscarRespostasComPalavraChave(respostas, pergunta);

            if (!respostasEncontradas.isEmpty()) {
                // Montar a resposta para o usuário
                StringBuilder respostaParaUsuario = new StringBuilder();
                respostaParaUsuario.append("Achei algumas Respostas:\n");
                for (String resposta : respostasEncontradas) {
                    respostaParaUsuario.append("- ").append(resposta).append("\n");
                }
                return respostaParaUsuario.toString();
            } else {
                // Se não houver respostas semelhantes, fazer uma busca no Google
                List<String> resultadosGoogle = buscarNoGoogle(pergunta);
                if (!resultadosGoogle.isEmpty()) {
                    // Salvar os resultados do Google na tabela de respostas
                    for (String resultado : resultadosGoogle) {
                        Resposta novaResposta = new Resposta();
                        novaResposta.setDescricao(resultado);
                        respostaRepository.save(novaResposta);
                    }

                    // Verificar se há palavras associadas
                    List<String> palavrasAssociadas = buscarAssociacoesPalavras(pergunta);
                    if (!palavrasAssociadas.isEmpty()) {
                        // Buscar respostas que contenham palavras associadas e ação definida
                        List<String> respostasComAcao = buscarRespostasComPalavraChaveRefinada(respostas, palavrasAssociadas);
                        if (!respostasComAcao.isEmpty()) {
                            // Montar a resposta com ação definida
                            StringBuilder respostaParaUsuario = new StringBuilder();
                            respostaParaUsuario.append("Encontrei as seguintes respostas com ação:\n");
                            for (String resposta : respostasComAcao) {
                                respostaParaUsuario.append("- ").append(resposta).append("\n");
                            }
                            return respostaParaUsuario.toString();
                        }
                    }

                    // Montar a resposta com os resultados do Google
                    StringBuilder respostaParaUsuario = new StringBuilder();
                    respostaParaUsuario.append("Vou tentar achar algo no Google para te auxiliar:\n");
                    for (String resultado : resultadosGoogle) {
                        respostaParaUsuario.append("- ").append(resultado).append("\n");
                    }
                    return respostaParaUsuario.toString();
                } else {
                    // Se não houver resultados no Google, criar uma nova resposta e salvar no banco de dados
                    Resposta novaResposta = new Resposta();
                    novaResposta.setDescricao(pergunta);
                    respostaRepository.save(novaResposta);

                    return "Não houve nenhuma resposta em pesquisa externa";
                }
            }
        } catch (IllegalArgumentException e) {
            return "Erro ao processar a pergunta: " + e.getMessage();
        } catch (JsonProcessingException e) {
            return "Erro ao processar o JSON de pergunta.";
        } catch (IOException e) {
            return "Erro ao processar a pergunta.";
        }
    }

    // Função para buscar associações de palavras na tabela tb_associacao
    private List<String> buscarAssociacoesPalavras(String pergunta) {
        List<String> palavrasAssociadas = new ArrayList<>();
        List<AssociacaoPalavras> associacoes = associacaoPalavrasRepository.findAll();
        for (AssociacaoPalavras associacao : associacoes) {
            if (pergunta.contains(associacao.getPalavra())) {
                palavrasAssociadas.add(associacao.getAssociada());
            }
        }
        return palavrasAssociadas;
    }

    private List<String> buscarRespostasComPalavraChaveRefinada(List<Resposta> respostas, List<String> palavrasChave) {
        List<String> respostasEncontradas = new ArrayList<>();
        for (Resposta resposta : respostas) {
            String descricao = resposta.getDescricao().toLowerCase();
            // Verificar se a descrição contém pelo menos uma das palavras-chave ou associadas
            for (String palavra : palavrasChave) {
                if (descricao.contains(palavra)) {
                    respostasEncontradas.add(resposta.getDescricao());
                    break; // Se uma palavra-chave ou associada for encontrada, passe para a próxima resposta
                }
            }
        }
        return respostasEncontradas;
    }

    private List<String> buscarRespostasComPalavraChave(List<Resposta> respostas, String pergunta) {
        List<String> respostasEncontradas = new ArrayList<>();

        // Dividir a pergunta em palavras individuais
        String[] palavrasChave = pergunta.toLowerCase().split("\\s+");

        // Verificar cada resposta para ver se contém todas as palavras-chave
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
            // Se a resposta contém todas as palavras-chave, adicionar a resposta, a ação e as rotinas à lista de respostas encontradas
            if (contemTodasPalavrasChave) {
                String acao = "Sem ação associada! Não sei o que fazer!";
                StringBuilder respostaComAcao = new StringBuilder();
                respostaComAcao.append(resposta.getDescricao()).append(": ");
                if (resposta.getAcao() != null) {
                    acao = resposta.getAcao().getExecutar();
                    respostaComAcao.append(acao).append("\n");
                }
                // Adicionar as rotinas associadas à ação (se houver)
                if (resposta.getAcao() != null && !resposta.getAcao().getRotinas().isEmpty()) {
                    respostaComAcao.append("Rotinas associadas:\n");
                    for (Rotina rotina : resposta.getAcao().getRotinas()) {
                        if ("A".equals(rotina.getStatus())) {
                            respostaComAcao.append("- ").append(rotina.getExecutarRotina()).append("\n");
                        }
                    }
                }
                respostasEncontradas.add(respostaComAcao.toString());
            }
        }

        return respostasEncontradas;
    }


    private List<String> buscarNoGoogle(String pergunta) {
        List<String> resultadosGoogle = new ArrayList<>();

        try {
            // Formatar a pergunta para uma URL segura
            String perguntaFormatada = URLEncoder.encode(pergunta, "UTF-8");

            // Fazer uma solicitação HTTP ao Google e obter a página de resultados
            Document doc = Jsoup.connect("https://www.google.com/search?q=" + perguntaFormatada).get();

            // Extrair os resultados da pesquisa (títulos e links)
            Elements resultados = doc.select("div.g");

            for (Element resultado : resultados) {
                // Extrair o título e o link de cada resultado
                Element tituloElemento = resultado.selectFirst("h3");
                Element linkElemento = resultado.selectFirst("a[href]");

                if (tituloElemento != null && linkElemento != null) {
                    String titulo = tituloElemento.text();
                    String link = linkElemento.attr("href");

                    // Adicionar o título e o link à lista de resultados do Google
                    resultadosGoogle.add(titulo + " - " + link);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Lida com erros de IO, como conexões HTTP falhadas
        }

        return resultadosGoogle;
    }

}
