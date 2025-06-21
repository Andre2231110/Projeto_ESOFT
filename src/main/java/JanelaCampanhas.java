import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

public class JanelaCampanhas extends JFrame {
    private JPanel contentPane;
    private JPanel painelTopo;
    private JLabel lblUser;
    private JLabel lblTitulo;
    private JButton adicionarButton;
    private JButton backButton;

    private JPanel painelCampanhas;
    private JScrollPane scrollCampanhas;

    private ArrayList<Campanhas> listaCampanhas = new ArrayList<>();
    private String nomeUser;

    private static final String FICHEIRO_CSV = "src/main/java/csv/campanhas.csv";


    public JanelaCampanhas(String nomeUser) {
        this.nomeUser = nomeUser;

        setTitle("Gestão de Campanhas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setContentPane(contentPane);
        painelCampanhas.setLayout(new BoxLayout(painelCampanhas, BoxLayout.Y_AXIS));
        lblUser.setText(nomeUser);

        backButton.addActionListener(e -> {
            new JanelaPrincipal(nomeUser);
            dispose();
        });

        adicionarButton.addActionListener(e -> {
            new JanelaEditarCampanha(this, null);
        });


        setVisible(true);
        carregarCampanhasCSV();
        atualizarLista();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarCampanhasCSV();
            }
        });

    }

    public void atualizarLista() {
        painelCampanhas.removeAll();

        for (Campanhas c : listaCampanhas) {
            painelCampanhas.add(criarCardCampanha(c));
            painelCampanhas.add(Box.createRigidArea(new Dimension(0, 10)));
        }


        painelCampanhas.revalidate();
        painelCampanhas.repaint();
    }

    private JPanel criarCardCampanha(Campanhas campanha) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(new Color(220, 255, 220));
        card.setMaximumSize(new Dimension(800, 120));

        JLabel lblNome = new JLabel(campanha.getNome());
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblDetalhes = new JLabel("<html><b>Público Alvo:</b> " + campanha.getPublicoAlvo()
                + "<br><b>Promoção:</b> " + campanha.getPromocao()
                + "<br><b>Filmes publicitados:</b> " + campanha.getFilmesAssociados()
                + "<br><b>Preço:</b> " + campanha.getPreco() + " €</html>");

        JPanel botoes = new JPanel(new GridLayout(3, 1, 5, 5));

        JButton btnEstado = new JButton(campanha.isAtiva() ? "Ativo" : "Inativo");
        btnEstado.setBackground(campanha.isAtiva() ? Color.GREEN : Color.LIGHT_GRAY);
        btnEstado.addActionListener(e -> {
            campanha.setAtiva(!campanha.isAtiva());
            JOptionPane.showMessageDialog(this,
                    campanha.isAtiva() ? "Campanha ativada com sucesso!" : "Campanha desativada com sucesso!");
            atualizarLista();
            guardarCampanhasCSV();
        });

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(202, 229, 189));
        btnEditar.addActionListener(e -> {
            new JanelaEditarCampanha(this, campanha);
        });

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(Color.RED);
        btnRemover.setForeground(Color.WHITE);
        btnRemover.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this,
                    "Deseja remover esta campanha?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                listaCampanhas.remove(campanha);
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Campanha eliminada com sucesso.");
                guardarCampanhasCSV();
            }
        });

        botoes.add(btnEstado);
        botoes.add(btnEditar);
        botoes.add(btnRemover);

        JPanel painelNome = new JPanel(new BorderLayout());
        painelNome.setBackground(new Color(220, 255, 220));
        painelNome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        painelNome.add(lblNome, BorderLayout.NORTH);
        card.add(painelNome, BorderLayout.WEST);
        card.add(lblDetalhes, BorderLayout.CENTER);
        card.add(botoes, BorderLayout.EAST);

        return card;
    }

    public void adicionarOuAtualizarCampanha(Campanhas campanha, boolean nova) {
        if (nova) {
            for (Campanhas c : listaCampanhas) {
                if (c.getNome().equalsIgnoreCase(campanha.getNome())) {
                    JOptionPane.showMessageDialog(this, "Essa campanha já existe!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            listaCampanhas.add(campanha);
        } else {
            for (int i = 0; i < listaCampanhas.size(); i++) {
                if (listaCampanhas.get(i).getNome().equals(campanha.getNome())) {
                    listaCampanhas.set(i, campanha);
                    break;
                }
            }
        }

        atualizarLista();
        guardarCampanhasCSV();
    }



    private void guardarCampanhasCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FICHEIRO_CSV))) {
            for (Campanhas c : listaCampanhas) {
                writer.println(c.getNome() + "," +
                        c.getPublicoAlvo() + "," +
                        c.getPromocao() + "," +
                        c.getFilmesAssociados() + "," +
                        c.getPreco() + "," +
                        c.isAtiva());
            }
        } catch (IOException e) {
            System.err.println("Erro ao guardar campanhas: " + e.getMessage());
        }
    }

    private void carregarCampanhasCSV() {
        File file = new File(FICHEIRO_CSV);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 6) {
                    String nome = partes[0];
                    String publico = partes[1];
                    String promocao = partes[2];
                    String filmes = partes[3];
                    double preco = Double.parseDouble(partes[4]);
                    boolean ativa = Boolean.parseBoolean(partes[5]);

                    listaCampanhas.add(new Campanhas(nome, publico, promocao, filmes, preco, ativa));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar campanhas: " + e.getMessage());
        }
    }

}
