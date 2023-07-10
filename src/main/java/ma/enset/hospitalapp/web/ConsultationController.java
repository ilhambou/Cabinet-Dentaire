package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
public class ConsultationController {
    // Injection des dépendances
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActRepository actRepository;
    @Autowired
    public ConsultationController(EventRepository eventRepository, ActRepository actRepository2) {
        this.eventRepository = eventRepository;
        this.actRepository = actRepository2;


    }

    @GetMapping("/user/index4")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Consultation> pageConsultations = consultationRepository.findBydescriptionContains(keyword, PageRequest.of(page, size));
        model.addAttribute("listConsultations", pageConsultations.getContent());
        model.addAttribute("pages", new int[pageConsultations.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "consultation";
    }

    @GetMapping("/user/formConsultation")
    public String formConsultation(Model model) {
        model.addAttribute("consultation", new Consultation());
        List<Event> events = eventRepository.findAll();
        model.addAttribute("listEvents", events);
        List<Act> acts = actRepository.findAll();
        model.addAttribute("listActs", acts);
        model.addAttribute("act", new Act()); // Ajout de l'objet Act à votre modèle
        return "formConsultation";
    }

    @PostMapping(path = "/user/saveConsultation")
    public String save(Model model, @Valid Consultation consultation, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            return "formConsultation";
        }
        consultationRepository.save(consultation);
        return "redirect:/user/index4?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/user/deleteConsultation")
    public String deleteAct(@RequestParam(name = "id") Long id, String keyword, int page){
        consultationRepository.deleteById(id);
        return "redirect:/user/index4?page="+page+"&keyword="+keyword;
    }
    @PostMapping("/user/editConsultation/{id}")
    public String editConsultation(@Valid Consultation patient, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()) {
            return "editConsultation";
        }

        Consultation p = consultationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient introuvable"));
        p.setDescription(patient.getDescription());
        p.setPourcentage(patient.getPourcentage());
        p.setAct(patient.getAct());
        p.setEvent(patient.getEvent());
        consultationRepository.save(p);
        return "redirect:/user/index4";
    }
    @GetMapping("/user/editConsultation")
    public String showEditConsultationForm(@RequestParam(name = "id") Long id, Model model){
        Consultation patient = consultationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient introuvable"));
        model.addAttribute("consultation", patient);
        model.addAttribute("listActs", actRepository.findAll()); // Ajoutez cette ligne
        model.addAttribute("listEvents", eventRepository.findAll()); // Ajoutez cette ligne

        return "editConsultation";
    }


}
