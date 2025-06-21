import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class JanelaHistoricoVendas extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;

    private static final String FICHEIRO_VENDAS_PRODUTO = "src/main/java/csv/vendasProduto.csv";
    private static final String FICHEIRO_PRODUTOS = "src/main/java/csv/produtos.csv";

    public JanelaHistoricoVendas() {
        setTitle("Histórico de Vendas (Lucro e Datas)");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{"Produto", "Total Vendido", "Lucro Total (€)", "Última Venda"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        carregarHistorico();
        setVisible(true);
    }

    private void carregarHistorico() {
        Map<String, Integer> totalPorProduto = new HashMap<>();
        Map<String, Double> totalFaturado = new HashMap<>();
        Map<String, String> ultimaVendaPorProduto = new HashMap<>();

        // 1. Lê o ficheiro de vendas
        File file = new File(FICHEIRO_VENDAS_PRODUTO);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 3) continue;

                String nome = partes[0];
                int quantidade = Integer.parseInt(partes[1]);
                double totalVenda = Double.parseDouble(partes[2].replace(",", "."));
                String dataVenda = (partes.length >= 4) ? partes[3] : "Data desconhecida";

                totalPorProduto.put(nome, totalPorProduto.getOrDefault(nome, 0) + quantidade);
                totalFaturado.put(nome, totalFaturado.getOrDefault(nome, 0.0) + totalVenda);
                ultimaVendaPorProduto.put(nome, dataVenda); // sempre sobrescreve com a última
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas: " + e.getMessage());
            return;
        }

        // 2. Lê os preços de compra do ficheiro de produtos
        Map<String, Double> precoCompraPorProduto = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_PRODUTOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length < 4) continue;

                String nome = partes[0];
                double precoCompra = Double.parseDouble(partes[3].replace(",", "."));
                precoCompraPorProduto.put(nome, precoCompra);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
            return;
        }

        // 3. Preenche a tabela com lucro e data
        for (String nome : totalPorProduto.keySet()) {
            int quantidade = totalPorProduto.get(nome);
            double faturado = totalFaturado.get(nome);
            double precoCompra = precoCompraPorProduto.getOrDefault(nome, 0.0);
            double custoTotal = precoCompra * quantidade;
            double lucro = faturado - custoTotal;
            String ultimaData = ultimaVendaPorProduto.getOrDefault(nome, "Data desconhecida");

            modelo.addRow(new Object[]{
                    nome,
                    quantidade,
                    String.format("%.2f", lucro),
                    ultimaData
            });
        }
    }
}
