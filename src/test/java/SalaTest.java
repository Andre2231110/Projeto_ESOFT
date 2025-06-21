import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {
    @Test
    public void testCriarSalaComSucesso() {
        Sala sala = new Sala("Sala 1", "Normal", "2x2", "Estéreo", true, true, 150.0);

        assertEquals("Sala 1", sala.getNome());
        assertEquals("Normal", sala.getTipo());
        assertEquals("2x2", sala.getLayout());
        assertEquals("Estéreo", sala.getSom());
        assertTrue(sala.isAcessivel());
        assertTrue(sala.isAtiva());
        assertEquals(150.0, sala.getPrecoCusto());
    }

    @Test
    public void testSalaInativaPorDefeito() {
        Sala sala = new Sala("Sala 2", "IMAX", "3x3", "Dolby", false, false, 200.0);
        assertFalse(sala.isAtiva(), "A sala devia estar inativa");
    }

    @Test
    public void testSalaAcessivelTrue() {
        Sala sala = new Sala("Sala 3", "VIP", "4x4", "Surround", true, true, 250.0);
        assertTrue(sala.isAcessivel(), "A sala devia ser acessível");
    }

    @Test
    public void testSalaNaoAcessivel() {
        Sala sala = new Sala("Sala 4", "Standard", "1x1", "Mono", false, true, 90.0);
        assertFalse(sala.isAcessivel(), "A sala não devia ser acessível");
    }

    @Test
    public void testNaoPermitirNomeSalaVazio() {
        Exception excecao = assertThrows(IllegalArgumentException.class, () -> {
            new Sala("", "Normal", "2x2", "Estéreo", true, true, 100.0);
        });

        assertEquals("Nome da sala não pode ser vazio", excecao.getMessage());
    }
}
