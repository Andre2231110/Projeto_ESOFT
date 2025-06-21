public class ResultadoFinanceiroProduto {
    private String nome;
    private double precoVenda;
    private double precoCompra;
    private double lucro;

    public ResultadoFinanceiroProduto(String nome, double precoVenda, double precoCompra) {
        this.nome = nome;
        this.precoVenda = precoVenda;
        this.precoCompra = precoCompra;
        this.lucro = precoVenda - precoCompra;
    }

    public String getNome() { return nome; }
    public double getPrecoVenda() { return precoVenda; }
    public double getPrecoCompra() { return precoCompra; }
    public double getLucro() { return lucro; }
}
