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
    private List<Filme> filmes;

    public JanelaVendaBilhetes(String nomeUser, List<Filme> listaFilmes) {
        this.nomeUser = nomeUser;
        this.filmes = listaFilmes;

        setTitle("Venda de Bilhetes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setContentPane(contentPane);
        scrollFilmes.setViewportView(painelFilmes);
        painelFilmes.setOpaque(true);

        lblUser.setText("Utilizador: " + nomeUser);
        System.out.println("Foram carregados " + filmes.size() + " filmes.");

        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        painelFilmes.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        atualizarFilmes();
        setVisible(true);
    }

    private void atualizarFilmes() {
        painelFilmes.removeAll();

        for (Filme f : filmes) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(200, 300));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setBackground(new Color(235, 255, 235));

            JLabel lblImagem;
            try {
                ImageIcon icon = new ImageIcon(f.getImagem());
                Image scaled = icon.getImage().getScaledInstance(180, 220, Image.SCALE_SMOOTH);
                lblImagem = new JLabel(new ImageIcon(scaled));
            } catch (Exception ex) {
                System.out.println("Erro na imagem do filme: " + f.getTitulo() + " → " + f.getImagem());
                continue;
            }

            lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel lblTitulo = new JLabel("<html><center>" + f.getTitulo() + "</center></html>", SwingConstants.CENTER);
            JButton btnSelecionar = new JButton("Selecionar");

            btnSelecionar.addActionListener(e -> {
                List<Sala> salas = JanelaVendaSessoes.carregarSalasCSV();
                List<Sessao> sessoes = JanelaVendaSessoes.carregarSessoesCSV(filmes, salas);
                new JanelaVendaSessoes(f, nomeUser, sessoes);
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
