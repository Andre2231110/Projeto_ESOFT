import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class JanelaConsultas extends JFrame {

    private JButton btnFilmes, btnSalas, btnSessoes, btnProdutos, btnMes;
    private JTable tabelaEstatisticas;
    private DefaultTableModel modeloTabela;
    private JButton btnLucro, btnPrejuizo;
    private List<ResultadoFinanceiroSala> resultadosSalas;


    public JanelaConsultas() {
        setTitle("Consultas");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 255, 240));

        JLabel lblTitulo = new JLabel("Consultas de Estatísticas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setBounds(300, 10, 300, 30);
        add(lblTitulo);

        criarBotoes();
        criarTabela();

        setVisible(true);
    }

    private void criarBotoes() {
        btnFilmes = new JButton("Filmes");
        btnSalas = new JButton("Salas");
        btnSessoes = new JButton("Sessões");
        btnProdutos = new JButton("Produtos");
        btnMes = new JButton("Mês");

        JButton[] botoes = {btnFilmes, btnSalas, btnSessoes, btnProdutos, btnMes};
        int y = 60;

        for (JButton btn : botoes) {
            btn.setBounds(30, y, 120, 40);
            btn.setFont(new Font("Serif", Font.PLAIN, 16));
            add(btn);
            y += 50;
        }
        btnSalas.addActionListener(e -> {
            resultadosSalas = calcularResultadosFinanceiros(); // lê e guarda em memória
            mostrarNaTabela(resultadosSalas); // mostra todos (lucro e prejuízo)
        });
        btnLucro = new JButton("Lucro");
        btnLucro.setBounds(440, 60, 100, 30);
        btnLucro.setFont(new Font("Serif", Font.PLAIN, 14));

        btnLucro.addActionListener(e -> {
            if (resultadosSalas == null) return;
            List<ResultadoFinanceiroSala> lucro = resultadosSalas.stream()
                    .filter(r -> r.getLucro() >= 0)
                    .toList();
            mostrarNaTabela(lucro);
        });





        add(btnLucro);

        btnPrejuizo = new JButton("Prejuízo");
        btnPrejuizo.setBounds(540, 60, 100, 30);
        btnPrejuizo.setFont(new Font("Serif", Font.PLAIN, 14));
        btnPrejuizo.addActionListener(e -> {
            if (resultadosSalas == null) return;
            List<ResultadoFinanceiroSala> prejuizo = resultadosSalas.stream()
                    .filter(r -> r.getLucro() < 0)
                    .toList();
            mostrarNaTabela(prejuizo);
        });
        add(btnPrejuizo);
    }

    private void criarTabela() {
        modeloTabela = new DefaultTableModel();
        tabelaEstatisticas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaEstatisticas);
        scroll.setBounds(180, 100, 680, 400); // altura menor que antes
        scroll.getViewport().setBackground(new Color(220, 255, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 255), 2));
        add(scroll);
    }

    private List<ResultadoFinanceiroSala> calcularResultadosFinanceiros() {
        Map<String, Double> totalPagoPorSala = new HashMap<>();

        // Primeiro lê as reservas.csv e soma os valores pagos por nome de sala
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/reservas.csv"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(",");
                if (partes.length == 6) {
                    String nomeSala = partes[5]; // sala onde foi feita a reserva
                    double precoPago = Double.parseDouble(partes[3].replace(",", "."));
                    totalPagoPorSala.put(nomeSala,
                            totalPagoPorSala.getOrDefault(nomeSala, 0.0) + precoPago);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler reservas: " + e.getMessage());
        }

        // Agora lê as salas.csv e cruza com os totais pagos
        List<ResultadoFinanceiroSala> resultados = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/salas.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(",");
                if (partes.length >= 7) {
                    String nomeSala = partes[0];
                    double custoAbertura = Double.parseDouble(partes[6].replace(",", "."));
                    double totalPago = totalPagoPorSala.getOrDefault(nomeSala, 0.0);

                    resultados.add(new ResultadoFinanceiroSala(nomeSala, custoAbertura, totalPago));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler salas: " + e.getMessage());
        }

        return resultados;
    }

    private void mostrarNaTabela(List<ResultadoFinanceiroSala> lista) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        modeloTabela.addColumn("Sala");
        modeloTabela.addColumn("Custo");
        modeloTabela.addColumn("Total Pago");
        modeloTabela.addColumn("Lucro");

        for (ResultadoFinanceiroSala r : lista) {
            modeloTabela.addRow(new Object[]{
                    r.getNomeSala(),
                    String.format("%.2f", r.getCusto()),
                    String.format("%.2f", r.getTotalPago()),
                    String.format("%.2f", r.getLucro())
            });
        }
    }




}