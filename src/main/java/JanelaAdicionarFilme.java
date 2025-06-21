import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class JanelaAdicionarFilme extends JDialog {
    private JPanel contentPane;
    private JTextField txtTitulo;
    private JTextField txtDuracao;
    private JTextArea txtSinopse;
    private JTextField txtGenero;
    private JTextField txtLicenca;
    private JTextField txtBilhete;
    private JLabel lblPreviewImagem;
    private JButton adicionarBtn;

    private String imagemSelecionada = null;
    private JanelaFilmes janelaFilmes;
    private Filme filmeOriginal = null;

    public JanelaAdicionarFilme(JFrame parent, JanelaFilmes janelaFilmes) {
        super(parent, "Adicionar Filme", true);
        this.janelaFilmes = janelaFilmes;
        setContentPane(contentPane);
        initJanela(parent);
        setVisible(true);
    }

    public JanelaAdicionarFilme(JFrame parent, JanelaFilmes janelaFilmes, Filme filmeAEditar) {
        super(parent, "Editar Filme", true);
        this.janelaFilmes = janelaFilmes;
        this.filmeOriginal = filmeAEditar;
        setContentPane(contentPane);
        initJanela(parent);

        txtTitulo.setText(filmeAEditar.getTitulo());
        txtDuracao.setText(String.valueOf(filmeAEditar.getDuracao()));
        txtSinopse.setText(filmeAEditar.getSinopse());
        txtGenero.setText(filmeAEditar.getGenero());
        txtLicenca.setText(String.valueOf(filmeAEditar.getPrecoLicenca()));
        txtBilhete.setText(String.valueOf(filmeAEditar.getPrecoBilhete()));

        imagemSelecionada = filmeAEditar.getImagem();

        if (imagemSelecionada != null) {
        SwingUtilities.invokeLater(() -> {
            int largura = lblPreviewImagem.getWidth();
            int altura = lblPreviewImagem.getHeight();

            if (largura == 0 || altura == 0) {
                largura = 120;
                altura = 160;
            }

            ImageIcon icon = new ImageIcon(imagemSelecionada);
            Image img = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
            lblPreviewImagem.setIcon(new ImageIcon(img));
            lblPreviewImagem.setText("");
        });
        }

        adicionarBtn.setText("Guardar");
        setVisible(true);
    }

    private void initJanela(JFrame parent) {
        setSize(600, 550);
        setLocationRelativeTo(parent);


        lblPreviewImagem.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewImagem.setOpaque(true);
        lblPreviewImagem.setBackground(new Color(216, 245, 209));
        lblPreviewImagem.setText("+");
        lblPreviewImagem.setHorizontalAlignment(SwingConstants.CENTER);

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
            Image img = icon.getImage().getScaledInstance(lblPreviewImagem.getWidth(), lblPreviewImagem.getHeight(), Image.SCALE_SMOOTH);
            lblPreviewImagem.setIcon(new ImageIcon(img));
            lblPreviewImagem.setText("");
        }
    }

    private void adicionarOuEditarFilme() {
        String titulo = txtTitulo.getText().trim();
        String duracaoStr = txtDuracao.getText().trim();
        String sinopse = txtSinopse.getText().trim();
        String genero = txtGenero.getText().trim();
        String licencaStr = txtLicenca.getText().trim();
        String precoBilheteStr = txtBilhete.getText().trim();

        if (titulo.isEmpty() || duracaoStr.isEmpty() || sinopse.isEmpty() || genero.isEmpty()
                || licencaStr.isEmpty() || precoBilheteStr.isEmpty() || imagemSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Preenche todos os campos e seleciona uma imagem.");
            return;
        }

        try {
            int duracao = Integer.parseInt(duracaoStr);
            double licenca = Double.parseDouble(licencaStr);
            double precoBilhete = Double.parseDouble(precoBilheteStr);

            if (filmeOriginal != null) {
                filmeOriginal.setTitulo(titulo);
                filmeOriginal.setDuracao(duracao);
                filmeOriginal.setSinopse(sinopse);
                filmeOriginal.setGenero(genero);
                filmeOriginal.setImagem(imagemSelecionada);
                filmeOriginal.setPrecoLicenca(licenca);
                filmeOriginal.setPrecoBilhete(precoBilhete);

                janelaFilmes.atualizarLista();
                JOptionPane.showMessageDialog(this, "Filme atualizado com sucesso!");
            } else {
                Filme novo = new Filme(titulo, duracao, sinopse, genero, imagemSelecionada, licenca, precoBilhete);
                janelaFilmes.adicionarFilme(novo);
                JOptionPane.showMessageDialog(this, "Filme adicionado com sucesso!");
            }

            janelaFilmes.guardarFilmesCSV();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Insere números válidos para duração, licença e bilhete.");
        }
    }
}
