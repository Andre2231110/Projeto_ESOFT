import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class JanelaVendaFilmes extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JPanel painelFilmes;
    private JButton backButton;
    private JLabel lblUser;
    private JLabel lblVenda;

    private String nomeUser;

    public JanelaVendaFilmes(String nomeUser, List<Filme> filmesDisponiveis) {
        this.nomeUser = nomeUser;

        setTitle("Venda de Bilhetes");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        lblUser.setText(nomeUser);

        mostrarFilmes(filmesDisponiveis);

        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        setVisible(true);
    }

    private void mostrarFilmes(List<Filme> filmes) {
        painelFilmes.removeAll();

        for (Filme filme : filmes) {
            painelFilmes.add(criarCardFilme(filme));
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }

    private JPanel criarCardFilme(Filme filme) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(140, 220));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        JLabel lblImagem = new JLabel(new ImageIcon(img));

        int lugaresOcupados = filme.getLugaresOcupados();
        int totalLugares = filme.getCapacidade();
        JLabel lblOcupacao = new JLabel(lugaresOcupados + "/" + totalLugares + " ocupados", SwingConstants.CENTER);
        lblOcupacao.setFont(new Font("Arial", Font.PLAIN, 12));

        card.add(lblImagem, BorderLayout.CENTER);
        card.add(lblOcupacao, BorderLayout.SOUTH);

        /*
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new JanelaVendaSessoes(JanelaVendaFilmes.this, nomeUser, filme);
                dispose();
            }
        });
*/
        return card;
    }
}
