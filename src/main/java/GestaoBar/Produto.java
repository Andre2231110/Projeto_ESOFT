package GestaoBar;
import java.time.LocalDate;

public class Produto {
    private String nome;
    private String categoria;
    private double preco;
    private double precoCompra; // NOVO CAMPO
    private int desconto;
    private int stock;
    private LocalDate validade;
    private String lote;

    // Construtor básico (sem validade, lote, stock)
    public Produto(String nome, String categoria, double preco, double precoCompra, int desconto) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.precoCompra = precoCompra;
        this.desconto = desconto;
        this.stock = 0;
        this.validade = null;
        this.lote = "—";
    }

    // Construtor completo
    public Produto(String nome, String categoria, double preco, double precoCompra, int desconto,
                   int stock, String validade, String lote) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.precoCompra = precoCompra;
        this.desconto = desconto;
        this.stock = stock;
        this.validade = null; // Podes depois tratar validade a partir de String para LocalDate
        this.lote = lote;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public double getPrecoCompra() { return precoCompra; } // NOVO GETTER
    public void setPrecoCompra(double precoCompra) { this.precoCompra = precoCompra; } // NOVO SETTER

    public int getDesconto() { return desconto; }
    public void setDesconto(int desconto) { this.desconto = desconto; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    @Override
    public String toString() {
        return nome;
    }
}
