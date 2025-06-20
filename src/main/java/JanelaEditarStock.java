
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import  GestaoBar.Produto;

public class JanelaEditarStock extends JDialog {

    private JTextField txtStock;
    private JTextField txtValidade;
    private JTextField txtLote;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Produto produto;
    private static final String CSV_FILE = "produtos.csv";

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
                // Soft delete automático se tudo estiver vazio ou a zero
                String stockInput = txtStock.getText().trim();
                String validadeInput = txtValidade.getText().trim();
                String loteInput = txtLote.getText().trim();

                if ((stockInput.isEmpty() || stockInput.equals("0")) &&
                        validadeInput.isEmpty() &&
                        loteInput.isEmpty()) {
                    produto.stock = 0;
                    produto.validade = null;
                    produto.lote = "—";
                } else {
                    int novoStock = Integer.parseInt(stockInput);
                    if (novoStock < 0) throw new NumberFormatException();

                    LocalDate novaValidade = validadeInput.isEmpty() ? null : LocalDate.parse(validadeInput);
                    String novoLote = loteInput.isEmpty() ? "—" : loteInput;

                    produto.stock = novoStock;
                    produto.validade = novaValidade;
                    produto.lote = novoLote;
                }

                guardarAlteracoesCSV(produto);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock inválido. Introduz um número inteiro positivo.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Usa o formato aaaa-mm-dd (ex: 2025-12-31).");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao guardar no ficheiro CSV.");
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarAlteracoesCSV(Produto editado) throws IOException {
        File ficheiro = new File(CSV_FILE);
        java.util.List<Produto> produtos = new ArrayList<>();

        // Recarrega todos os produtos do CSV
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length >= 7) {
                    Produto p = new Produto(
                            partes[0],
                            partes[1],
                            Double.parseDouble(partes[2]),
                            (int) Double.parseDouble(partes[3])
                    );
                    p.stock = Integer.parseInt(partes[4]);
                    p.lote = partes[5];
                    p.validade = partes[6].isEmpty() ? null : LocalDate.parse(partes[6]);

                    if (p.nome.equals(editado.nome)) {
                        produtos.add(editado); // substitui o antigo
                    } else {
                        produtos.add(p);
                    }
                }
            }
        }

        // Regrava o CSV com as alterações
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Produto p : produtos) {
                pw.println(p.nome + "," + p.categoria + "," + p.preco + "," + p.desconto + "," +
                        p.stock + "," + p.lote + "," + (p.validade != null ? p.validade : ""));
            }
        }
    }
}
