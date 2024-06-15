import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Pedido {

    private static List<Pedido> cardapio = new ArrayList<>();
    private Integer numPedido;
    private String nomePrato;
    private BigDecimal valor;

    public Pedido(Integer numPedido, String nomePrato, BigDecimal valor) {
        this.numPedido = numPedido;
        this.nomePrato = nomePrato;
        this.valor = valor;

        int index = IntStream.range(0, cardapio.size())
                .filter(i -> cardapio.get(i).getNumPedido().equals(numPedido))
                .findFirst()
                .orElse(-1);

        if (index != -1) {
            cardapio.set(index, this);
        } else {
            cardapio.add(this);
        }
    }

    public static List<Pedido> getCardapio() {
        return cardapio;
    }

    public Integer getNumPedido() {
        return numPedido;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public static Pedido procuraPedido(int numPedido) {
        return cardapio.stream().filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst().orElse(null);
    }
}
