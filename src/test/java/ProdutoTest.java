import GestaoBar.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    private Produto produto;

    @BeforeEach
    public void setUp() {
        produto = new Produto("Água", "Bebida", 1.50, 0.80, 10);
    }

    @Test
    public void testConstrutorBasico() {
        assertEquals("Água", produto.getNome());
        assertEquals("Bebida", produto.getCategoria());
        assertEquals(1.50, produto.getPreco());
        assertEquals(0.80, produto.getPrecoCompra());
        assertEquals(10, produto.getDesconto());
        assertEquals(0, produto.getStock());
        assertNull(produto.getValidade());
        assertEquals("—", produto.getLote());
    }

    @Test
    public void testConstrutorCompleto() {
        Produto completo = new Produto("Sumo", "Bebida", 2.00, 1.00, 20, 50, "2025-12-31", "L123");

        assertEquals("Sumo", completo.getNome());
        assertEquals(2.00, completo.getPreco());
        assertEquals(50, completo.getStock());
        assertEquals("L123", completo.getLote());
        assertNull(completo.getValidade()); // porque não está a ser convertida!
    }

    @Test
    public void testSettersFuncionam() {
        produto.setNome("Cerveja");
        produto.setCategoria("Alcoólica");
        produto.setPreco(2.20);
        produto.setPrecoCompra(1.10);
        produto.setDesconto(15);
        produto.setStock(25);
        produto.setValidade(LocalDate.of(2025, 10, 1));
        produto.setLote("X789");

        assertEquals("Cerveja", produto.getNome());
        assertEquals("Alcoólica", produto.getCategoria());
        assertEquals(2.20, produto.getPreco());
        assertEquals(1.10, produto.getPrecoCompra());
        assertEquals(15, produto.getDesconto());
        assertEquals(25, produto.getStock());
        assertEquals(LocalDate.of(2025, 10, 1), produto.getValidade());
        assertEquals("X789", produto.getLote());
    }

    @Test
    public void testPrecoFinalComDesconto() {
        double precoFinal = produto.getPreco() * (1 - (produto.getDesconto() / 100.0));
        assertEquals(1.35, precoFinal, 0.01); // 10% desconto sobre 1.50
    }

    @Test
    public void testToString() {
        assertEquals("Água", produto.toString());
    }
}
