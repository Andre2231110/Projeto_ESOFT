import javax.swing.*;
import java.awt.*;

public class JanelaRemoverSessao extends JDialog {
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private boolean confirmado = false;

    public JanelaRemoverSessao(JFrame parent, Sessao sessao) {
        super(parent, "Remover Sessão", true);
        setSize(400, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblMensagem = new JLabel("<html>Tem a certeza que deseja remover a sessão?<br><br>"
                + "<strong>" + sessao.getFilme().getTitulo() + "</strong> em "
                + sessao.getData() + " às " + sessao.getHoraInicio() + "</html>");
        lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblMensagem, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnConfirmar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    public boolean foiConfirmado() {
        return confirmado;
    }
}
