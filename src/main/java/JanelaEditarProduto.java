
import GestaoBar.Produto;

import javax.swing.*;
import GestaoBar.Produto;

public class JanelaEditarProduto extends JDialog {
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtPreco;
    private JTextField txtDesconto;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Produto produtoEditado;

    public JanelaEditarProduto(JFrame parent, Produto produto) {
        super(parent, "Editar Produto", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 100, 25);
        add(lblNome);
        txtNome = new JTextField(produto.getNome());
        txtNome.setBounds(150, 30, 200, 25);
        add(txtNome);

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(30, 70, 100, 25);
        add(lblCategoria);
        txtCategoria = new JTextField(produto.getCategoria());
        txtCategoria.setBounds(150, 70, 200, 25);
        add(txtCategoria);

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(30, 110, 100, 25);
        add(lblPreco);
        txtPreco = new JTextField(String.valueOf(produto.getPreco()));
        txtPreco.setBounds(150, 110, 200, 25);
        add(txtPreco);

        JLabel lblDesconto = new JLabel("Desconto:");
        lblDesconto.setBounds(30, 150, 100, 25);
        add(lblDesconto);
        txtDesconto = new JTextField(String.valueOf(produto.getDesconto()));
        txtDesconto.setBounds(150, 150, 200, 25);
        add(txtDesconto);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(80, 200, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 200, 100, 30);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                String categoria = txtCategoria.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                int desconto = Integer.parseInt(txtDesconto.getText());

                if (nome.isEmpty() || categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
                    return;
                }

                // Atualiza os dados do produto original
                produto.setNome(nome);
                produto.setCategoria(categoria);
                produto.setPreco(preco);
                produto.setDesconto(desconto);

                produtoEditado = produto;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço ou desconto inválidos.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public Produto getProdutoEditado() {
        return produtoEditado;
    }
}
