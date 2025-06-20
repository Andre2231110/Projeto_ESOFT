import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaStock extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ArrayList<Produto> produtos;
    private String[] colunas = {"Nome", "Stock", "Validade", "Lote"};

    public JanelaStock(ArrayList<Produto> produtos) {
        this.produtos = produtos;

        setTitle("Gestão de Stock");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta na tabela
            }
        };

        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnEditarStock = new JButton("Editar Stock");
        btnEditarStock.addActionListener(e -> editarProdutoSelecionado());

        JButton btnEliminar = new JButton("Eliminar Produto");
        btnEliminar.addActionListener(e -> eliminarProdutoSelecionado());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnEditarStock);
        painelBotoes.add(btnEliminar);
        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
        setVisible(true);
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0); // limpa a tabela

        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.nome,
                    p.stock,
                    (p.validade != null) ? p.validade : "—",
                    (p.lote != null && !p.lote.isEmpty()) ? p.lote : "—"
            });
        }
    }

    private void editarProdutoSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Seleciona um produto para editar.");
            return;
        }

        Produto produto = produtos.get(linha);
        JanelaEditarStock janela = new JanelaEditarStock(this, produto); // assume que tens esta classe
        janela.setVisible(true);
        atualizarTabela(); // atualiza a tabela após edição
    }

    private void eliminarProdutoSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Seleciona um produto para eliminar.");
            return;
        }

        Produto produto = produtos.get(linha);

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tens a certeza que queres eliminar \"" + produto.nome + "\"?\n(Isto irá apagar o stock, validade e lote.)",
                "Confirmar Soft Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            produto.stock = 0;
            produto.validade = null;
            produto.lote = "—";

            atualizarTabela(); // reflete as mudanças na UI
        }
    }
}
