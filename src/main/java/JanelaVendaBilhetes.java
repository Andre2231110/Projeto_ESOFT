import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.List;

public class JanelaVendaBilhetes extends JFrame {
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblFilmes;
    private JButton backButton;
    private JPanel painelFilmes;
    private JPanel contentPane;
    private JScrollPane scrollFilmes;
    private String nomeUser;
    private List<Filme> listaFilmes;

    public JanelaVendaBilhetes(String nomeUser, List<Filme> listaFilmes) {
        this.nomeUser = nomeUser;
        this.listaFilmes = listaFilmes;

        setTitle("Venda de Bilhetes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel topo = new JPanel(new BorderLayout());
        lblUser = new JLabel("Utilizador: " + nomeUser);
        backButton = new JButton("< Back");
        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        topo.add(backButton, BorderLayout.WEST);
        topo.add(lblUser, BorderLayout.EAST);
        contentPane.add(topo, BorderLayout.NORTH);

        painelFilmes = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JScrollPane scroll = new JScrollPane(painelFilmes);
        contentPane.add(scroll, BorderLayout.CENTER);

        atualizarFilmes();
        setVisible(true);
    }

    private void atualizarFilmes() {
        painelFilmes.removeAll();

        for (Filme f : listaFilmes) {
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setPreferredSize(new Dimension(200, 300));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setBackground(new Color(235, 255, 235));

            ImageIcon icon = new ImageIcon(f.getImagem());
            Image scaled = icon.getImage().getScaledInstance(180, 220, Image.SCALE_SMOOTH);
            JLabel lblImagem = new JLabel(new ImageIcon(scaled));
            lblImagem.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lblTitulo = new JLabel("<html><center>" + f.getTitulo() + "</center></html>");
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

            JButton btnSelecionar = new JButton("Selecionar");
            btnSelecionar.addActionListener(e -> {
                new JanelaSessoes(f, nomeUser);
                dispose();
            });

            card.add(lblImagem, BorderLayout.CENTER);
            card.add(lblTitulo, BorderLayout.NORTH);
            card.add(btnSelecionar, BorderLayout.SOUTH);

            painelFilmes.add(card);
        }

        painelFilmes.revalidate();
        painelFilmes.repaint();
    }
}
