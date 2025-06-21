import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class JanelaEditarFunc extends JDialog {
    private JTextField txtNome;
    private JTextField txtContacto;
    private JTextField txtTurno;
    private JTextField txtLucro;
    private JTextField txtFuncao;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Funcionario funcionario;
    private String nomeOriginal;
    private String contactoOriginal;
    private String turnoOriginal;

    private static final String CSV_FILE = "src/main/java/csv/funcionarios.csv";

    public JanelaEditarFunc(JFrame parent, Funcionario funcionario) {
        super(parent, "Editar Funcionário", true);
        this.funcionario = funcionario;
        this.nomeOriginal = funcionario.nome;
        this.contactoOriginal = funcionario.contacto;
        this.turnoOriginal = funcionario.turno;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);
        txtNome = new JTextField(funcionario.nome);
        txtNome.setBounds(140, 30, 200, 25);
        add(txtNome);

        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setBounds(30, 70, 100, 25);
        add(lblContacto);
        txtContacto = new JTextField(funcionario.contacto);
        txtContacto.setBounds(140, 70, 200, 25);
        add(txtContacto);

        JLabel lblTurno = new JLabel("Turno:");
        lblTurno.setBounds(30, 110, 100, 25);
        add(lblTurno);
        txtTurno = new JTextField(funcionario.turno);
        txtTurno.setBounds(140, 110, 200, 25);
        add(txtTurno);

        JLabel lblLucro = new JLabel("Lucro Total:");
        lblLucro.setBounds(30, 150, 100, 25);
        add(lblLucro);
        txtLucro = new JTextField(String.valueOf(funcionario.lucroTotal));
        txtLucro.setBounds(140, 150, 200, 25);
        add(txtLucro);

        JLabel lblFuncao = new JLabel("Função:");
        lblFuncao.setBounds(30, 190, 100, 25);
        add(lblFuncao);
        txtFuncao = new JTextField(funcionario.func);
        txtFuncao.setBounds(140, 190, 200, 25);
        add(txtFuncao);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(70, 250, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 250, 100, 30);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                funcionario.nome = txtNome.getText();
                funcionario.contacto = txtContacto.getText();
                funcionario.turno = txtTurno.getText();
                funcionario.lucroTotal = Double.parseDouble(txtLucro.getText());
                funcionario.func = txtFuncao.getText();

                atualizarCSV();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lucro deve ser um número válido.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void atualizarCSV() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(CSV_FILE));
            ArrayList<String> linhas = new ArrayList<>();
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                // Corrigido: compara todos os campos originais para garantir que é o funcionário certo
                if (partes.length == 5 &&
                        partes[0].equals(nomeOriginal) &&
                        partes[1].equals(contactoOriginal) &&
                        partes[2].equals(turnoOriginal)) {

                    // Atualiza com os novos dados
                    linhas.add(funcionario.nome + "," + funcionario.contacto + "," +
                            funcionario.turno + "," + funcionario.lucroTotal + "," + funcionario.func);
                } else {
                    linhas.add(linha); // mantém linha original
                }
            }
            br.close();

            // Reescreve o CSV com os dados atualizados
            PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE));
            for (String l : linhas) {
                pw.println(l);
            }
            pw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o ficheiro CSV.");
        }
    }

}
