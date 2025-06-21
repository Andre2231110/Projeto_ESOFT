public class ResultadoFinanceiroFilme {
    private String titulo;
    private int bilhetesVendidos;
    private double precoBilhete;
    private double precoLicenca;
    private double lucro;

    public ResultadoFinanceiroFilme(String titulo, int bilhetesVendidos, double precoBilhete, double precoLicenca) {
        this.titulo = titulo;
        this.bilhetesVendidos = bilhetesVendidos;
        this.precoBilhete = precoBilhete;
        this.precoLicenca = precoLicenca;
        this.lucro = (precoBilhete * bilhetesVendidos) - precoLicenca;
    }

    public String getTitulo() { return titulo; }
    public double getLucro() { return lucro; }
    public double getReceita() { return precoBilhete * bilhetesVendidos; }
    public double getCusto() { return precoLicenca; }
}
