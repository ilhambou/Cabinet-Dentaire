package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Consultation;
import ma.enset.hospitalapp.entities.Ordonnance;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.OrdonnanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrdonnanceController {
    @Autowired
    OrdonnanceRepository ordonnancerepository;

    @Autowired
    ConsultationRepository consultationRepository;


    public OrdonnanceController(OrdonnanceRepository ordonnancerepository, ConsultationRepository consultationRepository) {
        this.ordonnancerepository=ordonnancerepository;
        this.consultationRepository=consultationRepository;
    }



    @GetMapping(path = "/user/indexordonnance")
    public String ordonnances(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "5") int size,
                              @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        Page<Ordonnance> pageordonnances= ordonnancerepository.findByDescriptionContains(keyword, PageRequest.of(page, size));
        model.addAttribute("listOrdonnances", pageordonnances.getContent());
        model.addAttribute("pages", new int[pageordonnances.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "ordonnances";
    }
    @GetMapping("/user/ordonnances")
    public String getAllOrdonnances(Model model) {
        List<Ordonnance> ordonnances = ordonnancerepository.findAll();
        model.addAttribute("ordonnances", ordonnances);
        return "ordonnances"; // Remplacez "factures" par le nom de la vue que vous souhaitez afficher
    }







    @GetMapping("/user/formOrdonnance")
    public String formOrdonnance(Model model) {


        Ordonnance ordonnance=new Ordonnance();
        Consultation consultation=new Consultation();// Initialisez l'objet sf
        ordonnance.setConsultation(consultation); // Définissez l'objet sf dans facture

        model.addAttribute("ordonnance", ordonnance);

        List<Consultation> listConsultation =consultationRepository .findAll();
        model.addAttribute("listConsultation", listConsultation);


        return "formOrdonnance";

    }





    @PostMapping(path = "/user/saveOrdonnance")
    public String save(Model model, @Valid Ordonnance ordonnance, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) return "formOrdonnance";
        ordonnancerepository.save(ordonnance);
        return "redirect:/user/ordonnances?page="+page+"&keyword="+keyword;
    }





}