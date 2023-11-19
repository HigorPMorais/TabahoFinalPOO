package controle;

import java.time.LocalDate;
import java.util.HashMap;
import javax.swing.JOptionPane;
import modelo.Aluno;
import modelo.Curso;
import visao.TelaCadastroPessoa;

public class ControleAluno extends ControlePessoa<Aluno> {

    private ControleCurso controleCurso;
    
    public ControleAluno(ControleCurso controleCurso) {
        super(Aluno.class);
        this.controleCurso = controleCurso;
        setTelaCadastro(new TelaCadastroPessoa(this));
    }
    



    public ControleCurso getControleCurso() {
        return controleCurso;
    }

    public void setarDadosObjeto(Aluno al, HashMap<String, Object> dados) {
        try {
            super.setarDadosObjeto(al, dados);
           
            //al.setCurso(controleCurso.getCursoSelecionado(indexCurso));
            
            al.setRa((String) dados.getOrDefault("ra", ""));
            al.setCurso((Curso) dados.getOrDefault("curso", null));
            al.setDataMatricula((LocalDate) dados.getOrDefault("datamatricula", null));
            al.setSituacao((String) dados.getOrDefault("situacao", ""));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao Setar Dados do Aluno!\n"+e.getMessage(), "Falha ao Setar Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HashMap<String, Object> getDadosObjeto(Aluno al) {
        HashMap<String, Object> dados = super.getDadosObjeto(al);
        dados.put("ra", al.getRa());
        dados.put("curso", al.getCurso());
        dados.put("datamatricula", al.getDataMatricula());
        dados.put("situacao", al.getSituacao());
        
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
    public void editar(HashMap<String, Object> dados) {
        if (registroSelecionado != null) {
            setarDadosObjeto(registroSelecionado, dados);
        }
    }

    @Override
    public void salvar(HashMap<String, Object> dados) {
        Aluno al = new Aluno();
        setarDadosObjeto(al, dados);
        registros.add(al);
    }
    
   
}
