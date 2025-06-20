import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaFilmes extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblFilmes;
    private JPanel painelFilmes;
    private JButton adicionarButton;
    private JButton removerButton;
    private JButton backButton;


    private List<Filme> filmes = new ArrayList<>();


    public JanelaFilmes(String nomeUser) {
        setTitle("Gestão de Filmes");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        adicionarButton.addActionListener(e -> new JanelaAdicionarFilme(this));

        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });


        removerButton.addActionListener(e -> {
            if (!filmes.isEmpty()) {
                filmes.remove(filmes.size() - 1); // remove o último
                atualizarLista();
            }
        });

        atualizarLista();
        setVisible(true);
    }

    private void adicionarFilmeTeste() {
        Filme novo = new Filme(
                "Minecraft",
                90,
                "Um filme sobre blocos",
                "Aventura",
                "src/main/resources/minecraft.jpg" // adapta o caminho à tua imagem
        );
        filmes.add(novo);
        atualizarLista();
    }

    private void atualizarLista() {
        painelFilmes.removeAll();

        for (Filme f : filmes) {
            painelFilmes.add(criarCardFilme(f));
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    private JPanel criarCardFilme(Filme filme) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(120, 200));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));


        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image scaled = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        JLabel lblImagem = new JLabel(new ImageIcon(scaled));


        JLabel lblTitulo = new JLabel("<html><center>" + filme.getTitulo() + "</center></html>", SwingConstants.CENTER);

        card.add(lblImagem, BorderLayout.CENTER);
        card.add(lblTitulo, BorderLayout.SOUTH);

        return card;
    }

}
