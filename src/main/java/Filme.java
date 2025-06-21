public class Filme {
    private String titulo;
    private int duracao;
    private String sinopse;
    private String genero;
    private String imagem;
    private double precoLicenca;
    private double precoBilhete;

    // Construtor
    public Filme(String titulo, int duracao, String sinopse, String genero, String imagem,
                 double precoLicenca, double precoBilhete) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.sinopse = sinopse;
        this.genero = genero;
        this.imagem = imagem;
        this.precoLicenca = precoLicenca;
        this.precoBilhete = precoBilhete;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getGenero() {
        return genero;
    }

    public String getImagem() {
        return imagem;
    }

    public double getPrecoLicenca() {
        return precoLicenca;
    }

    public double getPrecoBilhete() {
        return precoBilhete;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setPrecoLicenca(double precoLicenca) {
        this.precoLicenca = precoLicenca;
    }

    public void setPrecoBilhete(double precoBilhete) {
        this.precoBilhete = precoBilhete;
    }

    // Método para construir Filme a partir de CSV
    public static Filme fromCSV(String linha) {
        String[] partes = linha.split(";");
        if (partes.length < 7) return null;

        String titulo = partes[0];
        int duracao = Integer.parseInt(partes[1]);
        String sinopse = partes[2];
        String genero = partes[3];
        String imagem = partes[4];
        double precoLicenca = Double.parseDouble(partes[5]);
        double precoBilhete = Double.parseDouble(partes[6]);

        return new Filme(titulo, duracao, sinopse, genero, imagem, precoLicenca, precoBilhete);
    }

    // Método para exportar para CSV
    public String toCSV() {
        return titulo + ";" + duracao + ";" + sinopse + ";" + genero + ";" +
                imagem + ";" + precoLicenca + ";" + precoBilhete;
    }
}
