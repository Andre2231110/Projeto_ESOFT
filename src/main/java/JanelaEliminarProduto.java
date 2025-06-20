
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaEliminarProduto extends JDialog {
    private JList<String> lista;
    private DefaultListModel<String> modeloLista;
    private JButton btnEliminar;
    private JButton btnCancelar;

    private Produto produtoSelecionado;

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
                    dispose();
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
}
