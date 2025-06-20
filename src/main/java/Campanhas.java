public class Campanhas {
    private String nome;
    private String publicoAlvo;
    private String promocao;
    private String filmesAssociados;
    private double preco;
    private boolean ativa;

    public Campanhas(String nome, String publicoAlvo, String promocao,
                    String filmesAssociados, double preco, boolean ativa) {
        this.nome = nome;
        this.publicoAlvo = publicoAlvo;
        this.promocao = promocao;
        this.filmesAssociados = filmesAssociados;
        this.preco = preco;
        this.ativa = ativa;
    }

    public String getNome() { return nome; }
    public String getPublicoAlvo() { return publicoAlvo; }
    public String getPromocao() { return promocao; }
    public String getFilmesAssociados() { return filmesAssociados; }
    public double getPreco() { return preco; }
    public boolean isAtiva() { return ativa; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPublicoAlvo(String publicoAlvo) { this.publicoAlvo = publicoAlvo; }
    public void setPromocao(String promocao) { this.promocao = promocao; }
    public void setFilmesAssociados(String filmesAssociados) { this.filmesAssociados = filmesAssociados; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}
