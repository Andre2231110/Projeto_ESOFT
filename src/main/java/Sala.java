public class Sala {

    private String nome;
    private String tipo;
    private String layout;
    private String som;
    private boolean acessivel;

    public Sala(String nome, String tipo, String layout, String som, boolean acessivel) {
        this.nome = nome;
        this.tipo = tipo;
        this.layout = layout;
        this.som = som;
        this.acessivel = acessivel;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLayout() {
        return layout;
    }

    public String getSom() {
        return som;
    }

    public boolean isAcessivel() {
        return acessivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setSom(String som) {
        this.som = som;
    }

    public void setAcessivel(boolean acessivel) {
        this.acessivel = acessivel;
    }
}
