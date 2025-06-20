import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;

public class JanelaAdicionarFunc extends JDialog {

    private JTextField txtNome, txtContacto, txtTurno, txtFuncao, txtLucro;
    private JButton btnGuardar, btnCancelar;
    private static final String CSV_FILE = "funcionarios.csv";

    public JanelaAdicionarFunc(JFrame parent) {
        super(parent, "Adicionar Funcionário", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(150, 30, 200, 25);
        add(txtNome);

        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setBounds(30, 70, 100, 25);
        add(lblContacto);
        txtContacto = new JTextField();
        txtContacto.setBounds(150, 70, 200, 25);
        add(txtContacto);

        JLabel lblTurno = new JLabel("Turno:");
        lblTurno.setBounds(30, 110, 100, 25);
        add(lblTurno);
        txtTurno = new JTextField();
        txtTurno.setBounds(150, 110, 200, 25);
        add(txtTurno);

        JLabel lblLucro = new JLabel("Lucro Total:");
        lblLucro.setBounds(30, 150, 100, 25);
        add(lblLucro);
        txtLucro = new JTextField();
        txtLucro.setBounds(150, 150, 200, 25);
        add(txtLucro);

        JLabel lblFuncao = new JLabel("Função:");
        lblFuncao.setBounds(30, 190, 100, 25);
        add(lblFuncao);
        txtFuncao = new JTextField();
        txtFuncao.setBounds(150, 190, 200, 25);
        add(txtFuncao);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(80, 250, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 250, 100, 30);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                String contacto = txtContacto.getText().trim();
                String turno = txtTurno.getText().trim();
                double lucro = Double.parseDouble(txtLucro.getText().trim());
                String funcao = txtFuncao.getText().trim();

                if (nome.isEmpty() || contacto.isEmpty() || turno.isEmpty() || funcao.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
                    return;
                }

                guardarNoCSV(nome, contacto, turno, lucro, funcao);
                JOptionPane.showMessageDialog(this, "Funcionário guardado com sucesso!");
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lucro inválido. Insere um número.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao guardar o funcionário.");
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarNoCSV(String nome, String contacto, String turno, double lucro, String funcao) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            pw.println(nome + "," + contacto + "," + turno + "," + lucro + "," + funcao);
        }
    }
}
