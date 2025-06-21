public class Sessao {
    private Filme filme;
    private Sala sala;
    private String data;
    private String horaInicio;
    private String horaFim;

    public Sessao(Filme filme, Sala sala, String data, String horaInicio, String horaFim) {
        this.filme = filme;
        this.sala = sala;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public Filme getFilme() {
        return filme;
    }

    public Sala getSala() {
        return sala;
    }

    public String getData() {
        return data;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String toCSV() {
        return filme.getTitulo() + "," + sala.getNome() + "," + data + "," + horaInicio + "," + horaFim;
    }

    public static Sessao fromCSV(String linha, java.util.List<Filme> filmes, java.util.List<Sala> salas) {
        String[] partes = linha.split(",");
        if (partes.length != 5) return null;

        String titulo = partes[0];
        String nomeSala = partes[1];
        String data = partes[2];
        String horaInicio = partes[3];
        String horaFim = partes[4];

        Filme filme = filmes.stream()
                .filter(f -> f.getTitulo().equalsIgnoreCase(titulo))
                .findFirst().orElse(null);

        Sala sala = salas.stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nomeSala))
                .findFirst().orElse(null);

        if (filme != null && sala != null) {
            return new Sessao(filme, sala, data, horaInicio, horaFim);
        }
        return null;
    }

    @Override
    public String toString() {
        return horaInicio + " → " + horaFim + " (" + sala.getNome() + ")";
    }
}
