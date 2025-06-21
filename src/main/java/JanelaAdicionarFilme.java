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
    private JTextField txtLicenca;
    private JTextField txtBilhete;
    private JLabel lblPreviewImagem;
    private JButton adicionarBtn;
    private JLabel lblTitulo;
    private JLabel lblDuracao;
    private JLabel lblGenero;
    private JLabel lblLicenca;
    private JLabel lblSinopse;
    private JLabel lblBilhete;
    private JPanel painelImagem;


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
        txtLicenca.setText(String.valueOf(filmeAEditar.getPrecoLicenca()));
        txtBilhete.setText(String.valueOf(filmeAEditar.getPrecoBilhete()));

        imagemSelecionada = filmeAEditar.getImagem();
        ImageIcon icon = new ImageIcon(imagemSelecionada);
        Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
        lblPreviewImagem.setIcon(new ImageIcon(img));
        lblPreviewImagem.setText("");

        adicionarBtn.setText("Guardar");
        setVisible(true);
    }

    private void initJanela(JFrame parent) {
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(30, 30, 100, 25);
        add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(150, 30, 400, 25);
        add(txtTitulo);

        JLabel lblDuracao = new JLabel("Duração (min):");
        lblDuracao.setBounds(30, 70, 100, 25);
        add(lblDuracao);
        txtDuracao = new JTextField();
        txtDuracao.setBounds(150, 70, 100, 25);
        add(txtDuracao);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(30, 110, 100, 25);
        add(lblGenero);
        txtGenero = new JTextField();
        txtGenero.setBounds(150, 110, 200, 25);
        add(txtGenero);

        JLabel lblSinopse = new JLabel("Sinopse:");
        lblSinopse.setBounds(30, 150, 100, 25);
        add(lblSinopse);
        txtSinopse = new JTextArea();
        txtSinopse.setLineWrap(true);
        txtSinopse.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtSinopse);
        scroll.setBounds(150, 150, 400, 60);
        add(scroll);

        JLabel lblLicenca = new JLabel("Preço Licença (€):");
        lblLicenca.setBounds(30, 230, 120, 25);
        add(lblLicenca);
        txtLicenca = new JTextField();
        txtLicenca.setBounds(150, 230, 100, 25);
        add(txtLicenca);

        JLabel lblBilhete = new JLabel("Preço Bilhete (€):");
        lblBilhete.setBounds(30, 270, 120, 25);
        add(lblBilhete);
        txtBilhete = new JTextField();
        txtBilhete.setBounds(150, 270, 100, 25);
        add(txtBilhete);

        JLabel lblImagem = new JLabel("Imagem:");
        lblImagem.setBounds(30, 310, 100, 25);
        add(lblImagem);

        lblPreviewImagem = new JLabel("+", SwingConstants.CENTER);
        lblPreviewImagem.setBounds(150, 310, 120, 160);
        lblPreviewImagem.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewImagem.setOpaque(true);
        lblPreviewImagem.setBackground(new Color(216, 245, 209));
        add(lblPreviewImagem);

        lblPreviewImagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                escolherImagem();
            }
        });

        adicionarBtn = new JButton("Adicionar");
        adicionarBtn.setBounds(250, 490, 100, 30);
        adicionarBtn.addActionListener(e -> adicionarOuEditarFilme());
        add(adicionarBtn);
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
