// Main.java
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        Configuracoes config;
        try {
            config = new Configuracoes(INPUT_FILE);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de configuração: " + e.getMessage());
            return;
        }

        TraduzFuncao func;
        try {
            // Instancia a função dinâmica com as strings do arquivo de configuração
            func = new TraduzFuncao(
                    config.funcaoFxString,
                    config.funcaoDerivadaString,
                    config.funcaoPhiString
            );
        } catch (IllegalArgumentException e) {
            System.err.println("Erro na definição da função no arquivo input.txt: " + e.getMessage());
            return;
        }

        Metodos methods = new Metodos(func, config.precisao, config.maxIteracoes);

        Formatacao resultado = null;

        // ... (o switch para escolher o método continua exatamente o mesmo)
        switch (config.metodoEscolhido) {
            case "bisseccao":
                System.out.println("Executando Método da Bissecção...");
                resultado = methods.bisseccao(config.bisseccaoIntervaloInicio, config.bisseccaoIntervaloFim);
                break;
            case "iteracao_linear":
                System.out.println("Executando Método da Iteração Linear...");
                resultado = methods.iteracaoLinear(config.iteracaoLinearX0);
                break;
            case "newton":
                System.out.println("Executando Método de Newton...");
                resultado = methods.newton(config.newtonX0);
                break;
            case "secante":
                System.out.println("Executando Método da Secante...");
                resultado = methods.secante(config.secanteX0, config.secanteX1);
                break;
            case "regula_falsi":
                System.out.println("Executando Método da Regula Falsi...");
                resultado = methods.regulaFalsi(config.regulaFalsiIntervaloInicio, config.regulaFalsiIntervaloFim);
                break;
            default:
                System.err.println("Método '" + config.metodoEscolhido + "' não reconhecido. Verifique o arquivo input.txt.");
                return;
        }


        if (resultado != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
                writer.println("--- Resultados da Análise de Zeros de Funções Reais ---");
                writer.println();
                writer.println("Função f(x): " + config.funcaoFxString); // Imprime a função lida
                writer.println("Precisão Delta: " + config.precisao);
                writer.println("Máximo de Iterações: " + config.maxIteracoes);
                writer.println("-----------------------------------------------------");
                writer.println(resultado.stringFormatada());
                System.out.println("Resultados salvos em " + OUTPUT_FILE);
            } catch (IOException e) {
                System.err.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
            }
        }
    }
}