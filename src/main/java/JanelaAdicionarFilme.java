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
    private JanelaFilmes janelaFilmes;
    private Filme filmeOriginal = null;

    public JanelaAdicionarFilme(JFrame parent, JanelaFilmes janelaFilmes) {
        super(parent, "Adicionar Filme", true);
        this.janelaFilmes = janelaFilmes;

        initJanela(parent);
        setVisible(true);
    }

    public JanelaAdicionarFilme(JFrame parent, JanelaFilmes janelaFilmes, Filme filmeAEditar) {
        super(parent, "Editar Filme", true);
        this.janelaFilmes = janelaFilmes;
        this.filmeOriginal = filmeAEditar;

        initJanela(parent);

        txtTitulo.setText(filmeAEditar.getTitulo());
        txtDuracao.setText(String.valueOf(filmeAEditar.getDuracao()));
        txtSinopse.setText(filmeAEditar.getSinopse());
        txtGenero.setText(filmeAEditar.getGenero());

        imagemSelecionada = filmeAEditar.getImagem();
        ImageIcon icon = new ImageIcon(imagemSelecionada);
        Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        lblPreviewImagem.setIcon(new ImageIcon(img));
        lblPreviewImagem.setText("");

        adicionarBtn.setText("Guardar"); // muda o texto do botão
        setVisible(true);
    }

    private void initJanela(JFrame parent) {
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        lblPreviewImagem.setOpaque(true);
        lblPreviewImagem.setBackground(new Color(216, 245, 209));
        lblPreviewImagem.setText("+");

        lblPreviewImagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                escolherImagem();
            }
        });

        adicionarBtn.addActionListener(e -> adicionarOuEditarFilme());
    }

    private void escolherImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar imagem do filme");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Imagens", "jpg", "png", "jpeg"));

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

    private void adicionarOuEditarFilme() {
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

            if (filmeOriginal != null) {
                filmeOriginal.setTitulo(titulo);
                filmeOriginal.setDuracao(duracao);
                filmeOriginal.setSinopse(sinopse);
                filmeOriginal.setGenero(genero);
                filmeOriginal.setImagem(imagemSelecionada);

                janelaFilmes.atualizarLista();
                JOptionPane.showMessageDialog(this, "Filme atualizado com sucesso!");
            } else {
                Filme novo = new Filme(titulo, duracao, sinopse, genero, imagemSelecionada);
                janelaFilmes.adicionarFilme(novo);
                JOptionPane.showMessageDialog(this, "Filme adicionado com sucesso!");
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A duração deve ser um número inteiro.");
        }
    }
}
