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
    private static final String FICHEIRO_SESSOES = "sessoes.csv";
    private static final String FICHEIRO_FILMES = "src/main/java/csv/Filmes.csv";

    public JanelaMostrarSessoes() {
        setTitle("Sessões do Dia Atual");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        filmes = carregarFilmesDeCSV();
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

        // Data atual real
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Preencher a lista com sessões do dia atual
        for (Sessao s : sessoes) {
            if (s.getData().equals(dataAtual)) {
                sessoesFiltradas.add(s);
                modeloLista.addElement(s.getHoraInicio() + " → " + s.getHoraFim() + " | " + s.getFilme().getTitulo());
            }
        }

        // Mostrar detalhes ao selecionar
        listaSessoes.addListSelectionListener(e -> {
            int index = listaSessoes.getSelectedIndex();
            if (index != -1) {
                Sessao s = sessoesFiltradas.get(index);
                Filme f = s.getFilme();
                detalhesFilme.setText(
                        "🎬 Título: " + f.getTitulo() +
                                "\n⏱️ Duração: " + f.getDuracao() + " minutos" +
                                "\n🎭 Género: " + f.getGenero() +
                                "\n📅 Data: " + s.getData() +
                                "\n🕓 Início: " + s.getHoraInicio() +
                                "\n🕔 Fim: " + s.getHoraFim() +
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
                if (partes.length == 5) {
                    String titulo = partes[0];
                    int duracao = Integer.parseInt(partes[1]);
                    String sinopse = partes[2];
                    String genero = partes[3];
                    String imagem = partes[4];
                    lista.add(new Filme(titulo, duracao, sinopse, genero, imagem));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de filmes.");
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
                Sessao s = Sessao.fromCSV(linha, filmes);
                if (s != null) lista.add(s);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de sessões.");
        }
        return lista;
    }
}
