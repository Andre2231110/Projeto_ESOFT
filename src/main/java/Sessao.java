public class Sessao {
    private Filme filme;
    private String data;
    private String horaInicio;
    private String horaFim;

    public Sessao(Filme filme, String data, String horaInicio, String horaFim) {
        this.filme = filme;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
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

    public Filme getFilme() { return filme; }
    public String getData() { return data; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFim() { return horaFim; }

    public String toCSV() {
        return filme.getTitulo() + ";" + data + ";" + horaInicio + ";" + horaFim;
    }

    public static Sessao fromCSV(String linha, java.util.List<Filme> filmes) {
        String[] partes = linha.split(";");
        if (partes.length != 4) return null;

        String titulo = partes[0];
        String data = partes[1];
        String horaInicio = partes[2];
        String horaFim = partes[3];

        for (Filme f : filmes) {
            if (f.getTitulo().equalsIgnoreCase(titulo)) {
                return new Sessao(f, data, horaInicio, horaFim);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return horaInicio + " → " + horaFim;
    }
}
