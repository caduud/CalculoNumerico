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

    public Formatacao() {
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

    public String stringFormatada() {
        Locale ptBR = new Locale("pt", "BR");
        StringBuilder sb = new StringBuilder();

        sb.append("-----------------------------------------------------\n");
        sb.append("Status da Convergência: ").append(converge).append("\n");

        if (converge.equals("Convergiu")) {
            sb.append(String.format(ptBR, "Raiz Aproximada: %.8f\n", raizAproximada));
            sb.append("Total de Iterações: ").append(count).append("\n");
        }
        else if (converge.contains("Não convergiu dentro do número máximo")) {
            sb.append(String.format(ptBR, "Última Raiz Calculada: %.8f\n", raizAproximada));
            sb.append("Total de Iterações: ").append(count).append("\n");
        }

        if (!iteracoes.isEmpty() && !titulo.isEmpty()) {
            sb.append("\n--- Tabela de Iterações ---\n");
            for (String header : titulo) {
                sb.append(String.format("%-14s", header));
            }
            sb.append("\n");

            for (List<Double> iterationData : iteracoes) {
                for (Double val : iterationData) {
                    if (val.isNaN() || val.isInfinite()) {
                        sb.append(String.format("%-14s", val.toString()));
                    } else {
                        sb.append(String.format(ptBR, "%-14.8f", val));
                    }
                }
                sb.append("\n");
            }
        }

        sb.append("\n");
        return sb.toString();
    }
}