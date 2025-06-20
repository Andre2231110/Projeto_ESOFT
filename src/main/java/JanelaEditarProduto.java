import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaEditarProduto extends JDialog {
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtPreco;
    private JTextField txtDesconto;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Produto produtoEditado;
    private static final String CSV_FILE = "produtos.csv";

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
                double desconto = Double.parseDouble(txtDesconto.getText()); // assumindo double

                if (nome.isEmpty() || categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preenche todos os campos.");
                    return;
                }

                // Atualiza os dados no objeto original
                produto.setNome(nome);
                produto.setCategoria(categoria);
                produto.setPreco(preco);
                produto.setDesconto((int) desconto);

                // Atualiza o ficheiro CSV
                atualizarProdutoCSV(produto);

                produtoEditado = produto;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço ou desconto inválidos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao guardar no ficheiro.");
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public Produto getProdutoEditado() {
        return produtoEditado;
    }

    private void atualizarProdutoCSV(Produto produtoEditado) throws IOException {
        File ficheiro = new File(CSV_FILE);
        ArrayList<Produto> produtos = new ArrayList<>();

        // Ler todos os produtos do CSV
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length >= 7) {
                    Produto p = new Produto(
                            partes[0],
                            partes[1],
                            Double.parseDouble(partes[2]),
                            (int) Double.parseDouble(partes[3])
                    );
                    p.stock = Integer.parseInt(partes[4]);
                    p.lote = partes[5];
                    p.validade = partes[6].isEmpty() ? null : LocalDate.parse(partes[6]);

                    // Substituir se for o produto que foi editado
                    if (p.getNome().equals(produtoEditado.getNome())) {
                        produtos.add(produtoEditado);
                    } else {
                        produtos.add(p);
                    }
                }
            }
        }

        // Reescrever o ficheiro com os produtos atualizados
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Produto p : produtos) {
                pw.println(p.nome + "," + p.categoria + "," + p.preco + "," + p.desconto + "," +
                        p.stock + "," + p.lote + "," + (p.validade != null ? p.validade : ""));
            }
        }
    }
}
