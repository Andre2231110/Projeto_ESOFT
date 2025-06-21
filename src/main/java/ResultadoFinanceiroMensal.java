public class ResultadoFinanceiroMensal {
    private String mesAno; // Ex: "2025-06"
    private double lucroTotal;

    public ResultadoFinanceiroMensal(String mesAno, double lucroTotal) {
        this.mesAno = mesAno;
        this.lucroTotal = lucroTotal;
    }

    public String getMesAno() { return mesAno; }
    public double getLucroTotal() { return lucroTotal; }
}
