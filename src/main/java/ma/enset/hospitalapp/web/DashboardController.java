package ma.enset.hospitalapp.web;

import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.Consultation;
import ma.enset.hospitalapp.entities.Event;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.EventRepository;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller

public class DashboardController {
    @Autowired
    PatientRepository patientRepository;
    @Autowired

    EventRepository eventRepository;
    @Autowired

    ConsultationRepository consultationRepository;


    @GetMapping("/user/dashboard")
    public String dashboard(Model model) {
        long totalPatients = patientRepository.count();
        long totalEvent=eventRepository.count();
        Map<Date, Double> sommeParPrix = consultationRepository.findAll().stream()
                .filter(rdv -> rdv.getDateConsultation() != null)
                .collect(Collectors.groupingBy(Consultation::getDateConsultation, Collectors.summingDouble(consultation -> consultation.getAct().getPrix())));
        long totalMalePatients = patientRepository.countBySexe("HOMME");
        long totalFemalePatients = patientRepository.countBySexe("FEMME");

        long totalPatientss = totalMalePatients + totalFemalePatients;

        double malePercentage = (double) totalMalePatients / totalPatientss * 100;
        double femalePercentage = (double) totalFemalePatients / totalPatientss* 100;


        ///

        Map<Date, Double> sommePrixActesParDate = new HashMap<>();

        List<Event> events = eventRepository.findAll();

        for (Event event : events) {
            Date startDate = event.getStartDate();

            for (Act act : event.getActs()) {
                Double prix = act.getPrix();
                Double sommePrix = sommePrixActesParDate.getOrDefault(startDate, 0.0);
                sommePrix += prix;
                sommePrixActesParDate.put(startDate, sommePrix);
            }
        }

        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalEvent", totalEvent);
        model.addAttribute("sommeParPrix", sommeParPrix);


        model.addAttribute("totalMalePatients", totalMalePatients);
        model.addAttribute("totalFemalePatients", totalFemalePatients);
        model.addAttribute("malePercentage", malePercentage);
        model.addAttribute("femalePercentage", femalePercentage);


        model.addAttribute("sommePrixActesParDate", sommePrixActesParDate);

        return "dashboard";
    }




    }



