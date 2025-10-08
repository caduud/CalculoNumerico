// MethodResult.java
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Formatacao {
    public String nomeMetodo;
    public List<String> titulo;
    public List<List<Double>> iteracoes;
    public double raizAproximada;
    public int count;
    public String converge;

    public Formatacao(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
        this.titulo = new ArrayList<>();
        this.iteracoes = new ArrayList<>();
        this.raizAproximada = Double.NaN;
        this.count = 0;
        this.converge = "Não executado";
    }

    public void adicionaIteracao(List<Double> data) {
        iteracoes.add(data);
    }

    public void defineTitulos(List<String> titulo) {
        this.titulo = titulo;
    }

    // Método modificado para uma saída mais clara e em português
    public String stringFormatada() {
        // Usar Locale para garantir que o ponto decimal seja uma vírgula
        Locale ptBR = new Locale("pt", "BR");
        StringBuilder sb = new StringBuilder();

        sb.append(nomeMetodo).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("Status da Convergência: ").append(converge).append("\n");

        // Só mostra a raiz e iterações se o método convergiu
        if (converge.equals("Convergiu")) {
            sb.append(String.format(ptBR, "Raiz Aproximada: %.8f\n", raizAproximada));
            sb.append("Total de Iterações: ").append(count).append("\n");
        }

        if (!iteracoes.isEmpty() && !titulo.isEmpty()) {
            sb.append("\n--- Tabela de Iterações ---\n");
            // Cabeçalhos
            for (String header : titulo) {
                sb.append(String.format("%-14s", header));
            }
            sb.append("\n");

            // Dados
            for (List<Double> iterationData : iteracoes) {
                for (Double val : iterationData) {
                    sb.append(String.format(ptBR, "%-14.8f", val));
                }
                sb.append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}