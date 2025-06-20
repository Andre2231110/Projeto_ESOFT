import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import GestaoBar.Produto;

public class JanelaAdicionarProduto extends JDialog {
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtPreco;
    private JTextField txtDesconto;
    private JButton btnAdicionar;
    private JButton btnCancelar;

    private Produto produtoCriado;

    private static final String CSV_FILE = "produtos.csv";

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

        JLabel lblPreco = new JLabel("Preço (€):");
        lblPreco.setBounds(30, 110, 100, 25);
        add(lblPreco);
        txtPreco = new JTextField();
        txtPreco.setBounds(150, 110, 200, 25);
        add(txtPreco);

        JLabel lblDesconto = new JLabel("Desconto (%):");
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
                String nome = txtNome.getText().trim();
                String categoria = txtCategoria.getText().trim();
                double preco = Double.parseDouble(txtPreco.getText().trim());
                int desconto = Integer.parseInt(txtDesconto.getText().trim());

                // Criação do produto com os campos essenciais
                produtoCriado = new Produto(nome, categoria, preco, desconto);

                // Guardar no CSV
                guardarProdutoCSV(produtoCriado);

                // Fechar a janela
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Insere valores válidos para preço e desconto.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao guardar o produto: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public Produto getProdutoCriado() {
        return produtoCriado;
    }

    private void guardarProdutoCSV(Produto p) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            pw.println(p.nome + "," + p.categoria + "," + p.preco + "," + p.desconto + "," +
                    p.stock + "," + p.lote + "," + (p.validade != null ? p.validade : ""));
        }
    }
}
