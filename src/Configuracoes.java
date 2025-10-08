// Configuracoes.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Configuracoes {
    // Funções lidas do arquivo
    public String funcaoFxString;
    public String funcaoDerivadaString;
    public String funcaoPhiString;

    // Configurações globais
    public double precisao;
    public int maxIteracoes;
    public String metodoEscolhido;

    // Parâmetros para os métodos
    public double bisseccaoIntervaloInicio, bisseccaoIntervaloFim;
    public double iteracaoLinearX0;
    public double newtonX0;
    public double secanteX0, secanteX1;
    public double regulaFalsiIntervaloInicio, regulaFalsiIntervaloFim;

    public Configuracoes(String filename) throws IOException {
        configuraDados(filename);
    }

    // Função auxiliar para pegar uma propriedade obrigatória
    private String verificaDados(Map<String, String> lista, String key) throws IOException {
        String value = lista.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IOException("A propriedade obrigatória '" + key + "' não foi encontrada ou está vazia no arquivo input.txt.");
        }
        return value.trim();
    }

    private void configuraDados(String filename) throws IOException {
        Map<String, String> lista = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                String[] parte = linha.split("=", 2);
                if (parte.length == 2) {
                    lista.put(parte[0].trim(), parte[1].trim());
                }
            }
        }

        // --- 1. Carrega as propriedades que são SEMPRE obrigatórias ---
        funcaoFxString = verificaDados(lista, "funcao_fx");
        funcaoDerivadaString = verificaDados(lista, "funcao_derivada");
        funcaoPhiString = verificaDados(lista, "funcao_phi");
        precisao = Double.parseDouble(verificaDados(lista, "precisao"));
        maxIteracoes = Integer.parseInt(verificaDados(lista, "max_iteracoes"));
        metodoEscolhido = verificaDados(lista, "metodo_escolhido").toLowerCase();

        // --- 2. Carrega os parâmetros obrigatórios APENAS para o método escolhido ---
        switch (metodoEscolhido) {
            case "bisseccao":
                String intervaloBisseccao = verificaDados(lista, "intervalo_bisseccao");
                parseIntervalo(intervaloBisseccao, "bisseccao");
                break;
            case "iteracao_linear":
                iteracaoLinearX0 = Double.parseDouble(verificaDados(lista, "x0_iteracao_linear"));
                break;
            case "newton":
                newtonX0 = Double.parseDouble(verificaDados(lista, "x0_newton"));
                break;
            case "secante":
                secanteX0 = Double.parseDouble(verificaDados(lista, "x0_secante"));
                secanteX1 = Double.parseDouble(verificaDados(lista, "x1_secante"));
                break;
            case "regula_falsi":
                String intervaloRegulaFalsi = verificaDados(lista, "intervalo_regula_falsi");
                parseIntervalo(intervaloRegulaFalsi, "regula_falsi");
                break;
            default:
                // O Main.java já trata o caso de um método inválido, então não precisamos fazer nada aqui.
                break;
        }
    }

    // Pequena função auxiliar para não repetir o código de parse do intervalo
    private void parseIntervalo(String intervalo, String metodo) throws IOException {
        String[] parte = intervalo.replace("[", "").replace("]", "").split(",");
        if (parte.length == 2) {
            try {
                double inicio = Double.parseDouble(parte[0].trim());
                double fim = Double.parseDouble(parte[1].trim());
                if (metodo.equals("bisseccao")) {
                    bisseccaoIntervaloInicio = inicio;
                    bisseccaoIntervaloFim = fim;
                } else if (metodo.equals("regula_falsi")) {
                    regulaFalsiIntervaloInicio = inicio;
                    regulaFalsiIntervaloFim = fim;
                }
            } catch (NumberFormatException e) {
                throw new IOException("Formato de intervalo inválido para o método " + metodo + ". Use o formato [numero, numero].");
            }
        } else {
            throw new IOException("Formato de intervalo inválido para o método " + metodo + ". Use o formato [numero, numero].");
        }
    }
}