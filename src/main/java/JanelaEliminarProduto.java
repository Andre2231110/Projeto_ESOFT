import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaEliminarProduto extends JDialog {
    private JList<String> lista;
    private DefaultListModel<String> modeloLista;
    private JButton btnEliminar;
    private JButton btnCancelar;

    private Produto produtoSelecionado;
    private static final String CSV_FILE = "produtos.csv";

    public JanelaEliminarProduto(JFrame parent, ArrayList<Produto> produtos) {
        super(parent, "Eliminar Produto", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(null);

        modeloLista = new DefaultListModel<>();
        for (Produto p : produtos) {
            modeloLista.addElement(p.getNome());
        }

        lista = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBounds(30, 30, 320, 150);
        add(scroll);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(80, 200, 100, 30);
        add(btnEliminar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 200, 100, 30);
        add(btnCancelar);

        btnEliminar.addActionListener(e -> {
            int index = lista.getSelectedIndex();
            if (index != -1) {
                produtoSelecionado = produtos.get(index);
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Tens a certeza que queres eliminar o produto \"" + produtoSelecionado.getNome() + "\"?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    try {
                        eliminarProdutoCSV(produtoSelecionado);
                        produtos.remove(index); // remove da lista em memória
                        modeloLista.remove(index); // remove da UI
                        JOptionPane.showMessageDialog(this, "Produto eliminado com sucesso.");
                        produtoSelecionado = null;
                        dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao eliminar do ficheiro.");
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleciona um produto para eliminar.");
            }
        });

        btnCancelar.addActionListener(e -> {
            produtoSelecionado = null;
            dispose();
        });
    }

    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }

    private void eliminarProdutoCSV(Produto aEliminar) throws IOException {
        File ficheiro = new File(CSV_FILE);
        ArrayList<Produto> produtosAtualizados = new ArrayList<>();

        // Ler os produtos existentes
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length >= 8) {
                    Produto p = new Produto(
                            partes[0], // nome
                            partes[1], // categoria
                            Double.parseDouble(partes[2]), // preco
                            Double.parseDouble(partes[3]), // precoCompra
                            Integer.parseInt(partes[4])    // desconto
                    );
                    p.setStock(Integer.parseInt(partes[5]));
                    p.setLote(partes[6]);
                    p.setValidade(partes[7].isEmpty() ? null : LocalDate.parse(partes[7]));

                    // Só adiciona se não for o produto a eliminar
                    if (!p.getNome().equals(aEliminar.getNome())) {
                        produtosAtualizados.add(p);
                    }
                }
            }
        }

        // Reescrever sem o produto eliminado
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Produto p : produtosAtualizados) {
                pw.println(p.getNome() + "," + p.getCategoria() + "," + p.getPreco() + "," +
                        p.getPrecoCompra() + "," + p.getDesconto() + "," + p.getStock() + "," +
                        p.getLote() + "," + (p.getValidade() != null ? p.getValidade() : ""));
            }
        }
    }
}
