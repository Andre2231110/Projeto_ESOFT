import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class JanelaAdicionarFilme extends JDialog {
    private JPanel contentPane;
    private JPanel painelFormulario;
    private JTextField txtTitulo;
    private JTextField txtDuracao;
    private JTextArea txtSinopse;
    private JTextField txtGenero;
    private JLabel lblTitulo;
    private JLabel lblDuracao;
    private JLabel lblSinopse;
    private JLabel lblGenero;
    private JPanel painelImagem;
    private JLabel lblPreviewImagem;
    private JButton adicionarBtn;

    private String imagemSelecionada = null;

    public JanelaAdicionarFilme(JFrame parent) {
        super(parent, "Adicionar Filme", true);
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        lblPreviewImagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                escolherImagem();
            }
        });

        adicionarBtn.addActionListener(e -> adicionarFilme());

        setVisible(true);
    }

    private void escolherImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar imagem do filme");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagens", "jpg", "png", "jpeg"));

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File ficheiro = fileChooser.getSelectedFile();
            imagemSelecionada = ficheiro.getAbsolutePath();

            ImageIcon icon = new ImageIcon(imagemSelecionada);
            Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            lblPreviewImagem.setIcon(new ImageIcon(img));
            lblPreviewImagem.setText("");
        }
    }

    private void adicionarFilme() {
        String titulo = txtTitulo.getText().trim();
        String duracaoStr = txtDuracao.getText().trim();
        String sinopse = txtSinopse.getText().trim();
        String genero = txtGenero.getText().trim();

        if (titulo.isEmpty() || duracaoStr.isEmpty() || sinopse.isEmpty() || genero.isEmpty() || imagemSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor preenche todos os campos e seleciona uma imagem.");
            return;
        }

        try {
            int duracao = Integer.parseInt(duracaoStr);
            JOptionPane.showMessageDialog(this, "Filme \"" + titulo + "\" adicionado com sucesso!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A duração deve ser um número inteiro.");
        }
    }
}
