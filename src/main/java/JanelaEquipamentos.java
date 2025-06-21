import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class JanelaEquipamentos extends JFrame {

    private Sala sala;
    private ArrayList<Equipamento> listaEquipamentos = new ArrayList<>();
    private DefaultTableModel modeloTabela;
    private JTable tabelaEquipamentos;
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;
    private JButton btnAdicionar, btnRemover;

    private final String FICHEIRO_EQUIPAMENTOS;

    public JanelaEquipamentos(JFrame parent, Sala sala) {
        super("Equipamentos da " + sala.getNome());
        this.sala = sala;
        this.FICHEIRO_EQUIPAMENTOS = "src/main/java/csv/equipamentos.csv";

        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 255, 240));

        JButton btnBack = new JButton("< Back");
        btnBack.setBounds(10, 10, 80, 25);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Serif", Font.PLAIN, 14));
        btnBack.addActionListener(e -> dispose());
        add(btnBack);

        JLabel lblTitulo = new JLabel("Equipamentos da Sala " + sala.getNome(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(300, 10, 300, 25);
        add(lblTitulo);

        criarTabela();
        carregarEquipamentosDeCSV();
        atualizarTabela();
        criarFormulario();
        criarBotoes();



        setVisible(true);
    }

    private void criarFormulario() {
        int x = 500;

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(x, 60, 120, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(x + 130, 60, 150, 25);
        add(txtNome);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(x, 100, 120, 25);
        add(lblTipo);
        cmbTipo = new JComboBox<>(new String[]{"Projetor", "Caixa de Som"});
        cmbTipo.setBounds(x + 130, 100, 150, 25);
        add(cmbTipo);
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Tipo"}, 0);
        tabelaEquipamentos = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaEquipamentos);
        scroll.setBounds(30, 60, 430, 300);
        scroll.getViewport().setBackground(new Color(220, 255, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scroll);

        tabelaEquipamentos.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaEquipamentos.getSelectedRow();
            if (linha >= 0) {
                Equipamento eq = listaEquipamentos.get(linha);
                txtNome.setText(eq.getNome());
                cmbTipo.setSelectedItem(eq.getTipo());
            }
        });
    }

    private void criarBotoes() {
        int x = 360;
        int y = 400;
        int largura = 100;
        int altura = 40;
        int gap = 20;

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(x, y, largura, altura);
        btnAdicionar.addActionListener(e -> adicionarEquipamento());
        add(btnAdicionar);

        btnRemover = new JButton("Remover");
        btnRemover.setBounds(x + (largura + gap), y, largura, altura);
        btnRemover.addActionListener(e -> removerEquipamento());
        add(btnRemover);

        JButton btnSair = new JButton("Sair");
        btnSair.setBounds(x + 2 * (largura + gap), y, largura, altura);
        btnSair.addActionListener(e -> dispose());
        add(btnSair);
    }

    private void adicionarEquipamento() {
        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preenche o nome do equipamento.");
            return;
        }

        Equipamento novo = new Equipamento(nome, tipo);
        listaEquipamentos.add(novo);
        modeloTabela.addRow(new Object[]{nome, tipo});
        txtNome.setText("");
        cmbTipo.setSelectedIndex(0);
        guardarEquipamentosEmCSV();
    }

    private void removerEquipamento() {
        int linha = tabelaEquipamentos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Seleciona um equipamento para remover.");
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this, "Tens a certeza?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            listaEquipamentos.remove(linha);
            modeloTabela.removeRow(linha);
        }
        guardarEquipamentosEmCSV();
    }

    private void carregarEquipamentosDeCSV() {
        File ficheiro = new File(FICHEIRO_EQUIPAMENTOS);
        if (!ficheiro.exists()) return;

        String nomeSalaAtual = sala.getNome();
        try (Scanner scanner = new Scanner(ficheiro)) {
            listaEquipamentos.clear();
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");
                if (partes.length == 3 && partes[2].equalsIgnoreCase(nomeSalaAtual)) {
                    String nome = partes[0];
                    String tipo = partes[1];
                    Equipamento equipamento = new Equipamento(nome, tipo);
                    listaEquipamentos.add(equipamento);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar equipamentos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarEquipamentosEmCSV() {
        try {
            File ficheiro = new File(FICHEIRO_EQUIPAMENTOS);

            try (PrintWriter writer = new PrintWriter(ficheiro)) {
                for (Equipamento eq : listaEquipamentos) {
                    writer.printf("%s,%s,%s%n",
                            eq.getNome(),
                            eq.getTipo(),
                            sala.getNome() // ou equipamento.getSala(), se for armazenado no objeto
                    );
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao guardar equipamentos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Equipamento eq : listaEquipamentos) {
            modeloTabela.addRow(new Object[]{
                    eq.getNome(),
                    eq.getTipo()
            });
        }

    }
}