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

    private final String FICHEIRO_SALAS = "src/main/java/csv/salas.csv";

    private DefaultTableModel modeloTabela;
    private ArrayList<Sala> listaSalas;

    public JanelaSalas() {
        super("Gestão de Salas");

        listaSalas = new ArrayList<>();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        painelPrincipal = new JPanel(new BorderLayout());

        criarFormulario();
        criarTabela();
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
        criarBotoes();

        setContentPane(painelPrincipal);
        setVisible(true);
        carregarSalasDeCSV();
        atualizarTabela();
    }

    private void criarFormulario() {
        JPanel painelFormulario = new JPanel(new GridLayout(8, 2));

        painelFormulario.add(new JLabel("Nome da Sala:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Tipo de Sala:"));
        cmbTipo = new JComboBox<>(new String[]{"Normal", "VIP", "Infantil", "3D", "IMAX"});
        painelFormulario.add(cmbTipo);

        painelFormulario.add(new JLabel("Layout:"));
        txtLayout = new JTextField();
        painelFormulario.add(txtLayout);

        painelFormulario.add(new JLabel("Tipo de Som:"));
        cmbSom = new JComboBox<>(new String[]{"Estéreo", "Dolby", "IMAX"});
        painelFormulario.add(cmbSom);

        painelFormulario.add(new JLabel("Acessível:"));
        chkAcessivel = new JCheckBox();
        painelFormulario.add(chkAcessivel);

        painelFormulario.add(new JLabel("Inativa:"));
        chkInativa = new JCheckBox();
        painelFormulario.add(chkInativa);

        painelFormulario.add(new JLabel("Preço de Custo (€):"));
        txtPrecoCusto = new JTextField();
        painelFormulario.add(txtPrecoCusto);

        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Tipo", "Layout", "Som", "Acessível", "Estado", "Preço Custo (€)"}, 0);
        tabelaSalas = new JTable(modeloTabela);
        painelPrincipal.add(new JScrollPane(tabelaSalas), BorderLayout.CENTER);
    }

    private void criarBotoes() {
        JPanel painelBotoes = new JPanel();

        btnAdicionar = new JButton("Adicionar");
        btnEditar = new JButton("Editar");
        btnRemover = new JButton("Remover");
        btnSair = new JButton("Sair");
        btnReservas = new JButton("Reservas");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnReservas);
        painelBotoes.add(btnSair);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarSala());
        btnEditar.addActionListener(e -> editarSala());
        btnRemover.addActionListener(e -> desativarSala());
        btnSair.addActionListener(e -> dispose());
        btnReservas.addActionListener(e -> abrirJanelaReservas());
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
        new JanelaReservas(this, salaSelecionada);
    }
}