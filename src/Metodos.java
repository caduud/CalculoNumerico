// NumericalMethods.java
import java.util.Arrays;

public class Metodos {

    private TraduzFuncao func;
    private double delta;
    private int maxIteracoes;

    public Metodos(TraduzFuncao func, double delta, int maxIteracoes) {
        this.func = func;
        this.delta = delta;
        this.maxIteracoes = maxIteracoes;
    }

    public Formatacao bisseccao(double a, double b) {
        Formatacao result = new Formatacao("Método da Bissecção");
        result.defineTitulos(Arrays.asList("Iteracao", "a", "b", "xm", "f(xm)", "|b-a|"));

        if (func.expressao(a) * func.expressao(b) >= 0) {
            result.converge = "Não convergiu: f(a) * f(b) >= 0, sem raiz garantida no intervalo.";
            return result;
        }

        double x = 0;
        for (int i = 0; i < maxIteracoes; i++) {
            x = (a + b) / 2;
            double f_x = func.expressao(x);
            double f_a = func.expressao(a);

            result.adicionaIteracao(Arrays.asList((double) i + 1, a, b, x, f_x, Math.abs(b - a)));

            if (Math.abs(f_x) < delta || Math.abs(b - a) < delta) {
                result.raizAproximada = x;
                result.count = i + 1;
                result.converge = "Convergiu";
                return result;
            }

            if (f_a * f_x < 0) {
                b = x;
            } else {
                a = x;
            }
        }

        result.raizAproximada = x;
        result.count = maxIteracoes;
        result.converge = "Não convergiu dentro do número máximo de iterações";
        return result;
    }

    public Formatacao iteracaoLinear(double x0) {
        Formatacao result = new Formatacao("Método da Iteração Linear");
        result.defineTitulos(Arrays.asList("Iteracao", "x", "f(x)", "phi(x)", "|x - x-1|"));

        double x = x0;
        double x_prev;
        for (int i = 0; i < maxIteracoes; i++) {
            x_prev = x;
            x = func.phi(x_prev);
            double f_x = func.expressao(x);

            result.adicionaIteracao(Arrays.asList((double) i + 1, x, f_x, func.phi(x), Math.abs(x - x_prev)));

            if (Math.abs(f_x) < delta || Math.abs(x - x_prev) < delta) {
                result.raizAproximada = x;
                result.count = i + 1;
                result.converge = "Convergiu";
                return result;
            }
        }
        result.raizAproximada = x;
        result.count = maxIteracoes;
        result.converge = "Não convergiu dentro do número máximo de iterações";
        return result;
    }

    public Formatacao newton(double x0) {
        Formatacao result = new Formatacao("Método de Newton");
        result.defineTitulos(Arrays.asList("Iteracao", "x", "f(x)", "f'(x)", "x+1", "|x+1 - x|"));

        double x = x0;
        double x_next = 0;
        for (int i = 0; i < maxIteracoes; i++) {
            double f_x = func.expressao(x);
            double f_derivada_x = func.derivada(x);

            if (Math.abs(f_derivada_x) < 1e-12) { // Evitar divisão por zero
                result.converge = "Não convergiu: Derivada próxima de zero.";
                return result;
            }

            x_next = x - (f_x / f_derivada_x);
            result.adicionaIteracao(Arrays.asList((double) i + 1, x, f_x, f_derivada_x, x_next, Math.abs(x_next - x)));

            if (Math.abs(f_x) < delta || Math.abs(x_next - x) < delta) {
                result.raizAproximada = x_next;
                result.count = i + 1;
                result.converge = "Convergiu";
                return result;
            }
            x = x_next;
        }
        result.raizAproximada = x_next;
        result.count = maxIteracoes;
        result.converge = "Não convergiu dentro do número máximo de iterações";
        return result;
    }

    public Formatacao secante(double x0, double x1) {
        Formatacao result = new Formatacao("Método da Secante");
        result.defineTitulos(Arrays.asList("Iteracao", "x-1", "x", "f(x-1)", "f(x)", "x+1", "|x+1 - x|"));

        double x_prev = x0;
        double x_curr = x1;
        double x_next;

        // Adiciona os pontos iniciais para visualização na tabela
        // result.adicionaIteracao(Arrays.asList(0.0, x_prev, x_curr, func.expressao(x_prev), func.expressao(x_curr), 0.0, Math.abs(x_curr - x_prev)));

        for (int i = 0; i < maxIteracoes; i++) {
            double f_prev = func.expressao(x_prev);
            double f_curr = func.expressao(x_curr);

            // Verificação para evitar divisão por zero
            if (Math.abs(f_curr - f_prev) < 1e-12) {
                result.converge = "Não convergiu: f(xk) - f(xk-1) próximo de zero.";
                return result;
            }

            // --- ESTA É A LINHA MAIS IMPORTANTE ---
            // Verifique se a sua fórmula está exatamente igual a esta
            x_next = x_curr - f_curr * (x_curr - x_prev) / (f_curr - f_prev);

            result.adicionaIteracao(Arrays.asList((double) i + 1, x_prev, x_curr, f_prev, f_curr, x_next, Math.abs(x_next - x_curr)));

            // Critério de parada
            if (Math.abs(func.expressao(x_next)) < delta || Math.abs(x_next - x_curr) < delta) {
                result.raizAproximada = x_next;
                result.count = i + 1;
                result.converge = "Convergiu";
                return result;
            }

            // --- ESTAS LINHAS TAMBÉM SÃO CRUCIAIS ---
            // Atualiza as variáveis para a próxima iteração
            x_prev = x_curr;
            x_curr = x_next;
        }

        result.raizAproximada = x_curr; // Retorna a última raiz calculada
        result.count = maxIteracoes;
        result.converge = "Não convergiu dentro do número máximo de iterações";
        return result;
    }

    public Formatacao regulaFalsi(double a, double b) {
        Formatacao result = new Formatacao("Método da Regula Falsi");
        result.defineTitulos(Arrays.asList("Iteracao", "a", "b", "f(a)", "f(b)", "x", "f(x)", "|b-a|"));

        if (func.expressao(a) * func.expressao(b) >= 0) {
            result.converge = "Não convergiu: f(a) * f(b) >= 0, sem raiz garantida no intervalo.";
            return result;
        }

        double fa = func.expressao(a);
        double fb = func.expressao(b);
        double x = 0;

        for (int i = 0; i < maxIteracoes; i++) {
            if (Math.abs(fb - fa) < 1e-12) { // Evitar divisão por zero
                result.converge = "Não convergiu: f(b) - f(a) próximo de zero.";
                return result;
            }
            x = (a * fb - b * fa) / (fb - fa); // Fórmula mais estável
            double fx = func.expressao(x);

            result.adicionaIteracao(Arrays.asList((double) i + 1, a, b, fa, fb, x, fx, Math.abs(b - a)));

            if (Math.abs(fx) < delta || Math.abs(b - a) < delta) {
                result.raizAproximada = x;
                result.count = i + 1;
                result.converge = "Convergiu";
                return result;
            }

            if (fa * fx < 0) {
                b = x;
                fb = fx;
            } else {
                a = x;
                fa = fx;
            }
        }
        result.raizAproximada = x;
        result.count = maxIteracoes;
        result.converge = "Não convergiu dentro do número máximo de iterações";
        return result;
    }
}