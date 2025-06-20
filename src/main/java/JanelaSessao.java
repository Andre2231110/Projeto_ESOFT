import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaSessao extends JFrame {
    private JPanel contentPane;
    private JList<String> listaFilmes;
    private DefaultListModel<String> modeloFilmes;
    private List<Filme> filmes;

    private JList<String> listaSessoes;
    private DefaultListModel<String> modeloSessoes;
    private List<Sessao> sessoes;

    private JTextField txtData;
    private static final String FICHEIRO_FILMES = "src/main/java/csv/Filmes.csv";
    private static final String FICHEIRO_SESSOES = "sessoes.csv";

    public JanelaSessao() {
        this.filmes = carregarFilmesDeCSV();
        this.sessoes = new ArrayList<>();

        setTitle("Sessões");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Lista de filmes
        modeloFilmes = new DefaultListModel<>();
        for (Filme f : filmes) modeloFilmes.addElement(f.getTitulo());

        listaFilmes = new JList<>(modeloFilmes);
        listaFilmes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollFilmes = new JScrollPane(listaFilmes);
        scrollFilmes.setPreferredSize(new Dimension(250, 0));

        // Lista de sessões
        modeloSessoes = new DefaultListModel<>();
        listaSessoes = new JList<>(modeloSessoes);
        JScrollPane scrollSessoes = new JScrollPane(listaSessoes);

        // Painel direito
        JPanel painelDireito = new JPanel(new BorderLayout());
        JPanel painelTopo = new JPanel();
        JButton btnVoltar = new JButton("Voltar");
        painelTopo.add(btnVoltar);
        btnVoltar.addActionListener(e -> {
            new JanelaPrincipal(""); // substitui se necessário
            dispose();
        });

        txtData = new JTextField("dd/mm/yyyy", 10);
        painelTopo.add(new JLabel("Hoje"));
        painelTopo.add(txtData);
        painelDireito.add(painelTopo, BorderLayout.NORTH);
        painelDireito.add(scrollSessoes, BorderLayout.CENTER);

        // Botões inferiores
        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("+");
        JButton btnEditar = new JButton("✎");
        JButton btnRemover = new JButton("Remover");
        JButton btnDisponiveis = new JButton("Sessões Disponíveis");

        btnDisponiveis.addActionListener(e -> {
            new JanelaMostrarSessoes(); // Abre a nova janela
        });

        btnRemover.setForeground(Color.WHITE);
        btnRemover.setBackground(Color.RED);
        btnRemover.addActionListener(e -> {
            int indexFilme = listaFilmes.getSelectedIndex();
            int indexSessao = listaSessoes.getSelectedIndex();

            if (indexFilme == -1 || indexSessao == -1) {
                JOptionPane.showMessageDialog(this, "Seleciona um filme e uma sessão para remover.");
                return;
            }

            Filme filmeSelecionado = filmes.get(indexFilme);
            String dataSelecionada = txtData.getText().trim();

            List<Sessao> sessoesDoFilme = new ArrayList<>();
            for (Sessao s : sessoes) {
                if (s.getFilme().getTitulo().equalsIgnoreCase(filmeSelecionado.getTitulo())
                        && s.getData().equals(dataSelecionada)) {
                    sessoesDoFilme.add(s);
                }
            }

            Sessao sessaoSelecionada = sessoesDoFilme.get(indexSessao);
            JanelaRemoverSessao janelaRemover = new JanelaRemoverSessao(this, sessaoSelecionada);

            if (janelaRemover.foiConfirmado()) {
                sessoes.remove(sessaoSelecionada);
                guardarSessoesCSV();
                mostrarSessoesDoFilmeSelecionado();
                JOptionPane.showMessageDialog(this, "Sessão removida com sucesso.");
            }
        });


        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnDisponiveis);
        painelDireito.add(painelBotoes, BorderLayout.SOUTH);

        contentPane.add(scrollFilmes, BorderLayout.WEST);
        contentPane.add(painelDireito, BorderLayout.CENTER);

        // Listeners
        listaFilmes.addListSelectionListener(e -> mostrarSessoesDoFilmeSelecionado());

        txtData.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mostrarSessoesDoFilmeSelecionado(); }
            public void removeUpdate(DocumentEvent e) { mostrarSessoesDoFilmeSelecionado(); }
            public void changedUpdate(DocumentEvent e) { mostrarSessoesDoFilmeSelecionado(); }
        });

        // Adicionar Sessão
        btnAdicionar.addActionListener(e -> {
            int index = listaFilmes.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Seleciona um filme primeiro.");
                return;
            }
            Filme filmeSelecionado = filmes.get(index);
            JanelaAdicionarSessao janela = new JanelaAdicionarSessao(this, filmeSelecionado);
            Sessao nova = janela.getSessaoCriada();
            if (nova != null) {
                sessoes.add(nova);
                guardarSessoesCSV();
                mostrarSessoesDoFilmeSelecionado();
            }
        });

        // Editar Sessão
        btnEditar.addActionListener(e -> {
            int indexFilme = listaFilmes.getSelectedIndex();
            int indexSessao = listaSessoes.getSelectedIndex();

            if (indexFilme == -1 || indexSessao == -1) {
                JOptionPane.showMessageDialog(this, "Seleciona um filme e uma sessão para editar.");
                return;
            }

            Filme filmeSelecionado = filmes.get(indexFilme);
            String dataSelecionada = txtData.getText().trim();

            List<Sessao> sessoesDoFilme = new ArrayList<>();
            for (Sessao s : sessoes) {
                if (s.getFilme().getTitulo().equalsIgnoreCase(filmeSelecionado.getTitulo()) &&
                        s.getData().equals(dataSelecionada)) {
                    sessoesDoFilme.add(s);
                }
            }

            Sessao sessaoSelecionada = sessoesDoFilme.get(indexSessao);
            JanelaEditarSessao janelaEditar = new JanelaEditarSessao(this, sessaoSelecionada);
            Sessao sessaoEditada = janelaEditar.getSessaoEditada();

            if (sessaoEditada != null) {
                sessoes.remove(sessaoSelecionada);
                sessoes.add(sessaoEditada);
                guardarSessoesCSV();
                mostrarSessoesDoFilmeSelecionado();
            }
        });

        carregarSessoesDeCSV();
        setVisible(true);
    }

    private void mostrarSessoesDoFilmeSelecionado() {
        modeloSessoes.clear();
        int index = listaFilmes.getSelectedIndex();
        if (index == -1) return;

        String dataSelecionada = txtData.getText().trim();
        Filme filmeSelecionado = filmes.get(index);

        for (Sessao s : sessoes) {
            if (s.getFilme().getTitulo().equalsIgnoreCase(filmeSelecionado.getTitulo())
                    && s.getData().equals(dataSelecionada)) {
                modeloSessoes.addElement(s.getHoraInicio() + " → " + s.getHoraFim());
            }
        }
    }

    private void carregarSessoesDeCSV() {
        File ficheiro = new File(FICHEIRO_SESSOES);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Sessao s = Sessao.fromCSV(linha, filmes);
                if (s != null) {
                    sessoes.add(s);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de sessões.");
        }
    }

    private void guardarSessoesCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_SESSOES))) {
            for (Sessao s : sessoes) {
                pw.println(s.toCSV());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar sessões.");
        }
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
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o ficheiro de filmes.");
        }

        return lista;
    }
}
