import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CampanhasTest {

    private static final String FICHEIRO_TESTE = "test_campanhas.csv";

    @Test
    void testCriarCampanhaComSucesso() {
        Campanhas c = new Campanhas("Promo Verão", "Jovens", "2x1", "Shrek, Matrix", 100.0, true);
        assertEquals("Promo Verão", c.getNome());
        assertEquals("Jovens", c.getPublicoAlvo());
        assertEquals("2x1", c.getPromocao());
        assertEquals("Shrek, Matrix", c.getFilmesAssociados());
        assertEquals(100.0, c.getPreco());
        assertTrue(c.isAtiva());
    }

    @Test
    void testEditarCampanha() {
        Campanhas c = new Campanhas("Inverno", "Adultos", "Desconto 50%", "Avatar", 200.0, false);
        c.setPromocao("Desconto 25%");
        c.setAtiva(true);
        assertEquals("Desconto 25%", c.getPromocao());
        assertTrue(c.isAtiva());
    }

    @Test
    void testGuardarCampanhasEmCSV() {
        List<Campanhas> lista = new ArrayList<>();
        lista.add(new Campanhas("Promo A", "Famílias", "Grátis Kids", "Frozen", 50.0, true));
        lista.add(new Campanhas("Promo B", "Estudantes", "2x1", "Matrix", 75.0, false));

        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHEIRO_TESTE))) {
            for (Campanhas c : lista) {
                writer.println(c.getNome() + "," +
                        c.getPublicoAlvo() + "," +
                        c.getPromocao() + "," +
                        c.getFilmesAssociados() + "," +
                        c.getPreco() + "," +
                        c.isAtiva());
            }
        } catch (IOException e) {
            fail("Erro ao guardar CSV: " + e.getMessage());
        }

        File f = new File(FICHEIRO_TESTE);
        assertTrue(f.exists());
        assertTrue(f.length() > 0);

        f.delete();
    }

    @Test
    void testCarregarCampanhasDeCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHEIRO_TESTE))) {
            writer.println("Promo C,Todos,Oferta 1€,Vingadores,120.0,true");
            writer.println("Promo D,Crianças,Grátis Popcorn,Toy Story,80.0,false");
        } catch (IOException e) {
            fail("Erro ao preparar CSV de teste");
        }

        List<Campanhas> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHEIRO_TESTE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 6) {
                    String nome = partes[0];
                    String publico = partes[1];
                    String promocao = partes[2];
                    String filmes = partes[3];
                    double preco = Double.parseDouble(partes[4]);
                    boolean ativa = Boolean.parseBoolean(partes[5]);

                    lista.add(new Campanhas(nome, publico, promocao, filmes, preco, ativa));
                }
            }
        } catch (IOException e) {
            fail("Erro ao carregar CSV de teste");
        }

        assertEquals(2, lista.size());
        assertEquals("Promo C", lista.get(0).getNome());
        assertFalse(lista.get(1).isAtiva());

        new File(FICHEIRO_TESTE).delete();
    }

    @Test
    void testEvitarCampanhasDuplicadas() {
        Campanhas c1 = new Campanhas("Promo E", "Adultos", "Desconto", "Batman", 100.0, true);
        Campanhas c2 = new Campanhas("Promo E", "Adultos", "Desconto", "Batman", 100.0, true);

        List<Campanhas> lista = new ArrayList<>();
        lista.add(c1);

        boolean duplicado = lista.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(c2.getNome()));
        assertTrue(duplicado, "Deveria detetar nome duplicado");
    }

    @Test
    void testAlternarEstadoCampanha() {
        Campanhas c = new Campanhas("Promo F", "Geral", "Oferta", "Todos", 70.0, true);
        c.setAtiva(false);
        assertFalse(c.isAtiva());
        c.setAtiva(true);
        assertTrue(c.isAtiva());
    }
}
