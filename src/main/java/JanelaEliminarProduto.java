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
            modeloLista.addElement(p.nome);
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
                        "Tens a certeza que queres eliminar o produto \"" + produtoSelecionado.nome + "\"?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    try {
                        eliminarProdutoCSV(produtoSelecionado);
                        produtos.remove(index); // também remove da lista em memória
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

                    if (!p.nome.equals(aEliminar.nome)) {
                        produtosAtualizados.add(p);
                    }
                }
            }
        }

        // Reescrever sem o produto eliminado
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Produto p : produtosAtualizados) {
                pw.println(p.nome + "," + p.categoria + "," + p.preco + "," + p.desconto + "," +
                        p.stock + "," + p.lote + "," + (p.validade != null ? p.validade : ""));
            }
        }
    }
}
