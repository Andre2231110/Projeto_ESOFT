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

    private Filme filme;

    public JanelaDetalhesFilme(JFrame parent, Filme filme) {
        super(parent, "Detalhes do Filme", true);
        this.filme = filme;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(null);

        lblImagem = new JLabel();
        lblImagem.setBounds(40, 40, 180, 240);
        add(lblImagem);

        lblTitulo = new JLabel("<html><b>Título do Filme:</b> " + filme.getTitulo() + "</html>");
        lblTitulo.setBounds(250, 40, 500, 30);
        add(lblTitulo);

        lblDuracao = new JLabel("<html><b>Duração:</b> " + filme.getDuracao() + " min</html>");
        lblDuracao.setBounds(250, 80, 300, 25);
        add(lblDuracao);

        lblGenero = new JLabel("<html><b>Género:</b> " + filme.getGenero() + "</html>");
        lblGenero.setBounds(250, 110, 300, 25);
        add(lblGenero);

        lblLicenca = new JLabel("<html><b>Preço da Licença:</b> " + String.format("%.2f €", filme.getPrecoLicenca()) + "</html>");
        lblLicenca.setBounds(250, 140, 300, 25);
        add(lblLicenca);

        lblBilhete = new JLabel("<html><b>Preço do Bilhete:</b> " + String.format("%.2f €", filme.getPrecoBilhete()) + "</html>");
        lblBilhete.setBounds(250, 170, 300, 25);
        add(lblBilhete);

        lblSinopse = new JLabel("<html><b>Sinopse:</b><br><div style='width:500px'>" + filme.getSinopse() + "</div></html>");
        lblSinopse.setBounds(40, 300, 700, 120);
        add(lblSinopse);

        editarButton = new JButton("Editar");
        editarButton.setBounds(200, 460, 100, 30);
        add(editarButton);

        removerButton = new JButton("Remover");
        removerButton.setBounds(320, 460, 100, 30);
        add(removerButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(440, 460, 100, 30);
        add(cancelarButton);

        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(filme.getImagem());
            Image img = icon.getImage().getScaledInstance(lblImagem.getWidth(), lblImagem.getHeight(), Image.SCALE_SMOOTH);
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
