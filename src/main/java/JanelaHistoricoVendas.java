import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class JanelaHistoricoVendas extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;

    private static final String FICHEIRO_VENDAS_PRODUTO = "src/main/java/csv/vendasProduto.csv";

    public JanelaHistoricoVendas() {
        setTitle("Histórico de Vendas de Produtos");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{"Produto", "Total Vendido", "Última Venda"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        carregarHistorico();
        setVisible(true);
    }

    private void carregarHistorico() {
        Map<String, Integer> totalPorProduto = new HashMap<>();
        Map<String, String> ultimaVendaPorProduto = new HashMap<>();

        File file = new File(FICHEIRO_VENDAS_PRODUTO);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 4) continue;

                String nome = partes[0];
                int quantidade = Integer.parseInt(partes[1]);
                String dataVenda = partes[3];

                totalPorProduto.put(nome, totalPorProduto.getOrDefault(nome, 0) + quantidade);
                ultimaVendaPorProduto.put(nome, dataVenda);
            }

            for (String nome : totalPorProduto.keySet()) {
                modelo.addRow(new Object[]{
                        nome,
                        totalPorProduto.get(nome),
                        ultimaVendaPorProduto.get(nome)
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + e.getMessage());
        }
    }
}
