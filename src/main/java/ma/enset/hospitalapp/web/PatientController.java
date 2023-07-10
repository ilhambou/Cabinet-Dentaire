package ma.enset.hospitalapp.web;

import jakarta.validation.Valid;
import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    /*  @GetMapping("/user/index")
       public String index(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "5") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String kw
                           ){
           Page<Patient> pagePatients = patientRepository.findByNomContains(kw, PageRequest.of(page,size));
           model.addAttribute("listPatients",pagePatients.getContent());
           model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
           model.addAttribute("currentPage",page);
           model.addAttribute("keyword",kw);
           //return "test.Site1";
           return "patients";
       }*/


    @GetMapping("/admin/list")
    public String list(Model model,
                       @RequestParam(name = "page",defaultValue = "0") int page,
                       @RequestParam(name = "size",defaultValue = "5") int size,
                       @RequestParam(name = "keyword",defaultValue = "") String kw
    ){
        Page<Patient> pagePatients = patientRepository.findByNomContains(kw, PageRequest.of(page,size));
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        //return "test.Site1";
        return "patients";
    }
    @GetMapping("/user/index")
    public String index(){

        return "index";
    }

    @GetMapping("/admin/deletePatient")
    public String deletePatient(@RequestParam(name = "id") Long id, String keyword, int page){
        patientRepository.deleteById(id);
        return "redirect:/user/list?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/admin/formPatient")
    public String formPatient(Model model ){
        model.addAttribute("patient",new Patient());
        return "formPatient";
    }
    @PostMapping("/admin/savePatient")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "formPatient";
        patientRepository.save(patient);
        return "redirect:/user/list";
    }

    @PostMapping("/admin/editPatient/{id}")
    public String editPatient(@Valid Patient patient, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()) {
            return "editPatient";
        }

        Patient p = patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient introuvable"));
        p.setNom(patient.getNom());
        p.setPrenom(patient.getPrenom());
        p.setDateNaissance(patient.getDateNaissance());
        p.setEmail(patient.getEmail());
        p.setTel(patient.getTel());
        p.setCIN(patient.getCIN());
        p.setSexe(patient.getSexe());

        patientRepository.save(p);
        return "redirect:/user/list";
    }
    @GetMapping("/admin/editPatient")
    public String editPatient(@RequestParam(name = "id") Long id, Model model){
        Patient patient=patientRepository.findById(id).get();
        model.addAttribute("patient",patient);
        return "editPatient";
    }
    @GetMapping("/index")
    public String home(){
        return "index";
    }

   /* @GetMapping("/index")
    public String home(Model model, Authentication authentication) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        model.addAttribute("userRoles", userDetails.getAuthorities());
        return "index";
    }*/



}
