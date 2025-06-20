import javax.swing.*;
import java.awt.*;

public class JanelaDetalhesFilme extends JDialog {
    private JPanel contentPane;
    private JLabel lblImagem;
    private JLabel lblTitulo;
    private JLabel lblDuracao;
    private JLabel lblSinopse;
    private JLabel lblGenero;
    private JButton removerButton;
    private JButton editarButton;
    private JButton cancelarButton;

    private Filme filme;

    public JanelaDetalhesFilme(JFrame parent, Filme filme) {
        super(parent, "Detalhes do Filme", true);
        this.filme = filme;

        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(parent);

        // Texto com HTML e estilo fixo
        lblTitulo.setText("<html><b>Título do Filme:</b> " + filme.getTitulo() + "</html>");
        lblDuracao.setText("<html><b>Duração:</b> " + filme.getDuracao() + " min</html>");
        lblGenero.setText("<html><b>Género:</b> " + filme.getGenero() + "</html>");
        lblSinopse.setText("<html><b>Sinopse:</b><br><div style='width:400px'>" + filme.getSinopse() + "</div></html>");

        // Carregar imagem após layout estar pronto
        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(filme.getImagem());

            int largura = lblImagem.getWidth();
            int altura = lblImagem.getHeight();

            if (largura == 0 || altura == 0) {
                largura = 180;
                altura = 240;
            }

            Image img = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
            lblImagem.setIcon(new ImageIcon(img));
        });

        cancelarButton.addActionListener(e -> dispose());

        removerButton.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja remover este filme?", "Confirmar remoção", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                ((JanelaFilmes) parent).removerFilme(filme);
                ((JanelaFilmes) parent).guardarFilmesCSV();
                dispose();
            }
        });

        editarButton.addActionListener(e -> {
            new JanelaAdicionarFilme((JFrame) parent, (JanelaFilmes) parent, filme);
            ((JanelaFilmes) parent).atualizarLista();
            ((JanelaFilmes) parent).guardarFilmesCSV();
            dispose();
        });

        setVisible(true);
    }
}
