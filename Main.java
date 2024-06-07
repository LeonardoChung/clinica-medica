import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        Scanner sc = new Scanner(System.in);

        String caminhoMedicos = "src/medicos.csv";
        String caminhoPacientes = "src/pacientes.csv";
        String caminhoConsultas = "src/consultas.csv";

        try {
            sistema.carregarMedicos(caminhoMedicos);
            sistema.carregarPacientes(caminhoPacientes);
            sistema.carregarConsultas(caminhoConsultas);

        } catch (FileNotFoundException e) {
            System.out.println("Erro: arquivo csv não encontrado.");
            return;
        }

        int escolhaSaida = 0;

        while (escolhaSaida != 1 && escolhaSaida != 2) {
            System.out.println("(1) Exibir na tela");
            System.out.println("(2) Salvar em arquivo txt");
            System.out.print("Opção: ");
            escolhaSaida = sc.nextInt();
            sc.nextLine();

            if (escolhaSaida != 1 && escolhaSaida != 2) {
                System.out.println("Opção inválida, por favor escolha 1 ou 2.");
            }
        }

        String nomeArquivo = null;
        FileWriter fw = null;

        if (escolhaSaida == 2) {
            System.out.print("Digite o nome do arquivo para salvar o resultado (com extensão .txt): ");
            nomeArquivo = sc.nextLine();

            try {
                fw = new FileWriter("src/" + nomeArquivo);
            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo: " + e.getMessage());
                return;
            }
        }

        int opcaoMenu = 0;
        while (opcaoMenu != 9) {
            System.out.println("\nMenu de Opções:");
            System.out.println("(1) Todos os pacientes de um médico");
            System.out.println("(2) Consultas agendadas para um médico em determinado período");
            System.out.println("(3) Médicos que um paciente já consultou");
            System.out.println("(4) Consultas realizadas entre um paciente e um médico");
            System.out.println("(5) Consultas agendadas para um paciente no futuro");
            System.out.println("(6) Pacientes que não consultam um médico há mais de X meses");
            System.out.println("(9) Sair");
            System.out.print("Opção: ");
            opcaoMenu = sc.nextInt();

            switch (opcaoMenu) {
                case 1:
                    System.out.print("Digite o código do médico: ");
                    int codigoMedico = sc.nextInt();
                    List<Paciente> pacientes = sistema.getPacientesDoMedico(codigoMedico);

                    String resultado = "Pacientes do médico " + codigoMedico + ":\n";
                    for (Paciente paciente : pacientes) {
                        resultado += "- " + paciente.getNome() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 2:
                    System.out.print("Digite o código do médico: ");
                    codigoMedico = sc.nextInt();
                    System.out.print("Digite a data inicial (AAAA-MM-DD): ");
                    LocalDate dataInicial = LocalDate.parse(sc.next());
                    System.out.print("Digite a data final (AAAA-MM-DD): ");
                    LocalDate dataFinal = LocalDate.parse(sc.next());

                    List<Consulta> consultas = sistema.getConsultasDoMedicoEmPeriodo(codigoMedico, dataInicial, dataFinal);

                    resultado = "Consultas para o médico " + codigoMedico + " no período:\n";
                    for (Consulta consulta : consultas) {
                        resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 3:
                    System.out.print("Digite o CPF do paciente: ");
                    String cpfPaciente = sc.next();

                    List<Medico> medicos = sistema.getMedicosDoPaciente(cpfPaciente);

                    resultado = "Médicos que o paciente com CPF " + cpfPaciente + " já consultou:\n";
                    for (Medico medico : medicos) {
                        resultado += "- " + medico.getNome() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 4:
                    System.out.print("Digite o CPF do paciente: ");
                    cpfPaciente = sc.next();
                    System.out.print("Digite o código do médico: ");
                    codigoMedico = sc.nextInt();

                    List<Consulta> consultasPacienteMedico = sistema.getConsultasPacienteMedico(cpfPaciente, codigoMedico);

                    resultado = "Consultas entre o paciente com CPF " + cpfPaciente +"e o médico " + codigoMedico + ":\n";
                    for (Consulta consulta : consultasPacienteMedico) {
                        resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 5:
                    System.out.print("Digite o CPF do paciente: ");
                    cpfPaciente = sc.next();

                    List<Consulta> consultasFuturas = sistema.getConsultasAgendadas(cpfPaciente);

                    resultado = "Consultas futuras para o paciente com CPF " + cpfPaciente + ":\n";
                    for (Consulta consulta : consultasFuturas) {
                        resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 6:
                    System.out.print("Digite o código do médico: ");
                    codigoMedico = sc.nextInt();
                    System.out.print("Digite o número de meses: ");
                    int meses = sc.nextInt();

                    List<Paciente> pacientesInativos = sistema.getPacientesInativos(codigoMedico, meses);

                    resultado = "Pacientes do médico " + codigoMedico + " que não consultam há mais de " + meses + " meses:\n";
                    for (Paciente paciente : pacientesInativos) {
                        resultado += "- " + paciente.getNome() + "\n";
                    }

                    if (fw != null) {
                        try {
                            fw.write(resultado);
                        } catch (IOException e) {
                            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                        }
                    } else {
                        System.out.print(resultado);
                    }
                    break;

                case 9:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }

        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
            }
        }

        sc.close();


    }
}
