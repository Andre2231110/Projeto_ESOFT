
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

    @Override
    public String toString() {
        return nome; // para aparecer bem na lista, se usares Produto diretamente
    }
}
