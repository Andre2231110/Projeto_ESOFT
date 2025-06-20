import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class JanelaSalas extends JFrame {

    private JPanel painelPrincipal;
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;
    private JTextField txtLayout;
    private JComboBox<String> cmbSom;
    private JCheckBox chkAcessivel;
    private JTable tabelaSalas;
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnRemover;
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
            }
        });
        criarBotoes();

        setContentPane(painelPrincipal);
        setVisible(true);
        carregarSalasDeCSV();
        atualizarTabela();
    }

    private void criarFormulario() {
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2));

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

        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Tipo", "Layout", "Som", "Acessível"}, 0);
        tabelaSalas = new JTable(modeloTabela);
        painelPrincipal.add(new JScrollPane(tabelaSalas), BorderLayout.CENTER);
    }

    private void criarBotoes() {
        JPanel painelBotoes = new JPanel();

        btnAdicionar = new JButton("Adicionar");
        btnEditar = new JButton("Editar");
        btnRemover = new JButton("Remover");
        btnSair = new JButton("Sair");



        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnSair);


        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarSala());
        btnEditar.addActionListener(e -> editarSala());
        btnRemover.addActionListener(e -> removerSala());
        btnSair.addActionListener(e -> dispose());
    }

    private void adicionarSala() {
        if (!dadosValidos()) return;

        String nome = txtNome.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String layout = txtLayout.getText().trim(); // ex: "10"
        String som = (String) cmbSom.getSelectedItem();
        boolean acessivel = chkAcessivel.isSelected();

        Sala novaSala = new Sala(nome, tipo, layout, som, acessivel);
        listaSalas.add(novaSala);

        modeloTabela.addRow(new Object[]{
                nome, tipo, layout + "x" + layout, som, acessivel ? "Sim" : "Não"
        });

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

        Sala sala = listaSalas.get(linhaSelecionada);
        sala.setNome(nome);
        sala.setTipo(tipo);
        sala.setLayout(layout);
        sala.setSom(som);
        sala.setAcessivel(acessivel);

        modeloTabela.setValueAt(nome, linhaSelecionada, 0);
        modeloTabela.setValueAt(tipo, linhaSelecionada, 1);
        modeloTabela.setValueAt(layout + "x" + layout, linhaSelecionada, 2);
        modeloTabela.setValueAt(som, linhaSelecionada, 3);
        modeloTabela.setValueAt(acessivel ? "Sim" : "Não", linhaSelecionada, 4);

        limparFormulario();

        JOptionPane.showMessageDialog(this,
                "Sala editada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        guardarSalasEmCSV();
    }


    private void removerSala() {
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Não foi selecionada nenhuma sala para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(this,
                "Tens a certeza que queres remover esta sala?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) return;
        if (linhaSelecionada == -1) return;

        listaSalas.remove(linhaSelecionada);
        modeloTabela.removeRow(linhaSelecionada);
        limparFormulario();

        JOptionPane.showMessageDialog(this,
                "Sala removida com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        guardarSalasEmCSV();
    }

    private void limparFormulario() {
        txtNome.setText("");
        cmbTipo.setSelectedIndex(0);
        txtLayout.setText("");
        cmbSom.setSelectedIndex(0);
        chkAcessivel.setSelected(false);
    }



    private void carregarSalasDeCSV() {
        File ficheiro = new File(FICHEIRO_SALAS);
        if (!ficheiro.exists()) {
            // Se o ficheiro ainda não existir, nada é carregado (primeira execução, por exemplo)
            return;
        }

        try (Scanner scanner = new Scanner(ficheiro)) {
            listaSalas.clear(); // limpar antes
            // ignorar cabeçalho

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");

                if (partes.length == 5) {
                    String nome = partes[0];
                    String tipo = partes[1];
                    String layout = partes[2].split("x")[0]; // ex: "10x10" → "10"
                    String som = partes[3];
                    boolean acessivel = partes[4].equalsIgnoreCase("Sim");

                    Sala sala = new Sala(nome, tipo, layout, som, acessivel);
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
                    writer.printf("%s,%s,%sx%s,%s,%s%n",
                            sala.getNome(),
                            sala.getTipo(),
                            sala.getLayout(), sala.getLayout(),
                            sala.getSom(),
                            sala.isAcessivel() ? "1" : "0");
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
        modeloTabela.setRowCount(0); // limpa a tabela
        for (Sala sala : listaSalas) {
            modeloTabela.addRow(new Object[]{
                    sala.getNome(),
                    sala.getTipo(),
                    sala.getLayout() + "x" + sala.getLayout(),
                    sala.getSom(),
                    sala.isAcessivel() ? "Sim" : "Não"
            });
        }
    }



    private boolean dadosValidos() {
        String nome = txtNome.getText().trim();
        String layoutTexto = txtLayout.getText().trim();

        if (nome.isEmpty() || layoutTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor preencha todos os campos obrigatórios.",
                    "Campos inválidos",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar se o layout é um número válido
        try {
            int layout = Integer.parseInt(layoutTexto);
            if (layout < 1) {
                JOptionPane.showMessageDialog(this,
                        "O layout deve ser um número inteiro positivo.",
                        "Layout inválido",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "O layout deve ser um número (ex: 10 para 10x10).",
                    "Layout inválido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}

