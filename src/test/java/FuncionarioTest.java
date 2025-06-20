import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {

    @Test
    void testCriarFuncionario() {
        Funcionario f = new Funcionario("Ana", "912345678", "Noite", 500.0, "Empregado");
        assertEquals("Ana", f.nome);
        assertEquals("912345678", f.contacto);
        assertEquals("Noite", f.turno);
        assertEquals(500.0, f.lucroTotal);
        assertEquals("Empregado", f.func);
    }

    @Test
    void testLucroZero() {
        Funcionario f = new Funcionario("João", "933112233", "Dia", 0.0, "Cozinheiro");
        assertEquals(0.0, f.lucroTotal);
    }

    @Test
    void testToString() {
        Funcionario f = new Funcionario("Maria", "911223344", "Tarde", 200.0, "Caixa");
        assertEquals("Maria", f.toString());
    }

    @Test
    void testAlterarDados() {
        Funcionario f = new Funcionario("Pedro", "912334455", "Noite", 100.0, "Bar");
        f.nome = "Pedro Novo";
        f.lucroTotal = 150.0;
        assertEquals("Pedro Novo", f.nome);
        assertEquals(150.0, f.lucroTotal);
    }

    @Test
    void testContactoValido() {
        Funcionario f = new Funcionario("Inês", "934567890", "Dia", 50.0, "Empregado");
        assertTrue(f.contacto.matches("\\d{9}"));
    }
}
