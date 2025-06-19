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

    private String nomeUser;
    private List<Filme> filmes = new ArrayList<>();

    public JanelaFilmes(String nomeUser) {
        this.nomeUser = nomeUser;

        setTitle("Gestão de Filmes");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Botão "Adicionar"
        adicionarButton.addActionListener(e -> new JanelaAdicionarFilme(this));

        // Botão "Remover" - remove o último da lista
        removerButton.addActionListener(e -> {
            if (!filmes.isEmpty()) {
                filmes.remove(filmes.size() - 1);
                atualizarLista();
            }
        });

        // Botão "Voltar"
        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        atualizarLista();
        setVisible(true);
    }

    public void adicionarFilme(Filme filme) {
        filmes.add(filme);
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

        // Imagem
        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image scaled = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        JLabel lblImagem = new JLabel(new ImageIcon(scaled));

        // Título
        JLabel lblTitulo = new JLabel("<html><center>" + filme.getTitulo() + "</center></html>", SwingConstants.CENTER);

        card.add(lblImagem, BorderLayout.CENTER);
        card.add(lblTitulo, BorderLayout.SOUTH);

        return card;
    }

    private void createUIComponents() {
        // Custom UI components se necessário
    }
}

