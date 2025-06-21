import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JanelaAdicionarSessao extends JDialog {
    private JTextField txtData;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFim;
    private JComboBox<String> cmbSala;

    private JButton btnAdicionar;
    private JButton btnCancelar;

    private Filme filme;
    private Sessao novaSessao;
    private List<Sala> salasDisponiveis;

    private static final String FICHEIRO_SESSOES = "src/main/java/csv/sessoes.csv";

    public JanelaAdicionarSessao(JFrame parent, Filme filme, List<Sala> salas) {
        super(parent, "Adicionar Sessão", true);
        this.filme = filme;
        this.salasDisponiveis = salas;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Data (dd/MM/yyyy):"));
        txtData = new JTextField();
        painel.add(txtData);

        painel.add(new JLabel("Hora Início (hh:mm):"));
        txtHoraInicio = new JTextField();
        painel.add(txtHoraInicio);

        painel.add(new JLabel("Hora Fim (calculado):"));
        txtHoraFim = new JTextField();
        txtHoraFim.setEditable(false);
        painel.add(txtHoraFim);

        painel.add(new JLabel("Sala:"));
        cmbSala = new JComboBox<>();
        for (Sala s : salasDisponiveis) {
            if (s.isAtiva()) cmbSala.addItem(s.getNome());
        }
        painel.add(cmbSala);

        btnAdicionar = new JButton("Adicionar");
        btnCancelar = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnCancelar);

        add(painel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarSessao());
        btnCancelar.addActionListener(e -> dispose());

        txtHoraInicio.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizarFim(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizarFim(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizarFim(); }
        });

        setVisible(true);
    }

    private void atualizarFim() {
        String horaInicio = txtHoraInicio.getText().trim();
        int duracao = filme.getDuracao();
        String fim = calcularHoraFim(horaInicio, duracao);
        txtHoraFim.setText(fim);
    }

    private String calcularHoraFim(String horaInicio, int duracaoMinutos) {
        try {
            String[] partes = horaInicio.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            int totalMinutos = horas * 60 + minutos + duracaoMinutos;
            int fimHoras = (totalMinutos / 60) % 24;
            int fimMinutos = totalMinutos % 60;

            return String.format("%02d:%02d", fimHoras, fimMinutos);
        } catch (Exception e) {
            return "";
        }
    }

    private void adicionarSessao() {
        String data = txtData.getText().trim();
        String horaInicio = txtHoraInicio.getText().trim();
        String horaFim = txtHoraFim.getText().trim();
        String nomeSala = (String) cmbSala.getSelectedItem();

        if (data.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty() || nomeSala == null) {
            JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
            return;
        }

        Sala salaSelecionada = salasDisponiveis.stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nomeSala))
                .findFirst().orElse(null);

        if (salaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Sala inválida.");
            return;
        }

        novaSessao = new Sessao(filme, salaSelecionada, data, horaInicio, horaFim);
        guardarSessaoCSV(novaSessao);

        JOptionPane.showMessageDialog(this, "Sessão adicionada com sucesso!");
        dispose();
    }

    private void guardarSessaoCSV(Sessao sessao) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHEIRO_SESSOES, true))) {
            writer.println(sessao.toCSV());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar sessão no CSV.");
        }
    }

    public Sessao getSessaoCriada() {
        return novaSessao;
    }
}
