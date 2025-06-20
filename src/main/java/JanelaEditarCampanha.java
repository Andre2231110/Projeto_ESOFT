import javax.swing.*;

public class JanelaEditarCampanha extends JDialog {
    private JTextField txtPublico;
    private JTextField txtNome;
    private JTextField txtPromocao;
    private JTextField txtFilmes;
    private JTextField txtPreco;
    private JButton confirmarButton;
    private JButton cancelarButton;
    private JLabel lblNome;
    private JLabel lblPublico;
    private JLabel lblPromocao;
    private JLabel lblFilmes;
    private JLabel lblPreco;
    private JPanel contentPane;

    private JanelaCampanhas janelaCampanhas;
    private Campanhas campanhaOriginal;

    public JanelaEditarCampanha(JanelaCampanhas janelaCampanhas, Campanhas campanhaEditar) {
        super((JFrame) null, "Editar Campanha", true);
        this.janelaCampanhas = janelaCampanhas;
        this.campanhaOriginal = campanhaEditar;

        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(janelaCampanhas);

        if (campanhaOriginal != null) {
            txtNome.setText(campanhaOriginal.getNome());
            txtPublico.setText(campanhaOriginal.getPublicoAlvo());
            txtPromocao.setText(campanhaOriginal.getPromocao());
            txtFilmes.setText(campanhaOriginal.getFilmesAssociados());
            txtPreco.setText(String.valueOf(campanhaOriginal.getPreco()));
        }

        cancelarButton.addActionListener(e -> dispose());

        confirmarButton.addActionListener(e -> {
            if (!validarCampos()) return;

            String nome = txtNome.getText().trim();
            String publico = txtPublico.getText().trim();
            String promocao = txtPromocao.getText().trim();
            String filmes = txtFilmes.getText().trim();
            double preco;

            try {
                preco = Double.parseDouble(txtPreco.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O preço deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (campanhaOriginal != null) {
                campanhaOriginal.setNome(nome);
                campanhaOriginal.setPublicoAlvo(publico);
                campanhaOriginal.setPromocao(promocao);
                campanhaOriginal.setFilmesAssociados(filmes);
                campanhaOriginal.setPreco(preco);

                janelaCampanhas.atualizarLista();
                JOptionPane.showMessageDialog(this, "Campanha alterada com sucesso!");
            } else {
                Campanhas nova = new Campanhas(nome, publico, promocao, filmes, preco, true);
                janelaCampanhas.adicionarOuAtualizarCampanha(nova, true);
                JOptionPane.showMessageDialog(this, "Campanha adicionada com sucesso!");
            }

            dispose();
        });

        setVisible(true);
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()
                || txtPublico.getText().trim().isEmpty()
                || txtPromocao.getText().trim().isEmpty()
                || txtFilmes.getText().trim().isEmpty()
                || txtPreco.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatórios.",
                    "Campos em falta",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
