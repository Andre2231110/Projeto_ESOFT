import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FilmesTest {

    @Test
    void testCriarFilme() {
        Filme f = new Filme("Inception", 148, "Sonhos dentro de sonhos", "Sci-Fi",
                "imagem.jpg", 300.0, 7.5);

        assertEquals("Inception", f.getTitulo());
        assertEquals(148, f.getDuracao());
        assertEquals("Sonhos dentro de sonhos", f.getSinopse());
        assertEquals("Sci-Fi", f.getGenero());
        assertEquals("imagem.jpg", f.getImagem());
        assertEquals(300.0, f.getPrecoLicenca());
        assertEquals(7.5, f.getPrecoBilhete());
    }

    @Test
    void testEditarFilme() {
        Filme f = new Filme("Antigo", 100, "Sinopse", "Drama", "old.png", 200.0, 5.0);
        f.setTitulo("Novo");
        f.setDuracao(120);
        f.setImagem("novo.png");

        assertEquals("Novo", f.getTitulo());
        assertEquals(120, f.getDuracao());
        assertEquals("novo.png", f.getImagem());
    }

    @Test
    void testToCSV() {
        Filme f = new Filme("Avatar", 162, "Pandora", "Fantasia", "avatar.jpg", 400.0, 8.0);
        String csv = f.toCSV();

        assertTrue(csv.contains("Avatar"));
        assertTrue(csv.contains("162"));
        assertTrue(csv.contains("400.0"));
    }

    @Test
    void testFromCSV() {
        String linha = "Avatar;162;Pandora;Fantasia;avatar.jpg;400.0;8.0";
        Filme f = Filme.fromCSV(linha);

        assertNotNull(f);
        assertEquals("Avatar", f.getTitulo());
        assertEquals(162, f.getDuracao());
        assertEquals("avatar.jpg", f.getImagem());
        assertEquals(8.0, f.getPrecoBilhete());
    }

    @Test
    void testDuracaoValida() {
        Filme f = new Filme("Rápido", 90, "Carros", "Ação", "carros.png", 100.0, 4.0);
        assertTrue(f.getDuracao() > 0 && f.getDuracao() < 300, "Duração deve ser realista");
    }
}
