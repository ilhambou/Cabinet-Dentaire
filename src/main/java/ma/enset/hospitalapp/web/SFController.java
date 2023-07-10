package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.FactureRepository;
import ma.enset.hospitalapp.repository.SFRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class SFController {
    @Autowired
    private SFRepository sfRepository;
    @Autowired
    private FactureRepository factureRepository;


    private ConsultationRepository consultationRepository;
    @Autowired
    public SFController(SFRepository sfRepository, FactureRepository factureRepository, ConsultationRepository consultationRepository) {
        this.sfRepository = sfRepository;
        this.factureRepository = factureRepository;
        this.consultationRepository = consultationRepository;
    }
    @GetMapping("/admin/index/situation")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<SF> sfPage = sfRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("listSFs", sfPage.getContent());
        model.addAttribute("pages", new int[sfPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "situations";
    }

    @GetMapping("/admin/situation")
    @ResponseBody
    public List<SF> listSituations() {
        return sfRepository.findAll();
    }
    @GetMapping("/admin/formSituation")
    public String formSituation(Model model) {
        SF sf = new SF();
        Consultation consultation = new Consultation(); // Initialisez l'objet consultation
        sf.setConsultation(consultation); // Définissez l'objet consultation dans sf

        model.addAttribute("sf", sf);

        List<Consultation> listConsultations = consultationRepository.findAll();
        model.addAttribute("listConsultations", listConsultations);

        return "formSituation";
    }

    @GetMapping("/admin/deleteSituation")
    public String deleteSituation(@RequestParam(name = "id") Long id, String keyword, int page){
        SF situation = sfRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Situation introuvable"));
        Consultation consultation = situation.getConsultation();

        // Supprimer la situation
        sfRepository.deleteById(id);

        // Supprimer la consultation associée
        consultationRepository.delete(consultation);

        return "redirect:/admin/index/situation?page=" + page + "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
    }

    @PostMapping("/admin/editSituation/{id}")
    public String editSituation(@Valid SF sf, BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return "editSituation";
        }

        SF existingSF = sfRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Situation financière introuvable"));

        // Mettre à jour les propriétés de la situation financière existante
        existingSF.setNom(sf.getNom());
        existingSF.setConsultation(sf.getConsultation());
        existingSF.setPaye(sf.getPaye());
        existingSF.setReste(sf.getReste());
        existingSF.setTotalapayer(sf.getTotalapayer());

        // Créer la facture en fonction du reste
        Facture facture = new Facture();
        facture.setNom("Nom de la facture");
        facture.setSf(existingSF);

        if (existingSF.getReste() == 0) {
            facture.setEtat(Etat.Payé);
        } else {
            facture.setEtat(Etat.Non_Payé);
        }

        existingSF.getFactures().add(facture);

        sfRepository.save(existingSF);

        return "redirect:/admin/index/situation";
    }

    @GetMapping("/admin/editSituation")
    public String showEditSituationForm(@RequestParam(name = "id") Long id, Model model){
        SF sf = sfRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient introuvable"));
        model.addAttribute("sf", sf);
        model.addAttribute("listConsultations", consultationRepository.findAll()); // Ajoutez cette ligne

        return "editSituation";
    }



    @PostMapping("/admin/saveSituation")
    public String saveSituation(@Valid SF sf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formSituation";
        }

        // Enregistrer l'objet SF
        SF savedSF = sfRepository.save(sf);

        // Créer la facture en fonction du reste
        Facture facture = new Facture();
        facture.setNom("Nom de la facture");
        facture.setSf(savedSF);

        if (savedSF.getReste() == 0) {
            facture.setEtat(Etat.Payé);
        } else if (savedSF.getReste() >= 0 && savedSF.getReste() <= savedSF.getTotalapayer()) {
            facture.setEtat(Etat.Partielement_Payé);
        } else {
            facture.setEtat(Etat.Non_Payé);
        }

        savedSF.getFactures().add(facture);

        // Enregistrer à nouveau l'objet SF avec la facture associée
        sfRepository.save(savedSF);

        return "redirect:/admin/index/situation";
    }



}