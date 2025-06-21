import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaVendaSessoes extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblFilmes;
    private JButton backButton;
    private JTextArea lblDescricao;
    private JLabel lblImage;
    private JButton seguinteButton;
    private JPanel painelLugares;
    private JPanel painelSessoes;

    private List<JToggleButton> botoesLugares = new ArrayList<>();
    private String nomeUser;
    private Filme filme;
    private List<Sessao> sessoes;

    private Sessao sessaoSelecionada = null;
    List<String> lugaresSelecionados = new ArrayList<>();

    public JanelaVendaSessoes(Filme filme, String nomeUser, List<Sessao> sessoesDisponiveis) {
        this.nomeUser = nomeUser;
        this.filme = filme;
        this.sessoes = sessoesDisponiveis;

        setTitle("Venda de Bilhetes - Sessões");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setContentPane(contentPane);

        lblUser.setText(nomeUser);
        lblFilmes.setText(filme.getTitulo());

        lblDescricao.setText(filme.getSinopse());
        lblDescricao.setOpaque(false);
        lblDescricao.setLineWrap(true);
        lblDescricao.setWrapStyleWord(true);
        lblDescricao.setEditable(false);

        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image scaled = icon.getImage().getScaledInstance(140, 180, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(scaled));

        configurarBotoesSessoesDinamico();
        gerarMapaDeLugares();

        backButton.addActionListener(e -> {
            new JanelaFilmes(nomeUser);
            dispose();
        });

        seguinteButton.addActionListener(e -> {
            if (sessaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor seleciona uma sessão primeiro.");
                return;
            }

            lugaresSelecionados.clear();

            for (JToggleButton botao : botoesLugares) {
                if (botao.isSelected()) {
                    lugaresSelecionados.add(botao.getText());
                }
            }

            if (lugaresSelecionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum lugar selecionado. Será atribuído um aleatório.");
                for (JToggleButton botao : botoesLugares) {
                    if (!botao.isSelected()) {
                        botao.setSelected(true);
                        lugaresSelecionados.add(botao.getText());
                        break;
                    }
                }
            }

            System.out.println("Sessão: " + sessaoSelecionada.getHoraInicio() + " → " + sessaoSelecionada.getHoraFim());
            System.out.println("Lugares: " + lugaresSelecionados);

            new JanelaVendasBar(filme, sessaoSelecionada, lugaresSelecionados, nomeUser);
            dispose();
        });

        setVisible(true);
    }

    private void gerarMapaDeLugares() {
        painelLugares.removeAll();
        painelLugares.setLayout(new GridLayout(5, 5, 5, 5));

        for (int i = 1; i <= 25; i++) {
            JToggleButton lugar = new JToggleButton(String.valueOf(i));
            lugar.setBackground(Color.LIGHT_GRAY);
            botoesLugares.add(lugar);
            painelLugares.add(lugar);
        }

        painelLugares.revalidate();
        painelLugares.repaint();
    }

    private void configurarBotoesSessoesDinamico() {
        painelSessoes.removeAll();

        for (Sessao s : sessoes) {
            if (!s.getFilme().getTitulo().equalsIgnoreCase(filme.getTitulo())) continue;

            String texto = s.getHoraInicio() + " → " + s.getHoraFim() + " | Sala: " + s.getSala().getNome();
            JButton btn = new JButton(texto);
            btn.setBackground(new Color(204, 255, 204));
            btn.setPreferredSize(new Dimension(180, 30));

            btn.addActionListener(e -> {
                for (Component c : painelSessoes.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(new Color(204, 255, 204));
                    }
                }
                btn.setBackground(Color.GREEN);
                sessaoSelecionada = s;
            });

            painelSessoes.add(btn);
        }

        painelSessoes.revalidate();
        painelSessoes.repaint();
    }

    // 🔄 NOVA versão: carregar sessões COM salas
    public static List<Sessao> carregarSessoesCSV(List<Filme> filmes, List<Sala> salas) {
        List<Sessao> lista = new ArrayList<>();
        File ficheiro = new File("src/main/java/csv/sessoes.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Sessao s = Sessao.fromCSV(linha, filmes, salas);
                if (s != null) lista.add(s);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler sessões: " + e.getMessage());
        }

        return lista;
    }

    // Opcional: carregar salas, caso precises fora da main
    public static List<Sala> carregarSalasCSV() {
        List<Sala> lista = new ArrayList<>();
        File ficheiro = new File("src/main/java/csv/salas.csv");

        if (!ficheiro.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length >= 7) {
                    String nome = partes[0];
                    String tipo = partes[1];
                    String layout = partes[2].split("x")[0];
                    String som = partes[3];
                    boolean acessivel = partes[4].equals("1");
                    boolean ativa = partes[5].equals("1");
                    double preco = Double.parseDouble(partes[6]);

                    lista.add(new Sala(nome, tipo, layout, som, acessivel, ativa, preco));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler salas: " + e.getMessage());
        }

        return lista;
    }
}
