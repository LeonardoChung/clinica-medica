import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InterfaceGrafica extends JFrame {
    private Sistema sistema;

    public InterfaceGrafica(Sistema sistema) {
        this.sistema = sistema;
        initUI();
    }

    private void initUI() {
        setTitle("Sistema de Consultas Médicas");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        getContentPane().add(panel);

        JButton btnAgendarConsulta = new JButton("Agendar Consulta");
        JButton btnRegistrarMedico = new JButton("Registrar Médico");
        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        JButton btnPacientesDoMedico = new JButton("Pacientes do Médico");
        JButton btnConsultasPeriodo = new JButton("Consultas no Período");
        JButton btnMedicosDoPaciente = new JButton("Médicos do Paciente");
        JButton btnConsultasPacienteMedico = new JButton("Consultas Paciente e Médico");
        JButton btnConsultasFuturas = new JButton("Consultas Futuras");
        JButton btnPacientesInativos = new JButton("Pacientes Inativos");

        panel.add(btnAgendarConsulta);
        panel.add(btnRegistrarMedico);
        panel.add(btnRegistrarPaciente);
        panel.add(btnPacientesDoMedico);
        panel.add(btnConsultasPeriodo);
        panel.add(btnMedicosDoPaciente);
        panel.add(btnConsultasPacienteMedico);
        panel.add(btnConsultasFuturas);
        panel.add(btnPacientesInativos);

        btnAgendarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarConsulta();
            }
        });

        btnRegistrarMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarMedico();
            }
        });

        btnRegistrarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPaciente();
            }
        });

        btnPacientesDoMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPacientesDoMedico();
            }
        });

        btnConsultasPeriodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarConsultasNoPeriodo();
            }
        });

        btnMedicosDoPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarMedicosDoPaciente();
            }
        });

        btnConsultasPacienteMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarConsultasPacienteMedico();
            }
        });

        btnConsultasFuturas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarConsultasFuturas();
            }
        });

        btnPacientesInativos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPacientesInativos();
            }
        });
    }

    private void agendarConsulta() {
        JTextField txtData = new JTextField();
        JTextField txtHorario = new JTextField();
        JTextField txtCodigoMedico = new JTextField();
        JTextField txtCpfPaciente = new JTextField();
        Object[] message = {
                "Data (dd/MM/yyyy):", txtData,
                "Horário (HH:mm):", txtHorario,
                "Código do Médico:", txtCodigoMedico,
                "CPF do Paciente:", txtCpfPaciente,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agendar Consulta", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                LocalDate data = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalTime horario = LocalTime.parse(txtHorario.getText());
                int codigoMedico = Integer.parseInt(txtCodigoMedico.getText());
                String cpfPaciente = txtCpfPaciente.getText();

                Medico medico = sistema.getMedicos().stream()
                        .filter(m -> m.getCodigo() == codigoMedico)
                        .findFirst()
                        .orElse(null);

                Paciente paciente = sistema.getPacientes().stream()
                        .filter(p -> p.getCpf().equals(cpfPaciente))
                        .findFirst()
                        .orElse(null);

                sistema.agendarConsulta(data, horario, medico, paciente);
                JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
            } catch (ConsultaInvalidaException | IOException e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }

    private void registrarMedico() {
        JTextField txtNome = new JTextField();
        JTextField txtCodigo = new JTextField();
        Object[] message = {
                "Nome do Médico:", txtNome,
                "Código do Médico:", txtCodigo,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Registrar Médico", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText();
                int codigo = Integer.parseInt(txtCodigo.getText());
                sistema.registrarMedico(nome, codigo);
                JOptionPane.showMessageDialog(this, "Médico registrado com sucesso!");
            } catch (MedicoInvalidoException | IOException e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }

    private void registrarPaciente() {
        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        Object[] message = {
                "Nome do Paciente:", txtNome,
                "CPF do Paciente:", txtCpf,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Registrar Paciente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText();
                String cpf = txtCpf.getText();
                sistema.registrarPaciente(nome, cpf);
                JOptionPane.showMessageDialog(this, "Paciente registrado com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }

    private void listarPacientesDoMedico() {
        JTextField txtCodigoMedico = new JTextField();
        Object[] message = {
                "Código do Médico:", txtCodigoMedico,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Pacientes do Médico", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int codigoMedico = Integer.parseInt(txtCodigoMedico.getText());
            List<Paciente> pacientes = sistema.getPacientesDoMedico(codigoMedico);
            String resultado = "Pacientes do médico " + codigoMedico + ":\n";
            for (Paciente paciente : pacientes) {
                resultado += "- " + paciente.getNome() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    private void listarConsultasNoPeriodo() {
        JTextField txtCodigoMedico = new JTextField();
        JTextField txtDataInicial = new JTextField();
        JTextField txtDataFinal = new JTextField();
        Object[] message = {
                "Código do Médico:", txtCodigoMedico,
                "Data Inicial (yyyy-MM-dd):", txtDataInicial,
                "Data Final (yyyy-MM-dd):", txtDataFinal,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Consultas no Período", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int codigoMedico = Integer.parseInt(txtCodigoMedico.getText());
            LocalDate dataInicial = LocalDate.parse(txtDataInicial.getText());
            LocalDate dataFinal = LocalDate.parse(txtDataFinal.getText());

            List<Consulta> consultas = sistema.getConsultasDoMedicoEmPeriodo(codigoMedico, dataInicial, dataFinal);
            String resultado = "Consultas para o médico " + codigoMedico + " no período:\n";
            for (Consulta consulta : consultas) {
                resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    private void listarMedicosDoPaciente() {
        JTextField txtCpfPaciente = new JTextField();
        Object[] message = {
                "CPF do Paciente:", txtCpfPaciente,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Médicos do Paciente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String cpfPaciente = txtCpfPaciente.getText();
            List<Medico> medicos = sistema.getMedicosDoPaciente(cpfPaciente);
            String resultado = "Médicos que o paciente com CPF " + cpfPaciente + " já consultou:\n";
            for (Medico medico : medicos) {
                resultado += "- " + medico.getNome() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    private void listarConsultasPacienteMedico() {
        JTextField txtCpfPaciente = new JTextField();
        JTextField txtCodigoMedico = new JTextField();
        Object[] message = {
                "CPF do Paciente:", txtCpfPaciente,
                "Código do Médico:", txtCodigoMedico,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Consultas Paciente e Médico", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String cpfPaciente = txtCpfPaciente.getText();
            int codigoMedico = Integer.parseInt(txtCodigoMedico.getText());

            List<Consulta> consultas = sistema.getConsultasPacienteMedico(cpfPaciente, codigoMedico);
            String resultado = "Consultas entre o paciente com CPF " + cpfPaciente + " e o médico " + codigoMedico + ":\n";
            for (Consulta consulta : consultas) {
                resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    private void listarConsultasFuturas() {
        JTextField txtCpfPaciente = new JTextField();
        Object[] message = {
                "CPF do Paciente:", txtCpfPaciente,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Consultas Futuras", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String cpfPaciente = txtCpfPaciente.getText();
            List<Consulta> consultas = sistema.getConsultasAgendadas(cpfPaciente);
            String resultado = "Consultas futuras para o paciente com CPF " + cpfPaciente + ":\n";
            for (Consulta consulta : consultas) {
                resultado += "- Data: " + consulta.getData() + ", Horário: " + consulta.getHorario() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    private void listarPacientesInativos() {
        JTextField txtCodigoMedico = new JTextField();
        JTextField txtMeses = new JTextField();
        Object[] message = {
                "Código do Médico:", txtCodigoMedico,
                "Número de Meses:", txtMeses,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Listar Pacientes Inativos", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int codigoMedico = Integer.parseInt(txtCodigoMedico.getText());
            int meses = Integer.parseInt(txtMeses.getText());

            List<Paciente> pacientes = sistema.getPacientesInativos(codigoMedico, meses);
            String resultado = "Pacientes do médico " + codigoMedico + " que não consultam há mais de " + meses + " meses:\n";
            for (Paciente paciente : pacientes) {
                resultado += "- " + paciente.getNome() + "\n";
            }
            JOptionPane.showMessageDialog(this, resultado);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Sistema sistema = new Sistema();
            try {
                sistema.carregarMedicos("src/medicos.csv");
                sistema.carregarPacientes("src/pacientes.csv");
                sistema.carregarConsultas("src/consultas.csv");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InterfaceGrafica ex = new InterfaceGrafica(sistema);
            ex.setVisible(true);
        });
    }
}
