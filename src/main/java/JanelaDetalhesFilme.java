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
        setSize(600, 600);
        setLocationRelativeTo(parent);

        lblTitulo.setText(lblTitulo.getText() + " " + filme.getTitulo());
        lblDuracao.setText(lblDuracao.getText() + " " + filme.getDuracao() + " min");
        lblGenero.setText(lblGenero.getText() + " " + filme.getGenero());
        lblSinopse.setText("<html>" + lblSinopse.getText() + "<br>" + filme.getSinopse() + "</html>");

        ImageIcon icon = new ImageIcon(filme.getImagem());
        Image img = icon.getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH);
        lblImagem.setIcon(new ImageIcon(img));

        cancelarButton.addActionListener(e -> dispose());

        removerButton.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja remover este filme?", "Confirmar remoção", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                ((JanelaFilmes) parent).removerFilme(filme);
                dispose();
            }
        });

        editarButton.addActionListener(e -> {
            new JanelaAdicionarFilme((JFrame) parent, (JanelaFilmes) parent, filme);
            dispose();
        });

        setVisible(true);
    }
}
