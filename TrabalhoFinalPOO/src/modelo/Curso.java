package modelo;

public class Curso implements IDescricao{
    protected Integer idCurso;
    protected String nome;
    protected int cargaHoraria;
    protected int qtdSemestres;
    protected Docente coordenador;
    
    //variável auxiliar 
    private int qtdAlunosCurso;

    public Curso() {
    }

    public Curso(Integer idCurso, String nome, int cargaHoraria, int qtdSemestres, Docente coordenador, int qtdAlunosCurso) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.qtdSemestres = qtdSemestres;
        this.coordenador = coordenador;
        this.qtdAlunosCurso = qtdAlunosCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getQtdSemestres() {
        return qtdSemestres;
    }

    public void setQtdSemestres(int qtdSemestres) {
        this.qtdSemestres = qtdSemestres;
    }

    public Docente getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Docente coordenador) {
        this.coordenador = coordenador;
    }

    public int getQtdAlunosCurso() {
        return qtdAlunosCurso;
    }

    public void setQtdAlunosCurso(int qtdAlunosCurso) {
        this.qtdAlunosCurso = qtdAlunosCurso;
    }
    
    public void exibirInformacoes(){
        System.out.println("ID: "+ idCurso + "Curso: " + nome + " | Carga horária: " + cargaHoraria + " | Quantidade semestre: " + qtdSemestres + " | Coordenador: " + coordenador.getNome() + " | Quantidade alunos: " + qtdAlunosCurso);
    }
    
    @Override
    /**
     * Retorna a propriedade que melhor descreve/representa o objeto 
     */
    public String getDescricao() {
        return nome;
    }
}
