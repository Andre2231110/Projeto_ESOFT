import GestaoBar.Produto;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class JanelaVendasBar extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblFilmes;
    private JButton backButton;
    private JPanel painelPacotes;
    private JPanel painelProdutos;
    private JPanel painelResumo;
    private JTextArea listaResumo;
    private JLabel lblTotal;
    private JButton confirmarButton;

    private Filme filme;
    private Sessao sessao;
    private List<String> lugaresSelecionados;
    private String nomeUser;

    private Map<String, ItemCarrinho> carrinho = new LinkedHashMap<>();
    private static final String FICHEIRO_PRODUTOS = "src/main/java/csv/produtos.csv";

    private static final String FICHEIRO_VENDAS_PRODUTO = "src/main/java/csv/vendasProduto.csv";
    private static final String FICHEIRO_VENDAS_BILHETE = "src/main/java/csv/vendasBilhete.csv";
    private static final String FICHEIRO_VENDAS_UTILIZADOR = "src/main/java/csv/vendasUtilizador.csv";


    public JanelaVendasBar(Filme filme, Sessao sessao, List<String> lugaresSelecionados, String nomeUser) {
        this.filme = filme;
        this.sessao = sessao;
        this.lugaresSelecionados = lugaresSelecionados;
        this.nomeUser = nomeUser;

        setTitle("Venda de Bilhetes - Bar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setContentPane(contentPane);
        painelPacotes.setLayout(new GridLayout(0, 1, 5, 5));
        painelPacotes.setPreferredSize(new Dimension(400, 500));

        carregarProdutos();

        double precoBilhete = 5.00;
        String nomeBilhete = "Bilhete";
        int quantidade = lugaresSelecionados.size();

        carrinho.putIfAbsent(nomeBilhete, new ItemCarrinho(precoBilhete));
        carrinho.get(nomeBilhete).quantidade += (quantidade - 1);
        atualizarResumo();

        backButton.addActionListener(e -> {
            List<Filme> filmes = JanelaFilmes.chamarFilmesCSV();
            List<Sala> salas = JanelaVendaSessoes.carregarSalasCSV();
            List<Sessao> sessoes = JanelaVendaSessoes.carregarSessoesCSV(filmes, salas);
            new JanelaVendaSessoes(filme, nomeUser, sessoes);
            dispose();
        });



        confirmarButton.addActionListener(e -> {
            if (carrinho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não adicionaste nenhum produto.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Compra confirmada!\n\nFilme: " + filme.getTitulo() +
                            "\nSessão: " + sessao.getHoraInicio() + " → " + sessao.getHoraFim() +
                            "\nLugares: " + String.join(", ", lugaresSelecionados) +
                            "\n\nTotal: " + String.format("%.2f €", calcularTotal())
            );

            guardarVendaProdutos();
            guardarVendaBilhetes();
            guardarVendaUtilizador();

            carrinho.clear();
            atualizarResumo();

            new JanelaPrincipal(nomeUser);
            dispose();
        });

        setVisible(true);
    }

    private void carregarProdutos() {
        File file = new File(FICHEIRO_PRODUTOS);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Ficheiro de produtos não encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length < 3) continue;

                String nome = partes[0];
                String tipo = partes[1]; // ignorado
                double preco = Double.parseDouble(partes[2]);

                JButton btn = new JButton(nome + " — " + preco + " €");
                btn.setBackground(new Color(204, 255, 204));

                btn.addActionListener(e -> {
                    if (!carrinho.containsKey(nome)) {
                        carrinho.put(nome, new ItemCarrinho(preco));
                    } else {
                        carrinho.get(nome).quantidade++;
                    }
                    atualizarResumo();
                });


                painelPacotes.add(btn);
            }

        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }

        System.out.println("Produtos carregados: " + painelPacotes.getComponentCount());
    }

    private void atualizarResumo() {
        painelResumo.removeAll();
        painelResumo.setLayout(new BoxLayout(painelResumo, BoxLayout.Y_AXIS));

        double total = 0;

        for (Map.Entry<String, ItemCarrinho> entry : carrinho.entrySet()) {
            String nome = entry.getKey();
            ItemCarrinho item = entry.getValue();
            double subtotal = item.getTotal();
            total += subtotal;

            JPanel linha = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel(nome + " (" + item.quantidade + ") → " + String.format("%.2f €", subtotal));

            linha.add(lbl, BorderLayout.CENTER);

            if (!nome.equals("Bilhete")) {
                JButton remover = new JButton("Remover");
                remover.setPreferredSize(new Dimension(90, 30));
                remover.setForeground(Color.WHITE);
                remover.setBackground(Color.RED);

                remover.addActionListener(e -> {
                    item.quantidade--;
                    if (item.quantidade <= 0) carrinho.remove(nome);
                    atualizarResumo();
                });

                linha.add(remover, BorderLayout.EAST);
            }



            linha.add(lbl, BorderLayout.CENTER);
            painelResumo.add(linha);
            painelResumo.add(confirmarButton);
        }

        lblTotal.setText("Total: " + String.format("%.2f €", total));
        painelResumo.revalidate();
        painelResumo.repaint();
    }

    private double calcularTotal() {
        double total = 0;
        for (ItemCarrinho item : carrinho.values()) {
            total += item.getTotal();
        }
        return total;
    }

    private static class ItemCarrinho {
        int quantidade;
        double precoUnitario;

        ItemCarrinho(double precoUnitario) {
            this.quantidade = 1;
            this.precoUnitario = precoUnitario;
        }

        double getTotal() {
            return quantidade * precoUnitario;
        }
    }
    private void guardarVendaProdutos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_VENDAS_PRODUTO, true))) {
            for (Map.Entry<String, ItemCarrinho> entry : carrinho.entrySet()) {
                String nome = entry.getKey();
                ItemCarrinho item = entry.getValue();
                if (!nome.equals("Bilhete")) {
                    pw.println(nome + ";" + item.quantidade + ";" + String.format("%.2f", item.getTotal()).replace(",", "."));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar vendas de produtos: " + e.getMessage());
        }
    }

    private void guardarVendaBilhetes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_VENDAS_BILHETE, true))) {
            int qnt = lugaresSelecionados.size();
            double precoBilhete = 5.00;
            double total = qnt * precoBilhete;

            String linha = String.join(";",
                    filme.getTitulo(),
                    sessao.getHoraInicio() + " → " + sessao.getHoraFim(),
                    sessao.getSala().getNome(),
                    String.valueOf(qnt),
                    String.format("%.2f", total).replace(",", ".")
            );

            pw.println(linha);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar vendas de bilhetes: " + e.getMessage());
        }
    }


    private void guardarVendaUtilizador() {
        double total = 0;

        for (Map.Entry<String, ItemCarrinho> entry : carrinho.entrySet()) {
            total += entry.getValue().getTotal();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_VENDAS_UTILIZADOR, true))) {
            pw.println(nomeUser + ";" + String.format("%.2f", total).replace(",", "."));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar venda do utilizador: " + e.getMessage());
        }
    }


}
