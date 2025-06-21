import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class JanelaEliminarFunc extends JDialog {
    private Funcionario funcionario;
    private static final String CSV_FILE = "src/main/java/csv/funcionarios.csv";

    public JanelaEliminarFunc(JFrame parent, Funcionario funcionario) {
        super(parent, "Eliminar Funcionário", true);
        this.funcionario = funcionario;

        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblConfirmacao = new JLabel("Tens a certeza que queres eliminar este funcionário?");
        lblConfirmacao.setBounds(30, 30, 340, 30);
        add(lblConfirmacao);

        JLabel lblNome = new JLabel("Nome: " + funcionario.nome);
        lblNome.setBounds(30, 70, 300, 20);
        add(lblNome);

        JButton btnSim = new JButton("Sim");
        btnSim.setBounds(80, 120, 100, 30);
        add(btnSim);

        JButton btnNao = new JButton("Não");
        btnNao.setBounds(200, 120, 100, 30);
        add(btnNao);

        btnSim.addActionListener(e -> {
            eliminarFuncionario();
            dispose();
        });

        btnNao.addActionListener(e -> dispose());
    }

    private void eliminarFuncionario() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(CSV_FILE));
            ArrayList<String> linhas = new ArrayList<>();
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length == 5 &&
                        !(partes[0].equals(funcionario.nome) &&
                                partes[1].equals(funcionario.contacto) &&
                                partes[2].equals(funcionario.turno))) {

                    linhas.add(linha); // mantém linha se não corresponder
                }
            }
            br.close();

            PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE));
            for (String l : linhas) {
                pw.println(l);
            }
            pw.close();

            JOptionPane.showMessageDialog(this, "Funcionário eliminado com sucesso!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao eliminar o funcionário.");
        }
    }
}
