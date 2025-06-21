import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessaoTest {

    private Filme filme;
    private Sala sala;
    private Sessao sessao;

    @BeforeEach
    public void setUp() {
        filme = new Filme(
                "Inception",
                148,
                "Dom Cobb é um ladrão de segredos",
                "Sci-Fi",
                "inception.jpg",
                2000.0,
                7.5
        );

        sala = new Sala(
                "Sala IMAX",
                "2D",
                "5x5",
                "Dolby Atmos",
                true,
                true,
                500.0
        );

        sessao = new Sessao(filme, sala, "2025-06-30", "21:00", "23:45");
    }

    @Test
    public void testGetters() {
        assertEquals("Inception", sessao.getFilme().getTitulo());
        assertEquals("Sala IMAX", sessao.getSala().getNome());
        assertEquals("2025-06-30", sessao.getData());
        assertEquals("21:00", sessao.getHoraInicio());
        assertEquals("23:45", sessao.getHoraFim());
    }

    @Test
    public void testToCSV() {
        String esperado = "Inception,Sala IMAX,2025-06-30,21:00,23:45";
        assertEquals(esperado, sessao.toCSV());
    }

    @Test
    public void testToStringFormatado() {
        assertEquals("21:00 → 23:45 (Sala IMAX)", sessao.toString());
    }

    @Test
    public void testFromCSVValido() {
        String linha = "Inception,Sala IMAX,2025-06-30,21:00,23:45";
        List<Filme> filmes = Arrays.asList(filme);
        List<Sala> salas = Arrays.asList(sala);

        Sessao novaSessao = Sessao.fromCSV(linha, filmes, salas);
        assertNotNull(novaSessao);
        assertEquals("Inception", novaSessao.getFilme().getTitulo());
        assertEquals("Sala IMAX", novaSessao.getSala().getNome());
    }

    @Test
    public void testFromCSVInvalido() {
        String linha = "Invalido,Desconhecida,2025-06-30,21:00,23:45";
        List<Filme> filmes = Arrays.asList(filme);
        List<Sala> salas = Arrays.asList(sala);

        Sessao invalida = Sessao.fromCSV(linha, filmes, salas);
        assertNull(invalida);
    }
}
