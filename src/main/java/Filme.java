public class Filme {
    private String titulo;
    private int duracao;
    private String sinopse;
    private String genero;
    private String imagem;
    private int lugaresOcupados;
    private int capacidade;

    public Filme(String titulo, int duracao, String sinopse, String genero, String imagem) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.sinopse = sinopse;
        this.genero = genero;
        this.imagem = imagem;
    }

    public String getTitulo() { return titulo; }
    public int getDuracao() { return duracao; }
    public String getSinopse() { return sinopse; }
    public String getGenero() { return genero; }
    public String getImagem() { return imagem; }
    public int getLugaresOcupados() { return lugaresOcupados; }
    public int getCapacidade() { return capacidade; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setImagem(String imagem) { this.imagem = imagem; }


}
