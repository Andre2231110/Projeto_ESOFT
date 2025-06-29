import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class JanelaFuncionario extends JFrame {
    private DefaultListModel<Funcionario> modeloFuncionarios;
    private JList<Funcionario> listaFuncionarios;
    private JTextArea detalhesFuncionario;
    private static final String CSV_FILE = "src/main/java/csv/funcionarios.csv";
    private static final String VENDAS_FILE = "src/main/java/csv/vendasUtilizador.csv";

    private String nomeUser;

    public JanelaFuncionario(String nomeUser) {
        this.nomeUser = nomeUser;

        setTitle("Funcionário");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(235, 255, 235));

        JButton btnBack = new JButton("< Back");
        btnBack.setBounds(10, 10, 80, 25);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFont(new Font("Serif", Font.PLAIN, 14));
        add(btnBack);

        btnBack.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        JLabel lblTitulo = new JLabel("Funcionário", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(300, 10, 200, 25);
        add(lblTitulo);

        JLabel lblUser = new JLabel(nomeUser);
        lblUser.setBounds(700, 10, 80, 25);
        add(lblUser);

        modeloFuncionarios = new DefaultListModel<>();
        listaFuncionarios = new JList<>(modeloFuncionarios);
        listaFuncionarios.setBackground(new Color(210, 250, 210));
        JScrollPane scrollLista = new JScrollPane(listaFuncionarios);
        scrollLista.setBounds(40, 60, 200, 350);
        add(scrollLista);

        detalhesFuncionario = new JTextArea();
        detalhesFuncionario.setEditable(false);
        detalhesFuncionario.setBackground(new Color(210, 250, 210));
        detalhesFuncionario.setFont(new Font("Arial", Font.PLAIN, 14));
        detalhesFuncionario.setBounds(300, 60, 300, 120);
        add(detalhesFuncionario);

        JButton btnAdd = new JButton("+");
        btnAdd.setBounds(300, 200, 45, 30);
        add(btnAdd);

        btnAdd.addActionListener(e -> {
            JanelaAdicionarFunc janela = new JanelaAdicionarFunc(this);
            janela.setVisible(true);
            modeloFuncionarios.clear();
            carregarFuncionariosCSV();
        });

        JButton btnEditar = new JButton("edit");
        btnEditar.setBounds(360, 200, 45, 30);
        add(btnEditar);

        btnEditar.addActionListener(e -> {
            Funcionario f = listaFuncionarios.getSelectedValue();
            if (f != null) {
                JanelaEditarFunc janela = new JanelaEditarFunc(this, f);
                janela.setVisible(true);
                modeloFuncionarios.clear();
                carregarFuncionariosCSV();
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um funcionário para editar.");
            }
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(420, 200, 100, 30);
        btnRemover.setBackground(Color.RED);
        btnRemover.setForeground(Color.WHITE);
        add(btnRemover);

        btnRemover.addActionListener(e -> {
            Funcionario f = listaFuncionarios.getSelectedValue();
            if (f != null) {
                JanelaEliminarFunc janela = new JanelaEliminarFunc(this, f);
                janela.setVisible(true);
                modeloFuncionarios.clear();
                carregarFuncionariosCSV();
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um funcionário para eliminar.");
            }
        });

        listaFuncionarios.addListSelectionListener(e -> {
            Funcionario f = listaFuncionarios.getSelectedValue();
            if (f != null) {
                detalhesFuncionario.setText(
                        "Lucro: " + f.lucroTotal + "\n" +
                                "Função: " + f.func + "\n" +
                                "Contacto: " + f.contacto + "\n" +
                                "Turno: " + f.turno
                );
            }
        });

        carregarFuncionariosCSV();
        atualizarLucrosDosFuncionarios();
        guardarFuncionariosAtualizadosNoCSV(); // guardar após atualização
        setVisible(true);
    }

    private void carregarFuncionariosCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length == 5) {
                    try {
                        Funcionario f = new Funcionario(
                                partes[0],
                                partes[1],
                                partes[2],
                                Double.parseDouble(partes[3].trim()),
                                partes[4]
                        );
                        modeloFuncionarios.addElement(f);
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter lucro: " + partes[3]);
                    }
                } else {
                    System.err.println("Linha inválida no CSV: " + linha);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler ficheiro de funcionários.");
        }
    }

    private void atualizarLucrosDosFuncionarios() {
        File vendasFile = new File(VENDAS_FILE);
        if (!vendasFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(vendasFile))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Corrige separador para ";"
                String[] partes = linha.split(";", -1);
                if (partes.length == 2) {
                    String nomeVenda = partes[0].trim();
                    double valor = Double.parseDouble(partes[1].trim());

                    for (int i = 0; i < modeloFuncionarios.size(); i++) {
                        Funcionario f = modeloFuncionarios.getElementAt(i);
                        if (f.nome.equalsIgnoreCase(nomeVenda)) {
                            f.lucroTotal += valor;
                            break;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar lucros dos funcionários.");
        }
    }


    private void guardarFuncionariosAtualizadosNoCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (int i = 0; i < modeloFuncionarios.size(); i++) {
                Funcionario f = modeloFuncionarios.getElementAt(i);
                pw.println(f.nome + "," + f.contacto + "," + f.turno + "," + f.lucroTotal + "," + f.func);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar os dados atualizados dos funcionários.");
        }
    }
}
