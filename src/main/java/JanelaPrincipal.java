import javax.swing.*;
import java.awt.*;


public class JanelaPrincipal  extends JFrame{
    private JPanel painelTopo;
    private JLabel lblUser;
    private JButton btnVendaBilhetes;
    private JButton btnSalas;
    private JButton btnFilmes;
    private JButton btnSessoes;
    private JButton btnBar;
    private JButton btnCampanhas;
    private JButton btnFuncionarios;
    private JButton btnConsultas;
    private JPanel contentPane;

    public JanelaPrincipal(String nomeUser) {
        setTitle("Menu Principal");
        setContentPane(contentPane);
        setIconeNoBotao(btnVendaBilhetes, "/gestaoVendaBilhetes.png");
        setIconeNoBotao(btnSessoes, "/gestaoSessoes.png");
        setIconeNoBotao(btnSalas, "/gestaoSalas.png");
        setIconeNoBotao(btnBar, "/gestaoBar.png");
        setIconeNoBotao(btnFilmes, "/gestaoFilmes.png");
        setIconeNoBotao(btnCampanhas, "/gestaoCampanhas.png");
        setIconeNoBotao(btnFuncionarios, "/gestaoFuncionarios.png");
        setIconeNoBotao(btnConsultas, "/moduloConsultas.png");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        lblUser.setText(nomeUser);

        btnFilmes.addActionListener(e -> {
            new JanelaFilmes();
            setVisible(false);
        });

        // Os outros botões ainda não fazem nada
        btnVendaBilhetes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));
        btnSessoes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));
        btnSalas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));
        btnBar.addActionListener(e -> {
            new JanelaBar(lblUser.getText());
            dispose();
        });
        btnCampanhas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));
        btnFuncionarios.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));
        btnConsultas.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ainda por implementar"));

        setVisible(true);
    }

    private void setIconeNoBotao(JButton botao, String caminho) {
        ImageIcon icon = new ImageIcon(getClass().getResource(caminho));
        Image imagemRedimensionada = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(imagemRedimensionada));
        botao.setFont(new Font("Arial", Font.BOLD, 14));
    }

}
