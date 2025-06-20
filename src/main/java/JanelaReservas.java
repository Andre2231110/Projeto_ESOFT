import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

        setSize(800, 450);
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
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2));
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

        btnAdicionar = new JButton("Adicionar");
        painelFormulario.add(btnAdicionar);

        add(painelFormulario, BorderLayout.NORTH);

        btnAdicionar.addActionListener(e -> adicionarReserva());
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Responsável", "Telefone", "Quantidade", "Preço (€)"}, 0);
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

        if (responsavel.isEmpty() || telefone.isEmpty() || quantidadeTexto.isEmpty() || precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos os campos são obrigatórios.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;
        double preco;
        try {
            quantidade = Integer.parseInt(quantidadeTexto);
            preco = Double.parseDouble(precoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade deve ser número inteiro e preço deve ser número decimal.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reserva nova = new Reserva(responsavel, telefone, quantidade, preco);
        listaReservas.add(nova);
        guardarReservasEmCSV();
        atualizarTabela();
        limparCampos();
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
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Reserva r : listaReservas) {
            modeloTabela.addRow(new Object[]{
                    r.getResponsavel(), r.getTelefone(), r.getQuantidadePessoas(), String.format("%.2f", r.getPrecoPago())
            });
        }
    }

    private void carregarReservasDeCSV() {
        File ficheiro = new File(FICHEIRO_RESERVAS);
        if (!ficheiro.exists()) return;

        try (Scanner scanner = new Scanner(ficheiro)) {
            listaReservas.clear();
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");
                if (partes.length == 5) {
                    String responsavel = partes[0];
                    String telefone = partes[1];
                    int quantidade = Integer.parseInt(partes[2]);
                    double preco = Double.parseDouble(partes[3].replace(",", ".")); // <- aqui a alteração
                    String nomeSala = partes[4];
                    if (nomeSala.equalsIgnoreCase(sala.getNome())) {
                        Reserva r = new Reserva(responsavel, telefone, quantidade, preco);
                        listaReservas.add(r);
                    }
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

            ArrayList<String> linhasExistentes = new ArrayList<>();
            if (ficheiro.exists()) {
                try (Scanner scanner = new Scanner(ficheiro)) {
                    while (scanner.hasNextLine()) {
                        String linha = scanner.nextLine();
                        if (!linha.trim().endsWith("," + sala.getNome())) {
                            linhasExistentes.add(linha);
                        }
                    }
                }
            }

            try (PrintWriter writer = new PrintWriter(ficheiro)) {
                for (String linha : linhasExistentes) {
                    writer.println(linha);
                }
                for (Reserva r : listaReservas) {
                    writer.printf("%s,%s,%d,%s,%s%n",
                            r.getResponsavel(), r.getTelefone(), r.getQuantidadePessoas(),
                            String.format(Locale.US, "%.2f", r.getPrecoPago()), sala.getNome());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao guardar reservas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
