import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JanelaEditarSessao extends JDialog {
    private JTextField txtData;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFim;
    private JComboBox<String> cmbSala;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Sessao sessaoEditada;
    private List<Sala> salas;

    public JanelaEditarSessao(JFrame parent, Sessao sessaoOriginal, List<Sala> salasDisponiveis) {
        super(parent, "Editar Sessão", true);
        this.salas = salasDisponiveis;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));
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

        painel.add(new JLabel("Sala:"));
        cmbSala = new JComboBox<>();
        for (Sala sala : salas) {
            if (sala.isAtiva()) cmbSala.addItem(sala.getNome());
        }
        cmbSala.setSelectedItem(sessaoOriginal.getSala().getNome());
        painel.add(cmbSala);

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
            String nomeSalaSelecionada = (String) cmbSala.getSelectedItem();

            if (novaData.isEmpty() || novaHoraInicio.isEmpty() || novaHoraFim.isEmpty() || nomeSalaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Preenche todos os campos!");
                return;
            }

            Sala novaSala = salas.stream()
                    .filter(s -> s.getNome().equalsIgnoreCase(nomeSalaSelecionada))
                    .findFirst().orElse(null);

            if (novaSala == null) {
                JOptionPane.showMessageDialog(this, "Sala inválida.");
                return;
            }

            sessaoEditada = new Sessao(sessaoOriginal.getFilme(), novaSala, novaData, novaHoraInicio, novaHoraFim);
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
