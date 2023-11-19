package controle;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import modelo.Curso;
import modelo.Docente;
import visao.TelaCadastroCurso;

/**
 *
 * @author Andre
 */
public class ControleCurso extends ControleCadastroGenerico<Curso>{
    
    private ControleDocente controleDocente;
    
    public ControleCurso(ControleDocente controleDocente) {
        super(Curso.class);
        this.controleDocente = controleDocente;
        setTelaCadastro(new TelaCadastroCurso(this));
    }

    public ControleDocente getControleDocente() {
        return controleDocente;
    }
    
    public void setarDadosObjeto(Curso curso, HashMap<String, Object> dados){
        if(curso == null){
            JOptionPane.showMessageDialog(null, "Falha ao Setar Dados!", "Falha ao Setar Dados", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        curso.setNome((String) dados.getOrDefault("curso", ""));
        curso.setCargaHoraria((int) dados.getOrDefault("cargahoraria", 0));
        curso.setQtdSemestres((int) dados.getOrDefault("semestres", 0));
        curso.setCoordenador((Docente) dados.getOrDefault("coordenador", null));
    }
    
    public HashMap<String, Object> gerarVetorDados(Curso curso){
        HashMap<String, Object> dados = new HashMap<>();        
        dados.put("curso", curso.getNome());
        dados.put("cargahoraria", curso.getCargaHoraria());
        dados.put("semestres", curso.getQtdSemestres());
        dados.put("coordenador", curso.getCoordenador());
        
        return dados;
    }
    
    public List<String> getNomesCursos(){
        return registros.stream().map(x -> x.getNome()).collect(Collectors.toList()); 
    }
    
    public Curso getCursoSelecionado(int index) {
        if (index >= 0 && index < registros.size()) {
            return registros.get(index);
        }
        return null;
    }
    
    @Override
    public void abrirTelaCadastroParaEdicao(int index) {
        registroSelecionado = getCursoSelecionado(index);
        if (registroSelecionado == null) {
            JOptionPane.showMessageDialog(null, "Falha ao Editar \nRegistro não encontrado!", "Falha ao Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        telaCadastro.setarDadosTela(gerarVetorDados(registroSelecionado));
        telaCadastro.setEditarDados(true);
        telaCadastro.setVisible(true);
    }
    
    
    @Override
    public void editar(HashMap<String, Object> dados){
        if(registroSelecionado != null){
            setarDadosObjeto(registroSelecionado, dados);
        }
    }

    @Override
    public void salvar(HashMap<String, Object> dados) {
        Curso curso = new Curso();
        setarDadosObjeto(curso, dados);
        registros.add(curso);
    }
}
