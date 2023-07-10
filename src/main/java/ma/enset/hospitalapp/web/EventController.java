package ma.enset.hospitalapp.web;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.entities.Event;
import ma.enset.hospitalapp.repository.ActRepository;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.EventRepository;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {
    @Autowired
    private EventRepository actRepository;
    private PatientRepository patientRepository;
    private ActRepository actRepository2;

    private  EntityManager entityManager ;




    @Autowired
    public EventController(PatientRepository patientRepository, ActRepository actRepository2) {
        this.patientRepository = patientRepository;
            this.actRepository2 = actRepository2;


    }


    @GetMapping("/admin/index3")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "5") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String kw
    ){
        Page<ma.enset.hospitalapp.entities.Event> pagePatients = actRepository.findBySummaryContains(kw, PageRequest.of(page,size));
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "Display_event";
    }



    @GetMapping("/admin/formEvent")
    public String formAct(Model model){
        model.addAttribute("event", new ma.enset.hospitalapp.entities.Event());
        model.addAttribute("consultation",new Consultation());
        // Fetch the list of patients
        List<Patient> patients = patientRepository.findAll(); // Replace 'patientRepository' with the actual repository for the Patient entity
        model.addAttribute("listPatients", patients);
        List<Act> acts = actRepository2.findAll(); // Replace 'patientRepository' with the actual repository for the Patient entity
        model.addAttribute("listActs", acts);
        return "event";
    }

    @PostMapping("/admin/saveEvent")
    public String saveEvent(@Valid Event patient, BindingResult bindingResult,
                            @RequestParam("actIds") List<Long> actIds,Model model,@Valid Consultation consultation) {
        if (bindingResult.hasErrors()) {
            return "event";
        }

        List<Act> acts = actRepository2.findAllById(actIds);
        patient.setActs(acts);

        actRepository.save(patient);

        if (patient.getEtat() == StatusRDV.DONE) {
            model.addAttribute("consultation", new Consultation());


            return "formConsultation" ;
        }

        return "redirect:/admin/index3";
    }

    @GetMapping("/api/patients")
    @ResponseBody
    public List<Patient> getPatients() {
        List<Patient> patients = patientRepository.findAll(); // Replace with appropriate repository method
        return patients;
    }






}