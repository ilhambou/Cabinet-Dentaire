package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repository.ConsultationRepository;
import ma.enset.hospitalapp.repository.FactureRepository;
import ma.enset.hospitalapp.repository.PatientRepository;
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
public class FactureController {
    private final FactureRepository factureRepository;
    private final SFRepository sfRepository;

    @Autowired
    public FactureController(FactureRepository factureRepository, SFRepository sfRepository) {
        this.factureRepository = factureRepository;
        this.sfRepository = sfRepository;
    }

    @GetMapping("/user/index/facture")
    public String factures(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Facture> facturePage = factureRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("factures", facturePage.getContent());
        model.addAttribute("pages", new int[facturePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "factures";
    }

    @GetMapping("/user/facture/{id}")
    public String getFactureDetails(@PathVariable("id") Long id, Model model) {
        // Récupérer la facture correspondant à l'ID
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Facture introuvable"));

        // Ajouter la facture au modèle
        model.addAttribute("facture", facture);

        // Retourner le nom de la vue pour afficher les détails de la facture
        return "facture_details";
    }
}