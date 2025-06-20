import GestaoBar.Produto;

import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class JanelaAdicionarProduto extends JDialog {
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtPreco;
    private JTextField txtDesconto;
    private JButton btnAdicionar;
    private JButton btnCancelar;

    private Produto produtoCriado;

    public JanelaAdicionarProduto(JFrame parent) {
        super(parent, "Adicionar Produto", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(150, 30, 200, 25);
        add(txtNome);

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(30, 70, 100, 25);
        add(lblCategoria);
        txtCategoria = new JTextField();
        txtCategoria.setBounds(150, 70, 200, 25);
        add(txtCategoria);

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(30, 110, 100, 25);
        add(lblPreco);
        txtPreco = new JTextField();
        txtPreco.setBounds(150, 110, 200, 25);
        add(txtPreco);

        JLabel lblDesconto = new JLabel("Desconto:");
        lblDesconto.setBounds(30, 150, 100, 25);
        add(lblDesconto);
        txtDesconto = new JTextField();
        txtDesconto.setBounds(150, 150, 200, 25);
        add(txtDesconto);

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(80, 200, 100, 30);
        add(btnAdicionar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 200, 100, 30);
        add(btnCancelar);

        btnAdicionar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                String categoria = txtCategoria.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                int desconto = Integer.parseInt(txtDesconto.getText());

                if (nome.isEmpty() || categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
                    return;
                }

                produtoCriado = new Produto(nome, categoria, preco, desconto);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço ou desconto inválidos.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public Produto getProdutoCriado() {
        return produtoCriado;
    }
}
