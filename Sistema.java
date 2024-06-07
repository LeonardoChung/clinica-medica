import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.io.*;
import java.util.*;

public class Sistema {
    private List<Medico> medicos = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Consulta> consultas = new ArrayList<>();

    public void carregarMedicos(String caminho) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(caminho));
        scanner.useDelimiter(";");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] campos = linha.split(";");
            String nome = campos[0];
            int codigo = Integer.parseInt(campos[1]);
            Medico medico = new Medico(nome, codigo);
            medicos.add(medico);
        }
        scanner.close();
    }

    public void carregarPacientes(String caminho) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(caminho));
        scanner.useDelimiter(";");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] campos = linha.split(";");
            String nome = campos[0];
            String cpf = campos[1];
            Paciente paciente = new Paciente(nome, cpf);
            pacientes.add(paciente);
        }
        scanner.close();
    }

    public void carregarConsultas(String caminho) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(caminho));
        scanner.useDelimiter(";");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] campos = linha.split(";");
            LocalDate data = LocalDate.parse(campos[0], dateFormatter);
            LocalTime horario = LocalTime.parse(campos[1]);
            int codigoMedico = Integer.parseInt(campos[2]);
            String cpfPaciente = campos[3];
            Medico medico = medicos.stream().filter(m -> m.getCodigo() == codigoMedico).findFirst().orElse(null);
            Paciente paciente = pacientes.stream().filter(p -> p.getCpf().equals(cpfPaciente)).findFirst().orElse(null);
            if (medico != null && paciente != null) {
                Consulta consulta = new Consulta(data, horario, medico, paciente);
                medico.adicionarPaciente(paciente);
                paciente.adicionarConsulta(consulta);
                consultas.add(consulta);
            }
        }
        scanner.close();
    }

    public void salvarConsultaNoCSV(Consulta consulta, String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String linha = String.format("%s;%s;%d;%s\n",
                    consulta.getData().format(dateFormatter),
                    consulta.getHorario().toString(),
                    consulta.getMedico().getCodigo(),
                    consulta.getPaciente().getCpf());
            writer.write(linha);
        }
    }

    public void salvarMedicoNoCSV(Medico medico, String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
            String linha = String.format("%s;%d\n", medico.getNome(), medico.getCodigo());
            writer.write(linha);
        }
    }

    public void salvarPacienteNoCSV(Paciente paciente, String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
            String linha = String.format("%s;%s\n", paciente.getNome(), paciente.getCpf());
            writer.write(linha);
        }
    }

    public void agendarConsulta(LocalDate data, LocalTime horario, Medico medico, Paciente paciente) throws ConsultaInvalidaException, IOException {
        if (paciente == null || medico == null) {
            throw new ConsultaInvalidaException("Paciente ou Médico inválido para agendamento de consulta.");
        }
        Consulta consulta = new Consulta(data, horario, medico, paciente);
        consultas.add(consulta);
        medico.adicionarPaciente(paciente);
        paciente.adicionarConsulta(consulta);
        salvarConsultaNoCSV(consulta, "src/consultas.csv");
    }

    public void registrarMedico(String nome, Integer codigo) throws MedicoInvalidoException, IOException {
        if (nome == null || codigo == null) {
            throw new MedicoInvalidoException("Nome ou codigo do médico inválido.");
        }
        Medico medico = new Medico(nome, codigo);
        medicos.add(medico);
        salvarMedicoNoCSV(medico, "src/medicos.csv");
    }

    public void registrarPaciente(String nome, String cpf) throws IOException {
        Paciente paciente = new Paciente(nome, cpf);
        pacientes.add(paciente);
        salvarPacienteNoCSV(paciente, "src/pacientes.csv");
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Paciente> getPacientesDoMedico(int codigoMedico) {
        Medico medico = medicos.stream().filter(m -> m.getCodigo() == codigoMedico).findFirst().orElse(null);
        if (medico != null) {
            return medico.getPacientes();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Consulta> getConsultasDoMedicoEmPeriodo(int codigoMedico, LocalDate dataInicial, LocalDate dataFinal) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getMedico().getCodigo() == codigoMedico && !c.getData().isBefore(dataInicial) && !c.getData().isAfter(dataFinal)) {
                resultado.add(c);
            }
        }
        resultado.sort((c1, c2) -> c1.getHorario().compareTo(c2.getHorario()));
        return resultado;
    }

    public List<Medico> getMedicosDoPaciente(String cpfPaciente) {
        List<Medico> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getPaciente().getCpf().equals(cpfPaciente)) {
                Medico medico = c.getMedico();
                if (!resultado.contains(medico)) {
                    resultado.add(medico);
                }
            }
        }
        return resultado;
    }

    public List<Consulta> getConsultasPacienteMedico(String cpfPaciente, int codigoMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getPaciente().getCpf().equals(cpfPaciente) && c.getMedico().getCodigo() == codigoMedico) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Consulta> getConsultasAgendadas(String cpfPaciente) {
        LocalDate hoje = LocalDate.now();
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getPaciente().getCpf().equals(cpfPaciente) && c.getData().isAfter(hoje)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Paciente> getPacientesInativos(int codigoMedico, int meses) {
        LocalDate limite = LocalDate.now().minusMonths(meses);
        List<Paciente> resultado = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            boolean consultou = false;
            for (Consulta consulta : paciente.getConsultas()) {
                if (consulta.getMedico().getCodigo() == codigoMedico && consulta.getData().isAfter(limite)) {
                    consultou = true;
                    break;
                }
            }
            if (!consultou) {
                resultado.add(paciente);
            }
        }
        return resultado;
    }

    public void salvarMedicos() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("medicos.dat"))) {
            oos.writeObject(medicos);
        }
    }

    public void carregarMedicos() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("medicos.dat"))) {
            medicos = (List<Medico>) ois.readObject();
        }
    }

    public void lerMedicosDeArquivo(String arquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 2) {
                    registrarMedico(dados[0], Integer.parseInt(dados[1]));
                }
            }
        } catch (MedicoInvalidoException e) {
            e.printStackTrace();
        }
    }
}
