import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class JanelaAdicionarSessao extends JDialog {
    private JTextField txtData;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFim;
    private JButton btnAdicionar;
    private JButton btnCancelar;

    private Filme filme;
    private Sessao novaSessao;
    private static final String FICHEIRO_SESSOES = "src/main/java/csv/sessoes.csv";

    public JanelaAdicionarSessao(JFrame parent, Filme filme) {
        super(parent, "Adicionar Sessão", true);
        this.filme = filme;

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
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
            int fimHoras = (totalMinutos / 60)%24;
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

        if (data.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
            return;
        }

        novaSessao = new Sessao(filme, data, horaInicio, horaFim);
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
