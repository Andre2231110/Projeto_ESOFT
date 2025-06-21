import org.junit.jupiter.api.*;
import java.util.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class VendasTest {

    private Map<String, JanelaVendasBar.ItemCarrinho> carrinho;

    @BeforeEach
    void setUp() {
        carrinho = new LinkedHashMap<>();
    }

    @Test
    void testAdicionarProdutoAoCarrinho() {
        // Adiciona 1 pipoca e 2 águas
        carrinho.put("Pipocas", new JanelaVendasBar.ItemCarrinho(3.0));
        carrinho.put("Água", new JanelaVendasBar.ItemCarrinho(1.0));
        carrinho.get("Água").quantidade++;

        assertEquals(2, carrinho.size());
        assertEquals(2, carrinho.get("Água").quantidade);
        assertEquals(2.0, carrinho.get("Água").getTotal());

    }

    @Test
    void testCalculoTotalDoCarrinho() {
        carrinho.put("Pipocas", new JanelaVendasBar.ItemCarrinho(3.0));
        carrinho.put("Água", new JanelaVendasBar.ItemCarrinho(1.5));
        carrinho.get("Água").quantidade++;

        double total = carrinho.values().stream().mapToDouble(JanelaVendasBar.ItemCarrinho::getTotal).sum();
        assertEquals(6.0, total);
    }

    @Test
    void testCalculoBilhetes() {
        double precoBilhete = 5.0;
        int lugaresSelecionados = 3;
        double total = precoBilhete * lugaresSelecionados;

        assertEquals(15.0, total);
    }

    @Test
    void testResumoDeCarrinho() {
        carrinho.put("Bilhete", new JanelaVendasBar.ItemCarrinho(5.0));
        carrinho.put("Pipocas", new JanelaVendasBar.ItemCarrinho(3.0));
        carrinho.get("Pipocas").quantidade = 2;

        List<String> resumo = new ArrayList<>();
        for (Map.Entry<String, JanelaVendasBar.ItemCarrinho> entry : carrinho.entrySet()) {
            String linha = entry.getKey() + " x" + entry.getValue().quantidade + " → " + entry.getValue().getTotal() + "€";
            resumo.add(linha);
        }

        assertTrue(resumo.contains("Bilhete x1 → 5.0€"));
        assertTrue(resumo.contains("Pipocas x2 → 6.0€"));
    }

    @Test
    void testCarrinhoVazio() {
        assertTrue(carrinho.isEmpty());
    }

    @Test
    void testItemCarrinhoTotal() {
        var item = new JanelaVendasBar.ItemCarrinho(2.0);
        item.quantidade = 4;
        assertEquals(8.0, item.getTotal());
    }
    @Test
    void testGuardarVendaProdutosCSV() {
        String ficheiroTeste = "test_vendasProduto.csv";

        carrinho.put("Pipocas", new JanelaVendasBar.ItemCarrinho(2.5));
        carrinho.put("Água", new JanelaVendasBar.ItemCarrinho(1.0));
        carrinho.get("Água").quantidade = 2;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiroTeste))) {
            for (Map.Entry<String, JanelaVendasBar.ItemCarrinho> entry : carrinho.entrySet()) {
                String nome = entry.getKey();
                JanelaVendasBar.ItemCarrinho item = entry.getValue();
                if (!nome.equals("Bilhete")) {
                    pw.println(nome + ";" + item.quantidade + ";" +
                            String.format("%.2f", item.getTotal()).replace(",", "."));
                }
            }
        } catch (IOException e) {
            fail("Erro ao guardar CSV de teste: " + e.getMessage());
        }

        File file = new File(ficheiroTeste);
        assertTrue(file.exists(), "Ficheiro CSV não foi criado");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> linhas = br.lines().toList();
            assertEquals(2, linhas.size(), "CSV deve conter duas linhas");

            assertTrue(linhas.get(0).startsWith("Pipocas"));
            assertTrue(linhas.get(1).contains("2")); // Quantidade de Águas

        } catch (IOException e) {
            fail("Erro ao ler CSV de teste: " + e.getMessage());
        }

        file.delete();
    }
    @Test
    void testGuardarVendaBilhetesCSV() {
        String ficheiroTeste = "test_vendasBilhete.csv";

        // Dados simulados
        String tituloFilme = "Matrix";
        String horaInicio = "18:00";
        String horaFim = "20:30";
        String nomeSala = "Sala 1";
        int quantidade = 3;
        double total = quantidade * 5.00;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiroTeste))) {
            String linha = String.join(";", tituloFilme,
                    horaInicio + " → " + horaFim,
                    nomeSala,
                    String.valueOf(quantidade),
                    String.format("%.2f", total).replace(",", "."));
            pw.println(linha);
        } catch (IOException e) {
            fail("Erro ao guardar bilhetes no CSV de teste: " + e.getMessage());
        }

        File file = new File(ficheiroTeste);
        assertTrue(file.exists(), "Ficheiro de bilhetes não foi criado");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha = br.readLine();
            assertNotNull(linha);
            assertTrue(linha.contains("Matrix"));
            assertTrue(linha.contains("3"));
            assertTrue(linha.endsWith("15.00"));
        } catch (IOException e) {
            fail("Erro ao ler ficheiro de bilhetes: " + e.getMessage());
        }

        file.delete();
    }
    @Test
    void testGuardarVendaUtilizadorCSV() {
        String ficheiroTeste = "test_vendasUtilizador.csv";

        String nomeUser = "Ana";
        double total = 12.50;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiroTeste))) {
            pw.println(nomeUser + ";" + String.format("%.2f", total).replace(",", "."));
        } catch (IOException e) {
            fail("Erro ao guardar venda do utilizador: " + e.getMessage());
        }

        File file = new File(ficheiroTeste);
        assertTrue(file.exists(), "Ficheiro de utilizador não foi criado");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha = br.readLine();
            assertNotNull(linha);
            assertTrue(linha.startsWith("Ana;"));
            assertTrue(linha.endsWith("12.50"));
        } catch (IOException e) {
            fail("Erro ao ler ficheiro de utilizador: " + e.getMessage());
        }

        file.delete();
    }


}
