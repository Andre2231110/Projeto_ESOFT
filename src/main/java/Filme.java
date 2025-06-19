public class Filme {
    private String titulo;
    private int duracao;
    private String sinopse;
    private String genero;
    private String imagem;

    public Filme(String titulo, int duracao, String sinopse, String genero, String imagem) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.sinopse = sinopse;
        this.genero = genero;
        this.imagem = imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagem() {
        return imagem;
    }
}
