import javax.swing.*;
import java.awt.*;

public class JanelaEditarSessao extends JDialog {
    private JTextField txtData;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFim;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Sessao sessaoEditada;

    public JanelaEditarSessao(JFrame parent, Sessao sessaoOriginal) {
        super(parent, "Editar Sessão", true);

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Data (dd/MM/yyyy):"));
        txtData = new JTextField(sessaoOriginal.getData());
        painel.add(txtData);

        painel.add(new JLabel("Hora de Início (hh:mm):"));
        txtHoraInicio = new JTextField(sessaoOriginal.getHoraInicio());
        painel.add(txtHoraInicio);

        painel.add(new JLabel("Hora de Fim (hh:mm):"));
        txtHoraFim = new JTextField(sessaoOriginal.getHoraFim());
        painel.add(txtHoraFim);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnGuardar);
        painelBotoes.add(btnCancelar);

        add(painel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String novaData = txtData.getText().trim();
            String novaHoraInicio = txtHoraInicio.getText().trim();
            String novaHoraFim = txtHoraFim.getText().trim();

            if (novaData.isEmpty() || novaHoraInicio.isEmpty() || novaHoraFim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preenche todos os campos!");
                return;
            }

            // Cria nova sessão com os dados atualizados
            sessaoEditada = new Sessao(sessaoOriginal.getFilme(), novaData, novaHoraInicio, novaHoraFim);

            JOptionPane.showMessageDialog(this, "Sessão atualizada com sucesso!");
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            sessaoEditada = null;
            dispose();
        });

        setVisible(true);
    }

    public Sessao getSessaoEditada() {
        return sessaoEditada;
    }
}
