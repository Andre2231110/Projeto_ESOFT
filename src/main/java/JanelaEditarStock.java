import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import GestaoBar.Produto;

public class JanelaEditarStock extends JDialog {

    private JTextField txtStock;
    private JTextField txtValidade;
    private JTextField txtLote;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Produto produto;
    private static final String CSV_FILE = "src/main/java/csv/produtos.csv";

    public JanelaEditarStock(JFrame parent, Produto produto) {
        super(parent, "Editar Stock", true);
        this.produto = produto;

        setSize(300, 250);
        setLocationRelativeTo(parent);
        setLayout(null);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(30, 30, 100, 25);
        add(lblStock);

        txtStock = new JTextField(String.valueOf(produto.getStock()));
        txtStock.setBounds(120, 30, 130, 25);
        add(txtStock);

        JLabel lblValidade = new JLabel("Validade:");
        lblValidade.setBounds(30, 70, 100, 25);
        add(lblValidade);

        txtValidade = new JTextField(
                produto.getValidade() != null ? produto.getValidade().toString() : ""
        );
        txtValidade.setBounds(120, 70, 130, 25);
        add(txtValidade);

        JLabel lblLote = new JLabel("Lote:");
        lblLote.setBounds(30, 110, 100, 25);
        add(lblLote);

        txtLote = new JTextField(produto.getLote());
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
                String stockInput = txtStock.getText().trim();
                String validadeInput = txtValidade.getText().trim();
                String loteInput = txtLote.getText().trim();

                if ((stockInput.isEmpty() || stockInput.equals("0")) &&
                        validadeInput.isEmpty() &&
                        loteInput.isEmpty()) {
                    produto.setStock(0);
                    produto.setValidade(null);
                    produto.setLote("—");
                } else {
                    int novoStock = Integer.parseInt(stockInput);
                    if (novoStock < 0) throw new NumberFormatException();

                    LocalDate novaValidade = validadeInput.isEmpty() ? null : LocalDate.parse(validadeInput);
                    String novoLote = loteInput.isEmpty() ? "—" : loteInput;

                    produto.setStock(novoStock);
                    produto.setValidade(novaValidade);
                    produto.setLote(novoLote);
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
        ArrayList<Produto> produtos = new ArrayList<>();

        // Recarrega os produtos existentes
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",", -1);
                if (partes.length >= 8) {
                    Produto p = new Produto(
                            partes[0], // nome
                            partes[1], // categoria
                            Double.parseDouble(partes[2]), // preco venda
                            Double.parseDouble(partes[3]), // preco compra
                            Integer.parseInt(partes[4])    // desconto
                    );
                    p.setStock(Integer.parseInt(partes[5]));
                    p.setLote(partes[6]);
                    p.setValidade(partes[7].isEmpty() ? null : LocalDate.parse(partes[7]));

                    if (p.getNome().equals(editado.getNome())) {
                        produtos.add(editado);
                    } else {
                        produtos.add(p);
                    }
                }
            }
        }

        // Regrava os produtos atualizados
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (Produto p : produtos) {
                pw.println(p.getNome() + "," + p.getCategoria() + "," + p.getPreco() + "," +
                        p.getPrecoCompra() + "," + p.getDesconto() + "," + p.getStock() + "," +
                        p.getLote() + "," + (p.getValidade() != null ? p.getValidade() : ""));
            }
        }
    }
}
