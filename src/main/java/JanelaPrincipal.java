import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JanelaPrincipal extends JFrame {
    private JPanel painelTopo;
    private JLabel lblUser;
    private JButton btnVendaBilhetes;
    private JButton btnSalas;
    private JButton btnFilmes;
    private JButton btnSessoes;
    private JButton btnBar;
    private JButton btnCampanhas;
    private JButton btnFuncionarios;
    private JButton btnConsultas;
    private JPanel contentPane;

    public JanelaPrincipal(String nomeUser) {
        setTitle("Menu Principal");
        setContentPane(contentPane);
        setIconeNoBotao(btnVendaBilhetes, "/gestaoVendaBilhetes.png");
        setIconeNoBotao(btnSessoes, "/gestaoSessoes.png");
        setIconeNoBotao(btnSalas, "/gestaoSalas.png");
        setIconeNoBotao(btnBar, "/gestaoBar.png");
        setIconeNoBotao(btnFilmes, "/gestaoFilmes.png");
        setIconeNoBotao(btnCampanhas, "/gestaoCampanhas.png");
        setIconeNoBotao(btnFuncionarios, "/gestaoFuncionarios.png");
        setIconeNoBotao(btnConsultas, "/moduloConsultas.png");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        lblUser.setText(nomeUser);

        btnFilmes.addActionListener(e -> {
            new JanelaFilmes(nomeUser);
            dispose();
        });

        btnVendaBilhetes.addActionListener(e -> {
            List<Filme> filmes = JanelaFilmes.chamarFilmesCSV();
            new JanelaVendaBilhetes(nomeUser, filmes);
            dispose();
        });



        btnSessoes.addActionListener(e -> {
            new JanelaSessao();
            dispose();
        });

        btnSalas.addActionListener(e -> {
            new JanelaSalas();
        });

        btnBar.addActionListener(e -> {
            new JanelaBar(nomeUser);
            dispose();
        });

        btnCampanhas.addActionListener(e -> {
            new JanelaCampanhas(nomeUser);
            dispose();
        });

        btnFuncionarios.addActionListener(e -> {
            new JanelaFuncionario(nomeUser);
            dispose();
        });

        btnConsultas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));

        setVisible(true);
    }

    private void setIconeNoBotao(JButton botao, String caminho) {
        ImageIcon icon = new ImageIcon(getClass().getResource(caminho));
        Image imagemRedimensionada = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(imagemRedimensionada));
        botao.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private List<Filme> carregarFilmesCSV() {
        List<Filme> lista = new ArrayList<>();
        File file = new File("csv/Filmes.csv");

        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 7) {
                    String titulo = partes[0];
                    int duracao = Integer.parseInt(partes[1]);
                    String sinopse = partes[2];
                    String genero = partes[3];
                    String imagem = partes[4];
                    int ocupados = Integer.parseInt(partes[5]);
                    int capacidade = Integer.parseInt(partes[6]);

                    lista.add(new Filme(titulo, duracao, sinopse, genero, imagem, ocupados, capacidade));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar filmes: " + e.getMessage());
        }

        return lista;
    }
}
