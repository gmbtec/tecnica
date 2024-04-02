package com.democrata.tecnica.service;

import com.democrata.tecnica.domain.model.AssociacaoPalavras;
import com.democrata.tecnica.domain.model.Rotina;
import com.democrata.tecnica.domain.model.Resposta;
import com.democrata.tecnica.domain.repository.AssociacaoPalavrasRepository;
import com.democrata.tecnica.domain.repository.RespostaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IAService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private AssociacaoPalavrasRepository associacaoPalavrasRepository;

  /*
    public String processarPergunta(String jsonPergunta) {
        try {
            if (jsonPergunta == null) {
                throw new IllegalArgumentException("Ainda não houve Nenhuma solicitação!");
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> respostaJson = new HashMap<>();

            JsonNode rootNode = mapper.readTree(jsonPergunta);
            String pergunta = rootNode.get("pergunta").asText();
            String pesquisaGoogle = rootNode.has("pesquisaGoogle") ? rootNode.get("pesquisaGoogle").asText() : "N";

            List<Resposta> respostas = respostaRepository.findAll();

            List<String> respostasEncontradas = buscarRespostasComPalavraChave(respostas, pergunta);

            if (respostasEncontradas.isEmpty()) {
                List<String> palavrasChave = Arrays.asList(pergunta.toLowerCase().split("\\s+"));
                List<String> palavrasAssociadas = buscarAssociacoesPalavras(palavrasChave);
                respostasEncontradas = buscarRespostasComPalavraChaveRefinada(respostas, palavrasAssociadas);
            }

            if (!respostasEncontradas.isEmpty()) {
                respostaJson.put("status", "success");
                respostaJson.put("respostas", construirRespostasJson(respostasEncontradas, respostas));
            } else if (pesquisaGoogle.equalsIgnoreCase("S")) {
                List<String> resultadosGoogle = buscarNoGoogle(pergunta);
                if (!resultadosGoogle.isEmpty()) {
                    respostaJson.put("status", "success");
                    respostaJson.put("resultadosGoogle", resultadosGoogle);

                    for (String resultado : resultadosGoogle) {
                        Resposta novaResposta = new Resposta();
                        novaResposta.setDescricao(resultado);
                        Resposta respostaSalva = respostaRepository.save(novaResposta);
                        respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
                    }

                    List<String> palavrasAssociadas = buscarAssociacoesPalavras(pergunta);
                    if (!palavrasAssociadas.isEmpty()) {
                        List<String> respostasComAcao = buscarRespostasComPalavraChaveRefinada(respostas, palavrasAssociadas);
                        if (!respostasComAcao.isEmpty()) {
                            respostaJson.put("respostasComAcao", construirRespostasJson(respostasComAcao, respostas));
                        }
                    }
                } else {
                    Resposta novaResposta = new Resposta();
                    novaResposta.setDescricao(pergunta);
                    Resposta respostaSalva = respostaRepository.save(novaResposta);
                    respostaJson.put("status", "warning");
                    respostaJson.put("mensagem", "Não tenho conhecimento sobre o assunto, poderia me ensinar mais a respeito?");
                    respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
                }
            } else {
                Resposta novaResposta = new Resposta();
                novaResposta.setDescricao(pergunta);
                Resposta respostaSalva = respostaRepository.save(novaResposta);
                respostaJson.put("status", "warning");
                respostaJson.put("mensagem", "Não tenho conhecimento sobre o assunto, poderia me ensinar mais a respeito?");
                respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
            }

            return mapper.writeValueAsString(respostaJson);
        } catch (IllegalArgumentException e) {
            return formatarRespostaErro("Erro ao processar a pergunta: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return formatarRespostaErro("Erro ao processar o JSON de pergunta.");
        } catch (IOException e) {
            return formatarRespostaErro("Erro ao processar a pergunta.");
        }
    }
*/
  public String processarPergunta(String jsonPergunta) {
      try {
          if (jsonPergunta == null) {
              throw new IllegalArgumentException("Ainda não houve Nenhuma solicitação!");
          }

          ObjectMapper mapper = new ObjectMapper();
          Map<String, Object> respostaJson = new HashMap<>();

          JsonNode rootNode = mapper.readTree(jsonPergunta);
          String pergunta = rootNode.get("pergunta").asText();
          String pesquisaGoogle = rootNode.has("pesquisaGoogle") ? rootNode.get("pesquisaGoogle").asText() : "N";

          List<Resposta> respostas = respostaRepository.findAll();

          List<String> respostasEncontradas = buscarRespostasComPalavraChave(respostas, pergunta);

          if (respostasEncontradas.isEmpty()) {
              List<String> palavrasChave = Arrays.asList(pergunta.toLowerCase().split("\\s+"));
              List<String> palavrasAssociadas = buscarAssociacoesPalavras(palavrasChave);
              respostasEncontradas = buscarRespostasComPalavraChaveRefinada(respostas, palavrasAssociadas);
          }

          if (!respostasEncontradas.isEmpty()) {
              respostaJson.put("status", "success");
              respostaJson.put("respostas", construirRespostasJson(respostasEncontradas, respostas));
          } else if (pesquisaGoogle.equalsIgnoreCase("S")) {
              List<String> resultadosGoogle = buscarNoGoogle(pergunta);
              if (!resultadosGoogle.isEmpty()) {
                  respostaJson.put("status", "success");
                  respostaJson.put("resultadosGoogle", resultadosGoogle);

                  for (String resultado : resultadosGoogle) {
                      Resposta novaResposta = new Resposta();
                      novaResposta.setDescricao(resultado);
                      Resposta respostaSalva = respostaRepository.save(novaResposta);
                      respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
                  }

                  List<String> palavrasAssociadas = buscarAssociacoesPalavras(pergunta);
                  if (!palavrasAssociadas.isEmpty()) {
                      List<String> respostasComAcao = buscarRespostasComPalavraChaveRefinada(respostas, palavrasAssociadas);
                      if (!respostasComAcao.isEmpty()) {
                          respostaJson.put("respostasComAcao", construirRespostasJson(respostasComAcao, respostas));
                      }
                  }
              } else {
                  Resposta novaResposta = new Resposta();
                  novaResposta.setDescricao(pergunta);
                  Resposta respostaSalva = respostaRepository.save(novaResposta);
                  respostaJson.put("status", "warning");
                  respostaJson.put("mensagem", "Não tenho conhecimento sobre o assunto, poderia me ensinar mais a respeito?");
                  respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
              }
          } else {
              Resposta novaResposta = new Resposta();
              novaResposta.setDescricao(pergunta);
              Resposta respostaSalva = respostaRepository.save(novaResposta);
              respostaJson.put("status", "warning");
              respostaJson.put("mensagem", "Não tenho conhecimento sobre o assunto, poderia me ensinar mais a respeito?");
              respostaJson.put("id", respostaSalva.getId()); // Inserindo o ID do registro
          }

          return mapper.writeValueAsString(respostaJson);
      } catch (IllegalArgumentException e) {
          return formatarRespostaErro("Erro ao processar a pergunta: " + e.getMessage());
      } catch (JsonProcessingException e) {
          return formatarRespostaErro("Erro ao processar o JSON de pergunta.");
      } catch (IOException e) {
          return formatarRespostaErro("Erro ao processar a pergunta.");
      }
  }

    /*
    private List<Map<String, Object>> construirRespostasJson(List<String> respostasEncontradas, List<Resposta> respostas) {
        List<Map<String, Object>> respostasJson = new ArrayList<>();
        for (String respostaEncontrada : respostasEncontradas) {
            Map<String, Object> respostaMap = new HashMap<>();
            respostaMap.put("descricao", respostaEncontrada);

            // Procura a ação e as rotinas associadas
            for (Resposta resposta : respostas) {
                if (resposta.getDescricao().equalsIgnoreCase(respostaEncontrada)) {
                    if (resposta.getAcao() != null) {
                        respostaMap.put("acao", resposta.getAcao().getExecutar());

                        List<Map<String, String>> rotinasJson = new ArrayList<>();
                        for (Rotina rotina : resposta.getAcao().getRotinas()) {
                            if ("A".equals(rotina.getStatus())) {
                                Map<String, String> rotinaMap = new HashMap<>();
                                rotinaMap.put("executarRotina", rotina.getExecutarRotina());
                                rotinasJson.add(rotinaMap);
                            }
                        }
                        respostaMap.put("rotinas", rotinasJson);
                    }
                    respostaMap.put("id", resposta.getId());
                    break;
                }
            }
            respostasJson.add(respostaMap);
        }
        return respostasJson;
    }
*/

    private List<Map<String, Object>> construirRespostasJson(List<String> respostasEncontradas, List<Resposta> respostas) {
        List<Map<String, Object>> respostasJson = new ArrayList<>();
        for (String respostaEncontrada : respostasEncontradas) {
            Map<String, Object> respostaMap = new HashMap<>();
            respostaMap.put("descricao", respostaEncontrada);

            // Procura a ação e as rotinas associadas
            for (Resposta resposta : respostas) {
                if (resposta.getDescricao().equalsIgnoreCase(respostaEncontrada)) {
                    if (resposta.getAcao() != null) {
                        respostaMap.put("acao", resposta.getAcao().getExecutar());

                        // Verifica se o link não é nulo antes de adicionar ao mapa
                        if (resposta.getAcao().getLink() != null) {
                            respostaMap.put("link", resposta.getAcao().getLink());
                        }

                        List<Map<String, String>> rotinasJson = new ArrayList<>();
                        for (Rotina rotina : resposta.getAcao().getRotinas()) {
                            if ("A".equals(rotina.getStatus())) {
                                Map<String, String> rotinaMap = new HashMap<>();
                                rotinaMap.put("executarRotina", rotina.getExecutarRotina());

                                // Verifica se o link não é nulo antes de adicionar ao mapa
                                if (rotina.getLink() != null) {
                                    rotinaMap.put("link", rotina.getLink());
                                }

                                rotinasJson.add(rotinaMap);
                            }
                        }
                        respostaMap.put("rotinas", rotinasJson);
                    }
                    respostaMap.put("id", resposta.getId());
                    break;
                }
            }
            respostasJson.add(respostaMap);
        }
        return respostasJson;
    }

    private List<String> buscarAssociacoesPalavrasOld(String pergunta) {
        List<String> palavrasAssociadas = new ArrayList<>();
        List<AssociacaoPalavras> associacoes = associacaoPalavrasRepository.findAll();
        for (AssociacaoPalavras associacao : associacoes) {
            if (calculaDistanciaLevenshtein(pergunta, associacao.getPalavra()) <= 2) {
                palavrasAssociadas.add(associacao.getAssociada());
            }
        }
        return palavrasAssociadas;
    }


    private List<String> buscarAssociacoesPalavras(String pergunta) {
        List<String> palavrasAssociadas = new ArrayList<>();
        List<AssociacaoPalavras> associacoes = associacaoPalavrasRepository.findAll();

        boolean criterioEncontrado = false;

        for (AssociacaoPalavras associacao : associacoes) {
            if (pergunta.contains(associacao.getPalavra())) {
                palavrasAssociadas.add(associacao.getAssociada());
                criterioEncontrado = true;
                break;
            }
        }

        if (!criterioEncontrado) {
            for (AssociacaoPalavras associacao : associacoes) {
                if (pergunta.contains(associacao.getPalavra())) {
                    palavrasAssociadas.add(associacao.getAssociada());
                    criterioEncontrado = true;
                    break;
                }
            }
        }

        // Se ainda não encontrarmos, retornamos todas as associações
        if (!criterioEncontrado) {
            for (AssociacaoPalavras associacao : associacoes) {
                palavrasAssociadas.add(associacao.getAssociada());
            }
        }

        return palavrasAssociadas;
    }


    private List<String> buscarAssociacoesPalavras(List<String> palavrasChave) {
        List<String> palavrasAssociadas = new ArrayList<>();
        List<AssociacaoPalavras> associacoes = associacaoPalavrasRepository.findAll();

        for (String palavra : palavrasChave) {
            for (AssociacaoPalavras associacao : associacoes) {
                if (similaridadeJaccard(palavra, associacao.getPalavra()) >= 0.5) {
                    palavrasAssociadas.add(associacao.getAssociada());
                }
            }
        }

        return palavrasAssociadas.stream().distinct().collect(Collectors.toList());
    }

    private double similaridadeJaccard(String palavra1, String palavra2) {
        Set<Character> conjunto1 = new HashSet<>();
        Set<Character> conjunto2 = new HashSet<>();

        for (char c : palavra1.toCharArray()) {
            conjunto1.add(c);
        }

        for (char c : palavra2.toCharArray()) {
            conjunto2.add(c);
        }

        int intersecao = 0;
        for (char c : conjunto1) {
            if (conjunto2.contains(c)) {
                intersecao++;
            }
        }

        int uniao = conjunto1.size() + conjunto2.size() - intersecao;

        return (double) intersecao / uniao;
    }
    private int calculaDistanciaLevenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(s1.charAt(i - 1), s2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    private List<String> buscarRespostasComPalavraChaveRefinada(List<Resposta> respostas, List<String> palavrasChave) {
        List<String> respostasEncontradas = new ArrayList<>();
        for (Resposta resposta : respostas) {
            String descricao = resposta.getDescricao().toLowerCase();
            for (String palavra : palavrasChave) {
                if (descricao.contains(palavra)) {
                    respostasEncontradas.add(resposta.getDescricao());
                    break;
                }
            }
        }
        return respostasEncontradas;
    }

    private List<String> buscarRespostasComPalavraChave(List<Resposta> respostas, String pergunta) {
        List<String> respostasEncontradas = new ArrayList<>();
        String[] palavrasChave = pergunta.toLowerCase().split("\\s+");
        for (Resposta resposta : respostas) {
            String descricao = resposta.getDescricao().toLowerCase();
            boolean contemTodasPalavrasChave = true;
            for (String palavraChave : palavrasChave) {
                if (!descricao.contains(palavraChave)) {
                    contemTodasPalavrasChave = false;
                    break;
                }
            }
            if (contemTodasPalavrasChave) {
                respostasEncontradas.add(resposta.getDescricao());
            }
        }
        return respostasEncontradas;
    }

    private List<String> buscarNoGoogle(String pergunta) {
        List<String> resultadosGoogle = new ArrayList<>();
        try {
            String perguntaFormatada = URLEncoder.encode(pergunta, "UTF-8");
            Document doc = Jsoup.connect("https://www.google.com/search?q=" + perguntaFormatada).get();
            Elements resultados = doc.select("div.g");
            for (Element resultado : resultados) {
                Element tituloElemento = resultado.selectFirst("h3");
                Element linkElemento = resultado.selectFirst("a[href]");
                if (tituloElemento != null && linkElemento != null) {
                    String titulo = tituloElemento.text();
                    String link = linkElemento.attr("href");
                    resultadosGoogle.add(titulo + " - " + link);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultadosGoogle;
    }

    private String formatarRespostaErro(String mensagem) {
        Map<String, Object> respostaJson = new HashMap<>();
        respostaJson.put("status", "error");
        respostaJson.put("mensagem", mensagem);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(respostaJson);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "{\"status\":\"error\",\"mensagem\":\"Erro interno\"}";
        }
    }
}
