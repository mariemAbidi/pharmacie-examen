package com.fst.first.controller;

import com.fst.first.model.Medicament;
import com.fst.first.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
@RequestMapping("/pharmacie")
public class PharmacieController {
    
    private final MedicamentRepository medicamentRepository;
    
    @Autowired
    public PharmacieController(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @GetMapping("")
    public String homeRedirect() {
        return "redirect:/pharmacie/medicaments";
    }

    @GetMapping("/medicaments")
    public String listeMedicaments(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findAll());
        model.addAttribute("searchForm", new SearchForm());
        return "medicaments/liste";
    }

    @GetMapping("/medicaments/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("medicament", new Medicament());
        return "medicaments/formulaire-ajout";
    }

    @PostMapping("/medicaments/ajouter")
    public String addMedicament(@ModelAttribute("medicament") Medicament medicament, 
                              BindingResult result) {
        if (result.hasErrors()) {
            return "medicaments/formulaire-ajout";
        }
        medicamentRepository.save(medicament);
        return "redirect:/pharmacie/medicaments";
    }

    @GetMapping("/medicaments/rechercher")
    public String searchMedicaments(@ModelAttribute SearchForm searchForm, 
                                  Model model) {
        List<Medicament> results = medicamentRepository
            .findByNomContainingIgnoreCase(searchForm.getSearchTerm());
        model.addAttribute("medicaments", results);
        return "medicaments/liste";
    }

    @PostMapping("/medicaments/supprimer/{id}")
    public String deleteMedicament(@PathVariable Long id) {
        medicamentRepository.deleteById(id);
        return "redirect:/pharmacie/medicaments";
    }
    
    public static class SearchForm {
        private String searchTerm;
        
        public String getSearchTerm() {
            return searchTerm;
        }
        
        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }
}