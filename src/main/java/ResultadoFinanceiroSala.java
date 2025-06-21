public class ResultadoFinanceiroSala {
    private String nomeSala;
    private double custo;
    private double totalPago;
    private double lucro;

    public ResultadoFinanceiroSala(String nomeSala, double custo, double totalPago) {
        this.nomeSala = nomeSala;
        this.custo = custo;
        this.totalPago = totalPago;
        this.lucro = totalPago - custo;
    }

    public String getNomeSala() { return nomeSala; }
    public double getCusto() { return custo; }
    public double getTotalPago() { return totalPago; }
    public double getLucro() { return lucro; }
}
