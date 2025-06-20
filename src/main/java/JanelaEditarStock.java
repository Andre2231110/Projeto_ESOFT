import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import GestaoBar.Produto;
public class JanelaEditarStock extends JDialog {

    private JTextField txtStock;
    private JTextField txtValidade;
    private JTextField txtLote;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Produto produto;

    public JanelaEditarStock(JFrame parent, Produto produto) {
        super(parent, "Editar Stock", true);
        this.produto = produto;

        setSize(300, 250);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(30, 30, 100, 25);
        add(lblStock);

        txtStock = new JTextField(String.valueOf(produto.stock));
        txtStock.setBounds(120, 30, 130, 25);
        add(txtStock);

        JLabel lblValidade = new JLabel("Validade:");
        lblValidade.setBounds(30, 70, 100, 25);
        add(lblValidade);

        txtValidade = new JTextField(
                produto.validade != null ? produto.validade.toString() : ""
        );
        txtValidade.setBounds(120, 70, 130, 25);
        add(txtValidade);


        JLabel lblLote = new JLabel("Lote:");
        lblLote.setBounds(30, 110, 100, 25);
        add(lblLote);

        txtLote = new JTextField(produto.lote);
        txtLote.setBounds(120, 110, 130, 25);
        add(txtLote);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(40, 160, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(150, 160, 100, 30);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                int novoStock = Integer.parseInt(txtStock.getText());
                if (novoStock < 0) throw new NumberFormatException();

                LocalDate novaValidade = LocalDate.parse(txtValidade.getText());
                String novoLote = txtLote.getText();

                produto.stock = novoStock;
                produto.validade = novaValidade;
                produto.lote = novoLote;

                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock inválido. Introduz um número inteiro positivo.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Usa o formato aaaa-mm-dd (ex: 2025-12-31).");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
