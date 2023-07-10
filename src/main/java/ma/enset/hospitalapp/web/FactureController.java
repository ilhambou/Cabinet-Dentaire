/**package ma.enset.hospitalapp.web;

 import jakarta.validation.Valid;
 import ma.enset.hospitalapp.entities.Consultation;
 import ma.enset.hospitalapp.entities.Facture;
 import ma.enset.hospitalapp.entities.SF;
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
 public class FactureController {
 @Autowired
 FactureRepository factureRepository;
 SFRepository sfRepository;


 public FactureController(FactureRepository factureRepository, SFRepository sfRepository) {
 this.factureRepository = factureRepository;
 this.sfRepository = sfRepository;
 }



 @GetMapping(path = "/admin/indexFacture")
 public String factures(Model model,
 @RequestParam(name = "page", defaultValue = "0") int page,
 @RequestParam(name = "size", defaultValue = "5") int size,
 @RequestParam(name = "keyword", defaultValue = "") String keyword
 ) {
 Page<Facture> pagesituations = factureRepository.findByEtatContains(keyword, PageRequest.of(page, size));
 model.addAttribute("listFactures", pagesituations.getContent());
 model.addAttribute("pages", new int[pagesituations.getTotalPages()]);
 model.addAttribute("currentPage", page);
 model.addAttribute("keyword", keyword);
 return "factures";
 }
 @GetMapping("/admin/factures")
 public String getAllFactures(Model model) {
 List<Facture> factures = factureRepository.findAll();
 model.addAttribute("factures", factures);
 return "factures"; // Remplacez "factures" par le nom de la vue que vous souhaitez afficher
 }

 @GetMapping("/admin/deleteFacture")
 public String deleteFacturation(Long id, String keyword, Integer page) {
 factureRepository.deleteById(id);
 return "redirect:/admin/factures?page=" + (page != null ? page : "") + "&keyword=" + (keyword != null ? keyword : "");
 }






 @GetMapping("/admin/formFacture")
 public String formFacture(Model model) {
 Facture facture=new Facture();
 SF sf=new SF();// Initialisez l'objet sf
 facture.setSf(sf); // Définissez l'objet sf dans facture

 model.addAttribute("facture", facture);

 List<SF> listSF =sfRepository .findAll();
 model.addAttribute("listSF", listSF);

 return "formFacture";
 }
 @PostMapping(path = "/admin/saveFacture")
 public String saveFacture(Model model, @Valid Facture f, BindingResult bindingResult,
 @RequestParam(defaultValue = "0") int page,
 @RequestParam(defaultValue = "") String keyword) {
 if (bindingResult.hasErrors()) return "formFacture";
 factureRepository.save(f);
 return "redirect:/admin/formFacture?page="+page+"&keyword="+keyword;
 }

 @GetMapping("/admin/editFacture")
 public String editFactures(Model model, Long id, String keyword, Integer page) {
 Facture sf = factureRepository.findById(id).orElse(null);
 if (sf == null) {
 throw new RuntimeException("Facture introuvable!");
 }
 List<SF> listSF = sfRepository.findAll(); // Remplacez "situationFinanciereRepository" par votre propre référence à votre source de données
 model.addAttribute("facture", sf);
 model.addAttribute("listSF", listSF);
 model.addAttribute("page", page);
 model.addAttribute("keyword", keyword);
 return "editFacture";
 }




 }**/
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