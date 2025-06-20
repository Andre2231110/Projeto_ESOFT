public class Funcionario {
    public String nome;
    public String contacto;
    public String turno;
    public String func;
    public double lucroTotal;


    public Funcionario(String nome, String contacto, String turno,Double lucroTotal, String func) {
        this.nome = nome;
        this.contacto = contacto;
        this.turno = turno;
        this.lucroTotal = lucroTotal;
        this.func=func;

    }

    @Override
    public String toString() {
        return nome;
    }
}
