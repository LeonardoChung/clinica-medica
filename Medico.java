import java.util.ArrayList;
import java.util.List;

public class Medico {
    private String nome;
    private int codigo;
    private List<Paciente> pacientes;

    // metodo construtor
    public Medico(String nome, int codigo) {
        this.nome = nome;
        this.codigo = codigo;
        this.pacientes = new ArrayList<>();
    }


    // metodos getters
    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }


    // adicionar na lista
    public void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);

    }

}
