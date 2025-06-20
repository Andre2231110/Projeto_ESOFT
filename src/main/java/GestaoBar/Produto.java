
package GestaoBar;
public class Produto {
    public String nome;
    public String categoria;
    public double preco;
    public int desconto;

    public Produto(String nome, String categoria, double preco, int desconto) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.desconto = desconto;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setDesconto(int desconto) { this.desconto = desconto; }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNome() {
        return nome;
    }

    public int getDesconto() {
        return desconto;
    }

    @Override
    public String toString() {
        return nome; // para aparecer bem na lista, se usares Produto diretamente
    }
}
