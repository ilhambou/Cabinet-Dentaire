package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.entities.Sexe;
import ma.enset.hospitalapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@SpringBootApplication
public class HospitalAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    CommandLineRunner start(PatientRepository patientRepository){
        return args -> {


        };
    }
    @Bean
    CommandLineRunner act(ActRepository actRepository){
        return args -> {
        };
    }
    @Bean
    CommandLineRunner Calendar(CalendrRepository calendrRepository){
        return args -> {
        };
    }
    @Bean
    CommandLineRunner eventRepositoryRunner(EventRepository eventRepository){
        return args -> {
        };
    }
    @Bean
    CommandLineRunner consultationRepositoryRunner(ConsultationRepository consultationRepository){
        return args -> {
            //    actRepository.save(new Act(null,"blanchissement",2000.0,"Blanchisseement pour tous "));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
