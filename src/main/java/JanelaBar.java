import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import GestaoBar.Produto;

public class JanelaBar extends JFrame {

    private DefaultListModel<String> modeloProdutos;
    private JList<String> listaProdutos;
    private JTextArea detalhesProduto;
    private ArrayList<Produto> listaDeProdutos;

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
        scrollLista.setBackground(new Color(220, 255, 200));
        scrollLista.getViewport().setBackground(new Color(220, 255, 200));
        scrollLista.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scrollLista);

        detalhesProduto = new JTextArea("Nome:\nCategoria:\nPreço:\nDesconto:");
        detalhesProduto.setEditable(false);
        detalhesProduto.setBackground(new Color(220, 255, 200));
        detalhesProduto.setBounds(300, 60, 200, 100);
        detalhesProduto.setFont(new Font("Arial", Font.PLAIN, 14));
        add(detalhesProduto);

        JButton btnAdicionar = new JButton("+");
        btnAdicionar.setBounds(300, 180, 45, 40);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdicionar.setContentAreaFilled(false);
        add(btnAdicionar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(355, 180, 45, 40);
        btnEditar.setContentAreaFilled(false);
        add(btnEditar);
        btnEditar.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index != -1) {
                Produto produtoSelecionado = listaDeProdutos.get(index);

                JanelaEditarProduto janela = new JanelaEditarProduto(this, produtoSelecionado);
                janela.setVisible(true);

                // Atualiza a lista visual (nome pode ter mudado)
                modeloProdutos.set(index, produtoSelecionado.nome);
                detalhesProduto.setText("Nome: " + produtoSelecionado.nome +
                        "\nCategoria: " + produtoSelecionado.categoria +
                        "\nPreço: " + produtoSelecionado.preco + " €" +
                        "\nDesconto: " + produtoSelecionado.desconto + "%");
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um produto para editar.");
            }
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(410, 180, 100, 30);
        btnRemover.setBackground(Color.RED);
        btnRemover.setForeground(Color.WHITE);
        add(btnRemover);

        JButton btnStock = new JButton("Stock");
        btnStock.setBounds(410, 220, 100, 30);
        btnStock.setBackground(Color.RED);
        btnStock.setForeground(Color.WHITE);
        add(btnStock);

        JButton btnHistorico = new JButton("Histórico");
        btnHistorico.setBounds(410, 260, 100, 30);
        btnHistorico.setBackground(Color.RED);
        btnHistorico.setForeground(Color.WHITE);
        add(btnHistorico);

        listaDeProdutos = new ArrayList<>();

        adicionarProduto(new Produto("Água 50cl", "Bebida", 1.00, 0));
        adicionarProduto(new Produto("Sumo Laranja", "Sumo", 1.50, 10));
        adicionarProduto(new Produto("Chips", "Snack", 1.20, 5));

        listaProdutos.addListSelectionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index >= 0 && index < listaDeProdutos.size()) {
                Produto p = listaDeProdutos.get(index);
                detalhesProduto.setText("Nome: " + p.nome +
                        "\nCategoria: " + p.categoria +
                        "\nPreço: " + p.preco + " €" +
                        "\nDesconto: " + p.desconto + "%");
            }
        });

        btnAdicionar.addActionListener(e -> {
            JanelaAdicionarProduto dialog = new JanelaAdicionarProduto(this);
            dialog.setVisible(true);
            Produto novo = dialog.getProdutoCriado();
            if (novo != null) {
                adicionarProduto(novo);
            }
        });

        btnBack.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        setVisible(true);
    }

    private void adicionarProduto(Produto produto) {
        listaDeProdutos.add(produto);
        modeloProdutos.addElement(produto.nome);
    }
}
