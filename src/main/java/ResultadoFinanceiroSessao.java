public class ResultadoFinanceiroSessao {
    private String filme;
    private String sala;
    private String data;
    private String hora;
    private int bilhetesVendidos;
    private double precoBilhete;
    private double custo;
    private double lucro;

    public ResultadoFinanceiroSessao(String filme, String sala, String data, String hora, int bilhetes, double preco, double custo) {
        this.filme = filme;
        this.sala = sala;
        this.data = data;
        this.hora = hora;
        this.bilhetesVendidos = bilhetes;
        this.precoBilhete = preco;
        this.custo = custo;
        this.lucro = (preco * bilhetes) - custo;
    }

    public String getFilme() { return filme; }
    public String getSala() { return sala; }
    public String getData() { return data; }
    public String getHora() { return hora; }
    public double getLucro() { return lucro; }
    public double getReceita() { return precoBilhete * bilhetesVendidos; }
    public double getCusto() { return custo; }
}