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
    private List<ResultadoFinanceiroProduto> resultadosProdutos;
    private List<ResultadoFinanceiroFilme> resultadosFilmes;
    private List<ResultadoFinanceiroSessao> resultadosSessoes;
    private List<ResultadoFinanceiroMensal> resultadosMensais;


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
        btnFilmes.addActionListener(e -> {
            resultadosFilmes = calcularLucrosFilmes();
            mostrarLucrosFilmes(resultadosFilmes);
        });
        btnSalas = new JButton("Salas");
        btnSalas.addActionListener(e -> {
            resultadosSalas = calcularlucroSalas(); // lê e guarda em memória
            mostrarNaTabela(resultadosSalas); // mostra todos (lucro e prejuízo)
        });
        btnSessoes = new JButton("Sessões");
        btnSessoes.addActionListener(e -> {
            resultadosSessoes = calcularLucrosSessoes();

            mostrarLucrosSessoes(resultadosSessoes);
        });
        btnProdutos = new JButton("Produtos");
        btnProdutos.addActionListener(e -> {
            resultadosProdutos = calcularLucrosProdutos();
            mostrarProdutosNaTabela(resultadosProdutos);
        });
        btnMes = new JButton("Mês");
        btnMes.addActionListener(e -> {
            resultadosMensais = calcularLucrosPorMes();

            mostrarLucrosMensais(resultadosMensais);
        });

        JButton[] botoes = {btnFilmes, btnSalas, btnSessoes, btnProdutos, btnMes};
        int y = 60;

        for (JButton btn : botoes) {
            btn.setBounds(30, y, 120, 40);
            btn.setFont(new Font("Serif", Font.PLAIN, 16));
            add(btn);
            y += 50;
        }

        btnLucro = new JButton("Lucro");
        btnLucro.setBounds(440, 60, 100, 30);
        btnLucro.setFont(new Font("Serif", Font.PLAIN, 14));

        btnLucro.addActionListener(e -> {
            if (resultadosSalas != null) {
                var lucroSalas = resultadosSalas.stream()
                        .filter(r -> r.getLucro() >= 0)
                        .toList();
                mostrarNaTabela(lucroSalas);
            } else if (resultadosProdutos != null) {
                var lucroProdutos = resultadosProdutos.stream()
                        .filter(p -> p.getLucro() >= 0)
                        .toList();
                mostrarProdutosNaTabela(lucroProdutos);
            } else if (resultadosFilmes != null) {
                var lucroFilmes = resultadosFilmes.stream()
                        .filter(f -> f.getLucro() >= 0)
                        .toList();
                mostrarLucrosFilmes(lucroFilmes);
            } else if (resultadosSessoes != null) {
                var lucroSessoes = resultadosSessoes.stream()
                        .filter(s -> s.getLucro() >= 0)
                        .toList();
                mostrarLucrosSessoes(lucroSessoes);
            }else if (resultadosMensais != null) {
                var lucroMeses = resultadosMensais.stream()
                        .filter(m -> m.getLucroTotal() >= 0)
                        .toList();
                mostrarLucrosMensais(lucroMeses);
            }
        });








        add(btnLucro);

        btnPrejuizo = new JButton("Prejuízo");
        btnPrejuizo.setBounds(540, 60, 100, 30);
        btnPrejuizo.setFont(new Font("Serif", Font.PLAIN, 14));
        btnPrejuizo.addActionListener(e -> {
            if (resultadosSalas != null) {
                var prejuizoSalas = resultadosSalas.stream()
                        .filter(r -> r.getLucro() < 0)
                        .toList();
                mostrarNaTabela(prejuizoSalas);
            } else if (resultadosProdutos != null) {
                var prejuizoProdutos = resultadosProdutos.stream()
                        .filter(p -> p.getLucro() < 0)
                        .toList();
                mostrarProdutosNaTabela(prejuizoProdutos);
            } else if (resultadosFilmes != null) {
                var prejuizoFilmes = resultadosFilmes.stream()
                        .filter(f -> f.getLucro() < 0)
                        .toList();
                mostrarLucrosFilmes(prejuizoFilmes);
            } else if (resultadosSessoes != null) {
                var prejuizoSessoes = resultadosSessoes.stream()
                        .filter(s -> s.getLucro() < 0)
                        .toList();
                mostrarLucrosSessoes(prejuizoSessoes);
            }else if (resultadosMensais != null) {
                var prejuizoMeses = resultadosMensais.stream()
                        .filter(m -> m.getLucroTotal() < 0)
                        .toList();
                mostrarLucrosMensais(prejuizoMeses);
            }
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

    private List<ResultadoFinanceiroSala> calcularlucroSalas() {
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
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem dados de salas com lucro/prejuízo.");
        }

        return resultados;
    }
    private List<ResultadoFinanceiroProduto> calcularLucrosProdutos() {
        List<ResultadoFinanceiroProduto> resultados = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("src/main/java/csv/produtos.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(",");

                if (partes.length >= 4) {
                    String nome = partes[0];
                    double precoVenda = Double.parseDouble(partes[2].replace(",", "."));
                    double precoCompra = Double.parseDouble(partes[3].replace(",", "."));

                    resultados.add(new ResultadoFinanceiroProduto(nome, precoVenda, precoCompra));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler produtos: " + e.getMessage());
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem dados de produtos com lucro/prejuízo.");
        }

        return resultados;
    }
    private List<ResultadoFinanceiroFilme> calcularLucrosFilmes() {
        Map<String, Integer> bilhetesPorFilme = new HashMap<>();
        Map<String, Double> receitaPorFilme = new HashMap<>();

        // 1. Lê vendasBilhete.csv
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/vendasBilhete.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(";");
                if (partes.length >= 5) {
                    String titulo = partes[0];
                    int quantidade = Integer.parseInt(partes[3]);
                    double preco = Double.parseDouble(partes[4].replace(",", "."));

                    bilhetesPorFilme.put(titulo, bilhetesPorFilme.getOrDefault(titulo, 0) + quantidade);
                    receitaPorFilme.put(titulo,
                            receitaPorFilme.getOrDefault(titulo, 0.0) + (preco * quantidade));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler vendasBilhete.csv: " + e.getMessage());
        }

        // 2. Lê filmes.csv e calcula lucro
        List<ResultadoFinanceiroFilme> resultados = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/filmes.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(";");
                if (partes.length >= 6) {
                    String titulo = partes[0];
                    double precoLicenca = Double.parseDouble(partes[5].replace(",", "."));

                    int vendidos = bilhetesPorFilme.getOrDefault(titulo, 0);
                    double receitaTotal = receitaPorFilme.getOrDefault(titulo, 0.0);
                    double precoBilheteMedio = vendidos > 0 ? receitaTotal / vendidos : 0.0;

                    resultados.add(new ResultadoFinanceiroFilme(titulo, vendidos, precoBilheteMedio, precoLicenca));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler filmes.csv: " + e.getMessage());
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem dados de filmes com lucro/prejuízo.");
        }

        return resultados;
    }

    private List<ResultadoFinanceiroSessao> calcularLucrosSessoes() {
        Map<String, Integer> bilhetesPorSessao = new HashMap<>();
        Map<String, Double> precoPorSessao = new HashMap<>();

        // Lê vendasBilhete.csv
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/vendasBilhete.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(";");
                if (partes.length >= 5) {
                    String filme = partes[0];
                    String horario = partes[1].split("→")[0].trim(); // extrai hora início
                    String sala = partes[2];
                    int quantidade = Integer.parseInt(partes[3]);
                    double preco = Double.parseDouble(partes[4].replace(",", "."));

                    String chave = filme + ";" + sala + ";" + horario;

                    bilhetesPorSessao.put(chave, bilhetesPorSessao.getOrDefault(chave, 0) + quantidade);
                    precoPorSessao.put(chave, preco);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler vendasBilhete.csv: " + e.getMessage());
        }

        // Lê sessoes.csv
        List<ResultadoFinanceiroSessao> resultados = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/sessoes.csv"))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(",");
                if (partes.length >= 5) {
                    String filme = partes[0];
                    String sala = partes[1];
                    String data = partes[2];
                    String hora = partes[3];

                    String chave = filme + ";" + sala + ";" + hora;

                    int vendidos = bilhetesPorSessao.getOrDefault(chave, 0);
                    double precoBilhete = precoPorSessao.getOrDefault(chave, 0.0);
                    double custo = 50.0; // ou outro critério mais tarde

                    resultados.add(new ResultadoFinanceiroSessao(filme, sala, data, hora, vendidos, precoBilhete, custo));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler sessoes.csv: " + e.getMessage());
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem sessões com dados cruzados para calcular lucro/prejuízo.");
        }

        return resultados;
    }

    private List<ResultadoFinanceiroMensal> calcularLucrosPorMes() {
        Map<String, Double> lucroPorMes = new HashMap<>();

        // 1. Salas
        List<ResultadoFinanceiroSala> salas = calcularlucroSalas();
        try (Scanner scanner = new Scanner(new File("src/main/java/csv/reservas.csv"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(",");
                if (partes.length >= 5) {
                    Date data = sdf.parse(partes[4]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    String mesAno = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1);

                    String nomeSala = partes[5];
                    ResultadoFinanceiroSala sala = salas.stream()
                            .filter(s -> s.getNomeSala().equals(nomeSala))
                            .findFirst().orElse(null);

                    if (sala != null) {
                        lucroPorMes.put(mesAno,
                                lucroPorMes.getOrDefault(mesAno, 0.0) + sala.getLucro());
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar datas das reservas: " + e.getMessage());
        }

        // 2. Sessoes
        List<ResultadoFinanceiroSessao> sessoes = calcularLucrosSessoes();
        for (ResultadoFinanceiroSessao s : sessoes) {
            String[] partesData = s.getData().split("/");
            if (partesData.length == 3) {
                String mesAno = partesData[2] + "-" + partesData[1]; // ano-mes
                lucroPorMes.put(mesAno,
                        lucroPorMes.getOrDefault(mesAno, 0.0) + s.getLucro());
            }
        }

        // 3. Converte para lista de objetos
        List<ResultadoFinanceiroMensal> resultados = new ArrayList<>();
        for (Map.Entry<String, Double> entry : lucroPorMes.entrySet()) {
            resultados.add(new ResultadoFinanceiroMensal(entry.getKey(), entry.getValue()));
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
    private void mostrarProdutosNaTabela(List<ResultadoFinanceiroProduto> produtos) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);

        modeloTabela.addColumn("Produto");
        modeloTabela.addColumn("Preço Venda");
        modeloTabela.addColumn("Preço Compra");
        modeloTabela.addColumn("Lucro");

        for (ResultadoFinanceiroProduto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.getNome(),
                    String.format("%.2f", p.getPrecoVenda()),
                    String.format("%.2f", p.getPrecoCompra()),
                    String.format("%.2f", p.getLucro())
            });
        }
    }
    private void mostrarLucrosFilmes(List<ResultadoFinanceiroFilme> filmes) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);

        modeloTabela.addColumn("Filme");
        modeloTabela.addColumn("Receita");
        modeloTabela.addColumn("Custo");
        modeloTabela.addColumn("Lucro");

        for (ResultadoFinanceiroFilme f : filmes) {
            modeloTabela.addRow(new Object[]{
                    f.getTitulo(),
                    String.format("%.2f", f.getReceita()),
                    String.format("%.2f", f.getCusto()),
                    String.format("%.2f", f.getLucro())
            });
        }
    }
    private void mostrarLucrosSessoes(List<ResultadoFinanceiroSessao> sessoes) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);

        modeloTabela.addColumn("Filme");
        modeloTabela.addColumn("Sala");
        modeloTabela.addColumn("Data");
        modeloTabela.addColumn("Hora");
        modeloTabela.addColumn("Receita");
        modeloTabela.addColumn("Custo");
        modeloTabela.addColumn("Lucro");

        for (ResultadoFinanceiroSessao s : sessoes) {
            modeloTabela.addRow(new Object[]{
                    s.getFilme(),
                    s.getSala(),
                    s.getData(),
                    s.getHora(),
                    String.format("%.2f", s.getReceita()),
                    String.format("%.2f", s.getCusto()),
                    String.format("%.2f", s.getLucro())
            });
        }
    }

    private void mostrarLucrosMensais(List<ResultadoFinanceiroMensal> lista) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        modeloTabela.addColumn("Mês");
        modeloTabela.addColumn("Lucro Total");

        for (ResultadoFinanceiroMensal r : lista) {
            modeloTabela.addRow(new Object[]{
                    r.getMesAno(),
                    String.format("%.2f", r.getLucroTotal())
            });
        }
    }





}