import java.util.Date;

public class Reserva {
    private String responsavel;
    private String telefone;
    private int quantidadePessoas;
    private double precoPago;
    private Date data;

    public Reserva(String responsavel, String telefone, int quantidadePessoas, double precoPago, Date data) {
        this.responsavel = responsavel;
        this.telefone = telefone;
        this.quantidadePessoas = quantidadePessoas;
        this.precoPago = precoPago;
        this.data = data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public double getPrecoPago() {
        return precoPago;
    }

    public void setPrecoPago(double precoPago) {
        this.precoPago = precoPago;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
}

