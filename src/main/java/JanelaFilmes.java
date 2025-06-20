import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaFilmes extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblFilmes;
    private JPanel painelFilmes;
    private JButton adicionarButton;
    private JButton backButton;

    private String nomeUser;
    private List<Filme> filmes = new ArrayList<>();
    private static final String FICHEIRO_CSV = "src/main/java/csv/Filmes.csv";


    public JanelaFilmes(String nomeUser) {
        this.nomeUser = nomeUser;

        setTitle("Gestão de Filmes");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        lblUser.setText(nomeUser);

        adicionarButton.addActionListener(e -> new JanelaAdicionarFilme(this, this));

        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        carregarFilmesCSV();
        atualizarLista();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarFilmesCSV();
            }
        });

        setVisible(true);
    }

    public void adicionarFilme(Filme filme) {
        filmes.add(filme);
        atualizarLista();
    }

    public void removerFilme(Filme filme) {
        filmes.remove(filme);
        atualizarLista();
    }

    public void atualizarLista() {
        painelFilmes.removeAll();

        for (Filme f : filmes) {
            JPanel card = criarCardFilme(f);
            painelFilmes.add(card);
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    private JPanel criarCardFilme(Filme filme) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(120, 200));

        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image scaled = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        JLabel lblImagem = new JLabel(new ImageIcon(scaled));
        JLabel lblTitulo = new JLabel("<html><center>" + filme.getTitulo() + "</center></html>", SwingConstants.CENTER);

        card.add(lblImagem, BorderLayout.CENTER);
        card.add(lblTitulo, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new JanelaDetalhesFilme(JanelaFilmes.this, filme);
            }
        });

        return card;
    }

    public void guardarFilmesCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHEIRO_CSV))) {
            for (Filme f : filmes) {
                writer.println(f.getTitulo() + ";" +
                        f.getDuracao() + ";" +
                        f.getSinopse() + ";" +
                        f.getGenero() + ";" +
                        f.getImagem());
            }
        } catch (IOException e) {
            System.err.println("Erro ao guardar filmes: " + e.getMessage());
        }
    }

    private void carregarFilmesCSV() {
        File file = new File(FICHEIRO_CSV);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    String titulo = partes[0];
                    int duracao = Integer.parseInt(partes[1]);
                    String sinopse = partes[2];
                    String genero = partes[3];
                    String imagem = partes[4];

                    filmes.add(new Filme(titulo, duracao, sinopse, genero, imagem));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar filmes: " + e.getMessage());
        }
    }
}
