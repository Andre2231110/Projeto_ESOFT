import javax.swing.*;
import java.awt.*;

public class JanelaDetalhesFilme extends JDialog {
    private JPanel contentPane;
    private JLabel lblImagem;
    private JLabel lblTitulo;
    private JLabel lblDuracao;
    private JLabel lblSinopse;
    private JLabel lblGenero;
    private JLabel lblLicenca;
    private JLabel lblBilhete;
    private JButton removerButton;
    private JButton editarButton;
    private JButton cancelarButton;
    private JTextArea txtSinopse;

    private Filme filme;

    public JanelaDetalhesFilme(JFrame parent, Filme filme) {
        super(parent, "Detalhes do Filme", true);
        this.filme = filme;

        setContentPane(contentPane);
        setSize(800, 600);
        setLocationRelativeTo(parent);

        lblTitulo.setText("Título do Filme: " + filme.getTitulo());
        lblDuracao.setText("Duração: " + filme.getDuracao() + " min");
        lblGenero.setText("Género: " + filme.getGenero());
        lblLicenca.setText("Preço da Licença: " + String.format("%.2f €", filme.getPrecoLicenca()));
        lblBilhete.setText("Preço do Bilhete: " + String.format("%.2f €", filme.getPrecoBilhete()));
        txtSinopse.setText(filme.getSinopse());


        SwingUtilities.invokeLater(() -> {
            if (lblImagem.getWidth() > 0 && lblImagem.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(filme.getImagem());
                Image img = icon.getImage().getScaledInstance(lblImagem.getWidth(), lblImagem.getHeight(), Image.SCALE_SMOOTH);
                lblImagem.setIcon(new ImageIcon(img));
            } else {
                ImageIcon icon = new ImageIcon(filme.getImagem());
                Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
                lblImagem.setIcon(new ImageIcon(img));
            }
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
