import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class JanelaSalas extends JFrame {

    private JPanel painelPrincipal;
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;
    private JTextField txtLayout;
    private JComboBox<String> cmbSom;
    private JCheckBox chkAcessivel;
    private JCheckBox chkInativa;
    private JTextField txtPrecoCusto;
    private JTable tabelaSalas;
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnReservas;
    private JButton btnSair;
    private JButton btnBack;
    private JButton btnEquipamentos;

    private final String FICHEIRO_SALAS = "src/main/java/csv/salas.csv";

    private DefaultTableModel modeloTabela;
    private ArrayList<Sala> listaSalas;

    public JanelaSalas() {
        super("Gestão de Salas");

        listaSalas = new ArrayList<>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 255, 240));

        btnBack = new JButton("< Back");
        btnBack.setBounds(10, 10, 80, 25);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Serif", Font.PLAIN, 14));
        btnBack.addActionListener(e -> dispose());
        add(btnBack);

        JLabel lblTitulo = new JLabel("Gestão de Salas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(300, 10, 300, 25);
        add(lblTitulo);


        criarTabela();
        criarFormulario();
        criarBotoes();

        carregarSalasDeCSV();
        atualizarTabela();

        setVisible(true);
    }

    private void criarFormulario() {
        int x = 500;

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(x, 60, 100, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(x + 120, 60, 150, 25);
        add(txtNome);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(x, 100, 100, 25);
        add(lblTipo);

        cmbTipo = new JComboBox<>(new String[]{"Normal", "VIP", "Infantil", "3D", "IMAX"});
        cmbTipo.setBounds(x + 120, 100, 150, 25);
        add(cmbTipo);

        JLabel lblLayout = new JLabel("Layout:");
        lblLayout.setBounds(x, 140, 100, 25);
        add(lblLayout);

        txtLayout = new JTextField();
        txtLayout.setBounds(x + 120, 140, 150, 25);
        add(txtLayout);

        JLabel lblSom = new JLabel("Som:");
        lblSom.setBounds(x, 180, 100, 25);
        add(lblSom);

        cmbSom = new JComboBox<>(new String[]{"Estéreo", "Dolby", "IMAX"});
        cmbSom.setBounds(x + 120, 180, 150, 25);
        add(cmbSom);

        JLabel lblAcessivel = new JLabel("Acessível:");
        lblAcessivel.setBounds(x, 220, 100, 25);
        add(lblAcessivel);

        chkAcessivel = new JCheckBox();
        chkAcessivel.setBounds(x + 120, 220, 30, 25);
        add(chkAcessivel);

        JLabel lblInativa = new JLabel("Inativa:");
        lblInativa.setBounds(x, 260, 100, 25);
        add(lblInativa);

        chkInativa = new JCheckBox();
        chkInativa.setBounds(x + 120, 260, 300, 25);
        add(chkInativa);

        JLabel lblPreco = new JLabel("Preço Custo:");
        lblPreco.setBounds(x, 300, 100, 25);
        add(lblPreco);

        txtPrecoCusto = new JTextField();
        txtPrecoCusto.setBounds(x + 120, 300, 150, 25);
        add(txtPrecoCusto);
    }


    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Tipo", "Layout", "Som", "Acessível", "Inativa", "Preço"}, 0);
        tabelaSalas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaSalas);
        scroll.setBounds(30, 60, 430, 300);
        scroll.getViewport().setBackground(new Color(220, 255, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scroll);

        tabelaSalas.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaSalas.getSelectedRow();
            if (linha >= 0) {
                Sala salaSelecionada = listaSalas.get(linha);
                txtNome.setText(salaSelecionada.getNome());
                cmbTipo.setSelectedItem(salaSelecionada.getTipo());
                txtLayout.setText(salaSelecionada.getLayout());
                cmbSom.setSelectedItem(salaSelecionada.getSom());
                chkAcessivel.setSelected(salaSelecionada.isAcessivel());
                chkInativa.setSelected(!salaSelecionada.isAtiva());
                txtPrecoCusto.setText(String.valueOf(salaSelecionada.getPrecoCusto()));
            }
        });
    }

    private void criarBotoes() {

        int x = 360;
        int y = 380;
        int largura = 80;
        int altura = 40;
        int gap = 20;
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(x, y, largura, altura);
        btnAdicionar.addActionListener(e -> adicionarSala());

        add(btnAdicionar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(x + (largura + gap) * 1, y, largura, altura);

        btnEditar.addActionListener(e -> editarSala());

        add(btnEditar);

        btnRemover = new JButton("Remover");
        btnRemover.setBounds(x + (largura + gap) * 2, y, largura, altura);
        btnRemover.addActionListener(e -> desativarSala());
        add(btnRemover);

        btnReservas = new JButton("Reservas");
        btnReservas.setBounds(x + (largura + gap) * 3, y, largura, altura);
        btnReservas.addActionListener(e -> abrirJanelaReservas());
        add(btnReservas);

        btnSair = new JButton("Sair");
        btnSair.setBounds(x + (largura + gap) * 4, y, largura, altura);
        btnSair.addActionListener(e -> dispose());
        add(btnSair);

        btnEquipamentos = new JButton("Equipamentos");
        btnEquipamentos.setBounds(x, y + 60, largura, altura);
        btnEquipamentos.addActionListener(e -> abrirJanelaEquipamentos());
        add(btnEquipamentos);
    }

    private void adicionarSala() {
        if (!dadosValidos()) return;

        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String layout = txtLayout.getText().trim();
        String som = (String) cmbSom.getSelectedItem();
        boolean acessivel = chkAcessivel.isSelected();
        boolean ativa = !chkInativa.isSelected();
        double precoCusto = Double.parseDouble(txtPrecoCusto.getText().trim());

        Sala novaSala = new Sala(nome, tipo, layout, som, acessivel, ativa, precoCusto);
        listaSalas.add(novaSala);

        atualizarTabela();
        limparFormulario();

        JOptionPane.showMessageDialog(this,
                "Sala adicionada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        guardarSalasEmCSV();
    }

    private void editarSala() {
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleciona uma sala para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dadosValidos()) return;

        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String layout = txtLayout.getText().trim();
        String som = (String) cmbSom.getSelectedItem();
        boolean acessivel = chkAcessivel.isSelected();
        boolean ativa = !chkInativa.isSelected();
        double precoCusto = Double.parseDouble(txtPrecoCusto.getText().trim());

        Sala sala = listaSalas.get(linhaSelecionada);
        sala.setNome(nome);
        sala.setTipo(tipo);
        sala.setLayout(layout);
        sala.setSom(som);
        sala.setAcessivel(acessivel);
        sala.setAtiva(ativa);
        sala.setPrecoCusto(precoCusto);

        atualizarTabela();
        limparFormulario();

        JOptionPane.showMessageDialog(this,
                "Sala editada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        guardarSalasEmCSV();
    }

    private void desativarSala() {
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Não foi selecionada nenhuma sala.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sala sala = listaSalas.get(linhaSelecionada);
        sala.setAtiva(false);
        atualizarTabela();
        guardarSalasEmCSV();

        JOptionPane.showMessageDialog(this,
                "Sala marcada como inativa com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void limparFormulario() {
        txtNome.setText("");
        cmbTipo.setSelectedIndex(0);
        txtLayout.setText("");
        cmbSom.setSelectedIndex(0);
        chkAcessivel.setSelected(false);
        chkInativa.setSelected(false);
        txtPrecoCusto.setText("");
    }

    private void carregarSalasDeCSV() {
        File ficheiro = new File(FICHEIRO_SALAS);
        if (!ficheiro.exists()) return;

        try (Scanner scanner = new Scanner(ficheiro)) {
            listaSalas.clear();
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");

                if (partes.length == 7) {
                    String nome = partes[0];
                    String tipo = partes[1];
                    String layout = partes[2].split("x")[0];
                    String som = partes[3];
                    boolean acessivel = partes[4].equals("1");
                    boolean ativa = partes[5].equals("1");
                    double preco = Double.parseDouble(partes[6]);

                    Sala sala = new Sala(nome, tipo, layout, som, acessivel, ativa, preco);
                    listaSalas.add(sala);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar CSV: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarSalasEmCSV() {
        try {
            File ficheiro = new File(FICHEIRO_SALAS);
            try (PrintWriter writer = new PrintWriter(ficheiro)) {
                for (Sala sala : listaSalas) {
                    writer.printf(Locale.US, "%s,%s,%sx%s,%s,%s,%s,%.2f%n",
                            sala.getNome(),
                            sala.getTipo(),
                            sala.getLayout(), sala.getLayout(),
                            sala.getSom(),
                            sala.isAcessivel() ? "1" : "0",
                            sala.isAtiva() ? "1" : "0",
                            sala.getPrecoCusto());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao guardar CSV: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Sala sala : listaSalas) {
            modeloTabela.addRow(new Object[]{
                    sala.getNome(),
                    sala.getTipo(),
                    sala.getLayout() + "x" + sala.getLayout(),
                    sala.getSom(),
                    sala.isAcessivel() ? "Sim" : "Não",
                    sala.isAtiva() ? "Ativa" : "Inativa",
                    String.format("%.2f", sala.getPrecoCusto())
            });
        }
    }

    private boolean dadosValidos() {
        String nome = txtNome.getText().trim();
        String layoutTexto = txtLayout.getText().trim();
        String precoTexto = txtPrecoCusto.getText().trim();

        if (nome.isEmpty() || layoutTexto.isEmpty() || precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor preencha todos os campos obrigatórios.",
                    "Campos inválidos",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int layout = Integer.parseInt(layoutTexto);
            if (layout < 1) {
                JOptionPane.showMessageDialog(this,
                        "O layout deve ser um número inteiro positivo.",
                        "Layout inválido",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double preco = Double.parseDouble(precoTexto.replace(",", "."));
            if (preco < 0) {
                JOptionPane.showMessageDialog(this,
                        "O preço de custo deve ser um número positivo.",
                        "Preço inválido",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "O layout e o preço devem ser números válidos.",
                    "Campos inválidos",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }




    private void abrirJanelaReservas() {
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma sala selecionada.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sala salaSelecionada = listaSalas.get(linhaSelecionada);

        if (!salaSelecionada.isAtiva()) {
            JOptionPane.showMessageDialog(this,
                    "Esta sala está inativa e não pode ter reservas.",
                    "Sala Inativa",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        new JanelaReservas(this, salaSelecionada);
    }
    private void abrirJanelaEquipamentos() {
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Nenhuma sala selecionada.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sala salaSelecionada = listaSalas.get(linhaSelecionada);


        new JanelaEquipamentos(this, salaSelecionada);
    }
}
