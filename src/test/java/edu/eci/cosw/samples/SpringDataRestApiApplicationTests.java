package edu.eci.cosw.samples;


import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.repository.PatientsRepository;
import edu.eci.cosw.samples.services.PatientServices;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles; 
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApiApplication.class)
@WebAppConfiguration
@ActiveProfiles("**test**")
public class SpringDataRestApiApplicationTests {

    @Autowired
    private PatientsRepository pr;

    @Autowired
    private PatientServices ps;

    @Test
	public void deberiaExistirUnPacienteEnBD(){

        pr.deleteAll();
        PacienteId pid=new PacienteId(1122334455,"cc");
        Paciente paciente = new Paciente(pid, "David Reina", new Date(1990,01,01));
        pr.save(paciente);
        

        Paciente pacientePrueba=pr.findOne(pid);
        Assert.assertEquals("Los dos pacientes son diferentes",pacientePrueba,paciente);
    }

    @Test
    public void noDeberiaExistirPaciente(){

        pr.deleteAll();
        PacienteId pid=new PacienteId(1122334455,"cc");
        Paciente paciente = new Paciente(pid, "David Reina", new Date(1990,01,01));

        Paciente pacientePrueba=pr.findOne(pid);
        Assert.assertNull("No existe paciente!",pacientePrueba);
    }

    @Test
    public void noExistePacientesConNConsultas() throws ServicesException {

        pr.deleteAll();
        PacienteId pid=new PacienteId(1122334455,"cc");
        Paciente paciente = new Paciente(pid, "David Reina", new Date(1990,01,01));
        paciente.getConsultas().add(new Consulta(new Date(2017,9,17), "Peritonitis"));

        pr.save(paciente);

        List<Paciente> pacientes = ps.topPatients(2);

        Assert.assertEquals("Lista de pacientes vacia",pacientes.size(),0);

    }

    @Test
    public void existePacientesConNConsultas() throws ServicesException {

        pr.deleteAll();
        PacienteId pid=new PacienteId(1122334455,"cc");
        Paciente paciente = new Paciente(pid, "David Reina", new Date(1990,01,01));

        PacienteId pid2=new PacienteId(918153436,"cc");
        Paciente paciente2 = new Paciente(pid2, "Gabriel Manrrigue.", new Date(2000,01,01));

        PacienteId pid3=new PacienteId(657483922,"cc");
        Paciente paciente3 = new Paciente(pid3, "Dario Payares", new Date(1999,01,01));

        pr.save(paciente);

        paciente2.getConsultas().add(new Consulta(new Date(2017,9,15), "Admidalitis"));
        pr.save(paciente2);

        paciente3.getConsultas().add(new Consulta(new Date(2017,9,16), "Dolor lumbar"));
        paciente3.getConsultas().add(new Consulta(new Date(2017,9,17), "Dolor dental"));
        pr.save(paciente3);

        List<Paciente> pacientes = ps.topPatients(1);

        Assert.assertEquals("Lista de pacientes",pacientes.size(),2);
    }

}
