import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JanelaMostrarSessoes extends JFrame {
    private DefaultListModel<String> modeloLista;
    private JList<String> listaSessoes;
    private JTextArea detalhesFilme;

    private List<Sessao> sessoesFiltradas;
    private List<Filme> filmes;
    private List<Sala> salas;

    private static final String FICHEIRO_SESSOES = "src/main/java/csv/sessoes.csv";
    private static final String FICHEIRO_FILMES = "src/main/java/csv/Filmes.csv";
    private static final String FICHEIRO_SALAS = "src/main/java/csv/salas.csv";

    public JanelaMostrarSessoes() {
        setTitle("Sessões do Dia Atual");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        filmes = carregarFilmesDeCSV();
        salas = carregarSalasDeCSV();
        List<Sessao> sessoes = carregarSessoesDeCSV();

        sessoesFiltradas = new ArrayList<>();

        modeloLista = new DefaultListModel<>();
        listaSessoes = new JList<>(modeloLista);
        JScrollPane scrollSessoes = new JScrollPane(listaSessoes);

        detalhesFilme = new JTextArea();
        detalhesFilme.setEditable(false);
        detalhesFilme.setLineWrap(true);
        detalhesFilme.setWrapStyleWord(true);
        JScrollPane scrollDetalhes = new JScrollPane(detalhesFilme);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollSessoes, scrollDetalhes);
        splitPane.setDividerLocation(250);
        add(splitPane);

        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        for (Sessao s : sessoes) {
            if (s.getData().equals(dataAtual)) {
                sessoesFiltradas.add(s);
                modeloLista.addElement(s.getHoraInicio() + " → " + s.getHoraFim()
                        + " | " + s.getFilme().getTitulo()
                        + " | Sala: " + s.getSala().getNome());
            }
        }

        listaSessoes.addListSelectionListener(e -> {
            int index = listaSessoes.getSelectedIndex();
            if (index != -1) {
                Sessao s = sessoesFiltradas.get(index);
                Filme f = s.getFilme();
                Sala sala = s.getSala();

                detalhesFilme.setText(
                        "🎬 Título: " + f.getTitulo() +
                                "\n⏱️ Duração: " + f.getDuracao() + " minutos" +
                                "\n🎭 Género: " + f.getGenero() +
                                "\n💸 Preço Licença: " + f.getPrecoLicenca() + " €" +
                                "\n🎟️ Preço Bilhete: " + f.getPrecoBilhete() + " €" +
                                "\n📅 Data: " + s.getData() +
                                "\n🕓 Início: " + s.getHoraInicio() +
                                "\n🕔 Fim: " + s.getHoraFim() +
                                "\n🏟️ Sala: " + sala.getNome() +
                                " (" + sala.getTipo() + ", " + sala.getSom() + ")" +
                                "\n\n📖 Sinopse:\n" + f.getSinopse()
                );
            }
        });

        setVisible(true);
    }

    private List<Filme> carregarFilmesDeCSV() {
        List<Filme> lista = new ArrayList<>();
        File ficheiro = new File(FICHEIRO_FILMES);
        if (!ficheiro.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 7) {
                    String titulo = partes[0];
                    int duracao = Integer.parseInt(partes[1]);
                    String sinopse = partes[2];
                    String genero = partes[3];
                    String imagem = partes[4];
                    double licenca = Double.parseDouble(partes[5]);
                    double bilhete = Double.parseDouble(partes[6]);

                    lista.add(new Filme(titulo, duracao, sinopse, genero, imagem, licenca, bilhete));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de filmes.");
        }

        return lista;
    }

    private List<Sala> carregarSalasDeCSV() {
        List<Sala> lista = new ArrayList<>();
        File ficheiro = new File(FICHEIRO_SALAS);
        if (!ficheiro.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length >= 7) {
                    String nome = partes[0];
                    String tipo = partes[1];
                    String layout = partes[2].split("x")[0];
                    String som = partes[3];
                    boolean acessivel = partes[4].equals("1");
                    boolean ativa = partes[5].equals("1");
                    double preco = Double.parseDouble(partes[6]);

                    lista.add(new Sala(nome, tipo, layout, som, acessivel, ativa, preco));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de salas.");
        }

        return lista;
    }

    private List<Sessao> carregarSessoesDeCSV() {
        List<Sessao> lista = new ArrayList<>();
        File ficheiro = new File(FICHEIRO_SESSOES);
        if (!ficheiro.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Sessao s = Sessao.fromCSV(linha, filmes, salas);
                if (s != null) lista.add(s);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de sessões.");
        }

        return lista;
    }
}
