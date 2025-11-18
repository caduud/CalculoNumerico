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
        Formatacao resultado;

        System.out.println("Executando os Métodos...");

        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {

            writer.println("--- Resultados da Análise de Zeros de Funções Reais ---");
            writer.println();
            writer.println("Função f(x): " + config.funcaoFxString);
            writer.println("Função f'(x): " + config.funcaoDerivadaString);
            writer.println("Função phi(x): " + config.funcaoPhiString);
            writer.println("Precisão Delta: " + config.precisao);
            writer.println("Máximo de Iterações: " + config.maxIteracoes);
            writer.println("-----------------------------------------------------");
            writer.println();

            writer.println("Metodo bisseccao");
            resultado = methods.bisseccao(config.bisseccaoIntervaloInicio, config.bisseccaoIntervaloFim);
            if (resultado != null) {
                writer.println(resultado.stringFormatada());
            }

            writer.println("Metodo iteracao linear");
            resultado = methods.iteracaoLinear(config.iteracaoLinearX0);
            if (resultado != null) {
                writer.println(resultado.stringFormatada());
            }

            writer.println("Metodo newton");
            resultado = methods.newton(config.newtonX0);
            if (resultado != null) {
                writer.println(resultado.stringFormatada());
            }

            writer.println("Metodo secante");
            resultado = methods.secante(config.secanteX0, config.secanteX1);
            if (resultado != null) {
                writer.println(resultado.stringFormatada());
            }

            writer.println("Metodo regula falsi");
            resultado = methods.regulaFalsi(config.regulaFalsiIntervaloInicio, config.regulaFalsiIntervaloFim);
            if (resultado != null) {
                writer.println(resultado.stringFormatada());
            }

            System.out.println("Resultados de todos os métodos salvos em " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
        }
    }
}