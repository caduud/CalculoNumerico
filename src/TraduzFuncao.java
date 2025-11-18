import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class TraduzFuncao {

    private Expression expr_fx;
    private Expression expr_derivada;
    private Expression expr_phi;

    public TraduzFuncao(String fx, String derivada, String phi) {
        try {
            this.expr_fx = new ExpressionBuilder(fx)
                    .variables("x", "e", "pi")
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao parsear a função f(x): " + fx, e);
        }

        try {
            this.expr_derivada = new ExpressionBuilder(derivada)
                    .variables("x", "e", "pi")
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao parsear a função derivada: " + derivada, e);
        }

        try {
            this.expr_phi = new ExpressionBuilder(phi)
                    .variables("x", "e", "pi")
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao parsear a função phi(x): " + phi, e);
        }
    }

    private double avaliaExpressao(Expression expr, double x) {
        try {
            expr.setVariable("x", x);
            expr.setVariable("e", Math.E);
            expr.setVariable("pi", Math.PI);
            return expr.evaluate();
        } catch (Exception e) {
            System.err.println("Erro ao avaliar a expressão para x = " + x);
            return Double.NaN;
        }
    }

    public double expressao(double x) {
        return avaliaExpressao(this.expr_fx, x);
    }

    public double derivada(double x) {
        return avaliaExpressao(this.expr_derivada, x);
    }

    public double phi(double x) {
        return avaliaExpressao(this.expr_phi, x);
    }
}