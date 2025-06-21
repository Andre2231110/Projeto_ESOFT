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
        setLayout(null);
        getContentPane().setBackground(new Color(240, 255, 240));

        JLabel lblTitulo = new JLabel("Reservas - " + sala.getNome(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(350, 10, 200, 25);
        add(lblTitulo);

        criarFormulario();
        criarTabela();
        criarBotoes();

        carregarReservasDeCSV();
        atualizarTabela();

        setVisible(true);
    }

    private void criarFormulario() {
        int x = 500;

        JLabel lblResponsavel = new JLabel("Responsável:");
        lblResponsavel.setBounds(x, 60, 120, 25);
        add(lblResponsavel);
        txtResponsavel = new JTextField();
        txtResponsavel.setBounds(x + 130, 60, 150, 25);
        add(txtResponsavel);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(x, 100, 120, 25);
        add(lblTelefone);
        txtTelefone = new JTextField();
        txtTelefone.setBounds(x + 130, 100, 150, 25);
        add(txtTelefone);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setBounds(x, 140, 120, 25);
        add(lblQuantidade);
        txtQuantidade = new JTextField();
        txtQuantidade.setBounds(x + 130, 140, 150, 25);
        add(txtQuantidade);

        JLabel lblPreco = new JLabel("Preço Pago (€):");
        lblPreco.setBounds(x, 180, 120, 25);
        add(lblPreco);
        txtPreco = new JTextField();
        txtPreco.setBounds(x + 130, 180, 150, 25);
        add(txtPreco);

        JLabel lblData = new JLabel("Data (AAAA-MM-DD):");
        lblData.setBounds(x, 220, 150, 25);
        add(lblData);
        txtData = new JTextField();
        txtData.setBounds(x + 130, 220, 150, 25);
        add(txtData);

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(x + 90, 270, 120, 30);
        add(btnAdicionar);

        btnAdicionar.addActionListener(e -> adicionarReserva());
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Responsável", "Telefone", "Quantidade", "Preço (€)", "Data"}, 0);
        tabelaReservas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaReservas);
        scroll.setBounds(30, 60, 430, 300);
        scroll.getViewport().setBackground(new Color(220, 255, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scroll);
    }

    private void criarBotoes() {
        btnRemover = new JButton("Remover Reserva");
        btnRemover.setBounds(30, 380, 180, 30);
        btnRemover.setBackground(Color.RED);
        btnRemover.setForeground(Color.WHITE);
        add(btnRemover);

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
        String nomeSalaAtual = sala.getNome();
        try (Scanner scanner = new Scanner(ficheiro)) {
            listaReservas.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");
                if (partes.length == 6 && partes[5].equalsIgnoreCase(nomeSalaAtual)) {
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
                    writer.printf(Locale.US, "%s,%s,%d,%.2f,%s,%s%n",
                            r.getResponsavel(),
                            r.getTelefone(),
                            r.getQuantidadePessoas(),
                            r.getPrecoPago(),
                            sdf.format(r.getData()),
                            sala.getNome()
                    );
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
