package controle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import modelo.Docente;
import visao.TelaCadastroPessoa;

/**
 *
 * @author Andre
 */
public class ControleDocente extends ControleFuncionario{
    
    public ControleDocente() {
        super(Docente.class);
        setTelaCadastro(new TelaCadastroPessoa(this));
    }
    
    public List<Docente> getListaDocentes(){
        return registros.stream().filter(x -> x.getClass().equals(Docente.class)).map(a -> (Docente) a).collect(Collectors.toList());
    }
    public List<String> getNomesDocentes(){
        return registros.stream().filter(x -> x.getClass().equals(Docente.class)).map(x -> x.getNome()).collect(Collectors.toList()); 
    }
    
    public Docente getDocenteSelecionado(int index) {
        List<Docente> docentes = getListaDocentes();
        if (index >= 0 && index < docentes.size()) {
            return docentes.get(index);
        }
        return null;
    }
    
   public void setarDadosObjeto(Docente docente, HashMap<String, Object> dados) {
        try {
            super.setarDadosObjeto(docente, dados);
            docente.setFormacao((String) dados.getOrDefault("formacao", ""));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao Setar Dados do Docente!\n"+e.getMessage(), "Falha ao Setar Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HashMap<String, Object> getDadosObjeto(Docente docente) {
        HashMap<String, Object> dados = super.getDadosObjeto(docente);
        dados.put("formacao", docente.getFormacao());
        
        return dados;
    }

   @Override
    public void abrirTelaCadastroParaEdicao(int index) {
        registroSelecionado = registros.get(index);
        if (registroSelecionado == null) {
            JOptionPane.showMessageDialog(null, "Falha ao Editar \nRegistro não encontrado!", "Falha ao Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        telaCadastro.setarDadosTela(getDadosObjeto(registroSelecionado));
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
        Docente docente = new Docente();
        setarDadosObjeto(docente, dados);
        registros.add(docente);
    }
    
}
