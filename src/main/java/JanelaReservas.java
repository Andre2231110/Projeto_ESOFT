import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;

public class JanelaReservas extends JDialog {

    private Sala sala;
    private ArrayList<Reserva> listaReservas;

    private JTextField txtResponsavel;
    private JTextField txtTelefone;
    private JTextField txtQuantidade;
    private JTextField txtPreco;
    private JTextField txtData;

    private JTable tabelaReservas;
    private DefaultTableModel modeloTabela;

    private JButton btnAdicionar;
    private JButton btnRemover;

    private final String FICHEIRO_RESERVAS;

    public JanelaReservas(JFrame parent, Sala sala) {
        super(parent, "Reservas da Sala: " + sala.getNome(), true);
        this.sala = sala;
        this.listaReservas = new ArrayList<>();
        this.FICHEIRO_RESERVAS = "src/main/java/csv/reservas.csv";

        setSize(900, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        criarFormulario();
        criarTabela();
        criarBotoes();

        carregarReservasDeCSV();
        atualizarTabela();

        setVisible(true);
    }

    private void criarFormulario() {
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Nova Reserva de Grupo"));

        painelFormulario.add(new JLabel("Responsável do Grupo:"));
        txtResponsavel = new JTextField();
        painelFormulario.add(txtResponsavel);

        painelFormulario.add(new JLabel("Nº Telefone:"));
        txtTelefone = new JTextField();
        painelFormulario.add(txtTelefone);

        painelFormulario.add(new JLabel("Quantidade de Pessoas:"));
        txtQuantidade = new JTextField();
        painelFormulario.add(txtQuantidade);

        painelFormulario.add(new JLabel("Preço Pago (€):"));
        txtPreco = new JTextField();
        painelFormulario.add(txtPreco);

        painelFormulario.add(new JLabel("Data (AAAA-MM-DD):"));
        txtData = new JTextField();
        painelFormulario.add(txtData);

        btnAdicionar = new JButton("Adicionar");
        painelFormulario.add(btnAdicionar);

        add(painelFormulario, BorderLayout.NORTH);

        btnAdicionar.addActionListener(e -> adicionarReserva());
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Responsável", "Telefone", "Quantidade", "Preço (€)", "Data"}, 0);
        tabelaReservas = new JTable(modeloTabela);
        add(new JScrollPane(tabelaReservas), BorderLayout.CENTER);
    }

    private void criarBotoes() {
        btnRemover = new JButton("Remover Reserva");
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSul.add(btnRemover);
        add(painelSul, BorderLayout.SOUTH);

        btnRemover.addActionListener(e -> removerReserva());
    }

    private void adicionarReserva() {
        String responsavel = txtResponsavel.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String quantidadeTexto = txtQuantidade.getText().trim();
        String precoTexto = txtPreco.getText().trim();
        String dataTexto = txtData.getText().trim();

        if (!dadosReservaValidos(responsavel, telefone, quantidadeTexto, precoTexto, dataTexto)) {
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeTexto);
            double preco = Double.parseDouble(precoTexto.replace(",", "."));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date data = sdf.parse(dataTexto);

            Reserva nova = new Reserva(responsavel, telefone, quantidade, preco, data);
            listaReservas.add(nova);
            guardarReservasEmCSV();
            atualizarTabela();
            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro no formato dos dados. Verifica os campos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerReserva() {
        int linha = tabelaReservas.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma reserva selecionada.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja mesmo eliminar esta reserva?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            listaReservas.remove(linha);
            guardarReservasEmCSV();
            atualizarTabela();
        }
    }

    private void limparCampos() {
        txtResponsavel.setText("");
        txtTelefone.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
        txtData.setText("");
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Reserva r : listaReservas) {
            modeloTabela.addRow(new Object[]{
                    r.getResponsavel(), r.getTelefone(), r.getQuantidadePessoas(),
                    String.format("%.2f", r.getPrecoPago()), sdf.format(r.getData())
            });
        }
    }

    private void carregarReservasDeCSV() {
        File ficheiro = new File(FICHEIRO_RESERVAS);
        if (!ficheiro.exists()) return;

        try (Scanner scanner = new Scanner(ficheiro)) {
            listaReservas.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");
                if (partes.length == 5) {
                    String responsavel = partes[0];
                    String telefone = partes[1];
                    int quantidade = Integer.parseInt(partes[2]);
                    double preco = Double.parseDouble(partes[3].replace(",", "."));
                    Date data = sdf.parse(partes[4]);
                    Reserva r = new Reserva(responsavel, telefone, quantidade, preco, data);
                    listaReservas.add(r);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar reservas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarReservasEmCSV() {
        try {
            File ficheiro = new File(FICHEIRO_RESERVAS);
            ficheiro.getParentFile().mkdirs();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try (PrintWriter writer = new PrintWriter(ficheiro)) {
                for (Reserva r : listaReservas) {
                    writer.printf(Locale.US, "%s,%s,%d,%.2f,%s%n",
                            r.getResponsavel(),
                            r.getTelefone(),
                            r.getQuantidadePessoas(),
                            r.getPrecoPago(),
                            sdf.format(r.getData()));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao guardar reservas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean dadosReservaValidos(String responsavel, String telefone, String quantidadeTexto, String precoTexto, String dataTexto) {
        if (responsavel.isEmpty() || telefone.isEmpty() || quantidadeTexto.isEmpty()
                || precoTexto.isEmpty() || dataTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos os campos são obrigatórios.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
