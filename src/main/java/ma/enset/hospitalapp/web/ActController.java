package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.ActRepository;
import ma.enset.hospitalapp.repository.PatientRepository;
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

import java.util.NoSuchElementException;

@Controller
public class ActController {
    @Autowired
    private ActRepository actRepository;
    @GetMapping("/admin/index2")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "5") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String kw
    ){
        Page<Act> pagePatients = actRepository.findByDescriptionContains(kw, PageRequest.of(page,size));
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "acts";
    }
    @GetMapping("/admin/deleteAct")
    public String deleteAct(@RequestParam(name = "id") Long id, String keyword, int page){
        actRepository.deleteById(id);
        return "redirect:/admin/index2?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/admin/formAct")
    public String formAct(Model model ){
        model.addAttribute("act",new Act());
        return "formAct";
    }
   @PostMapping("/admin/saveAct")
    public String saveAct(@Valid Act patient, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "formAct";
       actRepository.save(patient);
        return "redirect:/admin/index2";
    }

     @PostMapping("/admin/editAct/{id}")
    public String editAct(@Valid Act patient, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()) {
            return "editAct";
        }

        Act p = actRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient introuvable"));
        p.setDescription(patient.getDescription());
        p.setLibelle(patient.getLibelle());
        p.setPrix(patient.getPrix());
        actRepository.save(p);
         return "redirect:/admin/index2";
    }
    @GetMapping("/admin/editAct")
    public String editAct(@RequestParam(name = "id") Long id, Model model){
        Act patient=actRepository.findById(id).get();
        model.addAttribute("act",patient);
        return "editAct";
    }

}
