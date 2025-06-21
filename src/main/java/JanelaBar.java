// (as importações ficam iguais)
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaBar extends JFrame {

    private DefaultListModel<Produto> modeloProdutos;
    private JList<Produto> listaProdutos;
    private JTextArea detalhesProduto;
    private ArrayList<Produto> listaDeProdutos;
    private static final String CSV_FILE = "src/main/java/csv/produtos.csv";

    public JanelaBar(String nomeUser) {
        setTitle("Espaço Bar");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 255, 240));

        JButton btnBack = new JButton("< Back");
        btnBack.setBounds(10, 10, 80, 25);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Serif", Font.PLAIN, 14));
        add(btnBack);

        JLabel lblTitulo = new JLabel("Espaço Bar", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(300, 10, 200, 25);
        add(lblTitulo);

        JLabel lblUser = new JLabel(nomeUser);
        lblUser.setBounds(700, 10, 80, 25);
        add(lblUser);

        modeloProdutos = new DefaultListModel<>();
        listaProdutos = new JList<>(modeloProdutos);
        JScrollPane scrollLista = new JScrollPane(listaProdutos);
        scrollLista.setBounds(30, 60, 200, 360);
        scrollLista.getViewport().setBackground(new Color(220, 255, 200));
        scrollLista.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scrollLista);

        detalhesProduto = new JTextArea("Nome:\nCategoria:\nPreço:\nDesconto:");
        detalhesProduto.setEditable(false);
        detalhesProduto.setBackground(new Color(220, 255, 200));
        detalhesProduto.setBounds(300, 60, 230, 120);
        detalhesProduto.setFont(new Font("Arial", Font.PLAIN, 14));
        add(detalhesProduto);

        JButton btnAdicionar = new JButton("+");
        btnAdicionar.setBounds(300, 200, 45, 40);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdicionar.setContentAreaFilled(false);
        add(btnAdicionar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(355, 200, 70, 40);
        btnEditar.setContentAreaFilled(false);
        add(btnEditar);
        btnEditar.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index != -1) {
                Produto produtoSelecionado = modeloProdutos.get(index);
                JanelaEditarProduto janela = new JanelaEditarProduto(this, produtoSelecionado);
                janela.setVisible(true);
                modeloProdutos.set(index, produtoSelecionado);
                atualizarDetalhes(produtoSelecionado);
                guardarProdutosCSV();
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um produto para editar.");
            }
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(430, 200, 100, 30);
        btnRemover.setBackground(Color.RED);
        btnRemover.setForeground(Color.WHITE);
        add(btnRemover);
        btnRemover.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index != -1) {
                Produto produtoSelecionado = modeloProdutos.get(index);
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Tens a certeza que queres eliminar o produto \"" + produtoSelecionado.getNome() + "\"?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    modeloProdutos.remove(index);
                    listaDeProdutos.remove(produtoSelecionado);
                    guardarProdutosCSV();
                    detalhesProduto.setText("Nome:\nCategoria:\nPreço:\nDesconto:");
                    JOptionPane.showMessageDialog(this, "Produto eliminado com sucesso.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um produto da lista para remover.");
            }
        });

        JButton btnStock = new JButton("Stock");
        btnStock.setBounds(430, 240, 100, 30);
        btnStock.setBackground(Color.RED);
        btnStock.setForeground(Color.WHITE);
        add(btnStock);
        btnStock.addActionListener(e -> new JanelaStock(new ArrayList<>(listaDeProdutos)));

        JButton btnHistorico = new JButton("Histórico");
        btnHistorico.setBounds(430, 280, 100, 30);
        btnHistorico.setBackground(Color.RED);
        btnHistorico.setForeground(Color.WHITE);
        add(btnHistorico);
        btnHistorico.addActionListener(e -> new JanelaHistoricoVendas());

        listaDeProdutos = new ArrayList<>();
        listaProdutos.addListSelectionListener(e -> {
            Produto p = listaProdutos.getSelectedValue();
            if (p != null) {
                atualizarDetalhes(p);
            }
        });

        btnAdicionar.addActionListener(e -> {
            JanelaAdicionarProduto dialog = new JanelaAdicionarProduto(this);
            dialog.setVisible(true);
            Produto novo = dialog.getProdutoCriado();
            if (novo != null) {
                adicionarProduto(novo);
                guardarProdutosCSV();
            }
        });

        btnBack.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        carregarProdutosCSV();
        setVisible(true);
    }

    private void adicionarProduto(Produto produto) {
        listaDeProdutos.add(produto);
        modeloProdutos.addElement(produto);
    }

    private void atualizarDetalhes(Produto p) {
        detalhesProduto.setText("Nome: " + p.getNome() +
                "\nCategoria: " + p.getCategoria() +
                "\nPreço: " + p.getPreco() + " €" +
                "\nDesconto: " + p.getDesconto() + "%");
    }

    private void carregarProdutosCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length >= 8) {
                    Produto p = new Produto(
                            partes[0],
                            partes[1],
                            Double.parseDouble(partes[2]),
                            Double.parseDouble(partes[3]),
                            Integer.parseInt(partes[4])
                    );
                    p.setStock(Integer.parseInt(partes[5]));
                    p.setLote(partes[6]);
                    p.setValidade(partes[7].isEmpty() ? null : LocalDate.parse(partes[7]));
                    adicionarProduto(p);
                }
            }
        } catch (FileNotFoundException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarProdutosCSV() {
        try (PrintWriter pw = new PrintWriter(CSV_FILE)) {
            for (int i = 0; i < modeloProdutos.size(); i++) {
                Produto p = modeloProdutos.get(i);
                pw.println(p.getNome() + "," + p.getCategoria() + "," + p.getPreco() + "," +
                        p.getPrecoCompra() + "," + p.getDesconto() + "," + p.getStock() + "," +
                        p.getLote() + "," + (p.getValidade() != null ? p.getValidade() : ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
