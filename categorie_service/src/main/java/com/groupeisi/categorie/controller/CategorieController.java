package com.groupeisi.categorie.controller;

import com.groupeisi.categorie.entities.Categorie;
import com.groupeisi.categorie.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategorieController 
{
    @Autowired
    private CategorieService categorieService;

	/*
	 * @GetMapping public List<Categorie> getAllCategories() { return
	 * categorieRepository.findAll(); }
	 */
    
    @GetMapping("/list")
    public String listCategories(@RequestParam(defaultValue = "0") int page, Model model)
    {
        int pageSize = 5; // nombre d'éléments par page

        Page<Categorie> categoriePage = categorieService.getAllCategories(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        List<Categorie> items = categoriePage.getContent();

        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentNumber", categoriePage.getNumber());
        model.addAttribute("totalPages", categoriePage.getTotalPages());
        model.addAttribute("categories", categorieService.getAllCategories());

        return "categorie/list";
    }

    @GetMapping("/modal/{itemId}")
    public String loadItemModal(@PathVariable Optional<Long> itemId, Model model)
    {
        Categorie item = null;
        if (itemId.isPresent())
        {
            Long itemIdRecup = itemId.get();
            item = categorieService.getCategorieById(itemIdRecup);
        }

        if (item==null)
        {
            item = new Categorie();
        }

        model.addAttribute("item", item);
        model.addAttribute("categories", categorieService.getAllCategories());

        return "categorie/modal";
    }

    // Traite le formulaire de création de catégorie après soumission
    @PostMapping("/save")
    public String save(@ModelAttribute("item") Categorie item, @RequestParam(defaultValue = "0") int currentPage, BindingResult bindingResult, Model modely)
    {
        if (categorieService.isNomCategorieUnique(item.getNom(), item.getId()))
        {
            System.out.println("good here");

            // Si l'ID est null, il s'agit d'une nouvelle catégorie à créer
            if (item.getId() == null)
            {
                // Logique de création de la catégorie
                item.setId(null);
                if (!(item.getCategorieParent()!=null && item.getCategorieParent().getId()!=null && !item.getCategorieParent().getId().toString().isEmpty()))
                {
                    item.setCategorieParent(null);
                }
                categorieService.saveCategorie(item);
            }
            else
            {
                // Sinon, c'est une catégorie existante à mettre à jour
                // Logique de mise à jour de la catégorie
                Categorie existingItem = categorieService.getCategorieById(item.getId());

                if (existingItem==null)
                {
                    new RuntimeException("Catégorie introuvable");
                }
                else
                {
                    Categorie categorieParent = null;
                    System.out.println("arrive dans la fonction =>" + item.getNom());

                    existingItem.setNom(item.getNom());
                    existingItem.setDescription(item.getDescription());

                    if (item.getCategorieParent()!=null && item.getCategorieParent().getId()!=null && !item.getCategorieParent().getId().toString().isEmpty())
                    {
                        categorieParent = categorieService.getCategorieById(item.getCategorieParent().getId());

                        // Il faut controler la remontée au niveau des parents pour éviter une boucle infinie
                        // La catégorie parente ne peut pas être la catégorie elle-même ou l'un de ses enfants.
                        /*
                        // 1ère Méthode
                        for (int i=0; i < categorieParent.getCategorieParents().size(); i++)
                        {
                            if (categorieParent.getCategories().get(i).getId()==existingItem.getId())
                            {
                                throw new IllegalArgumentException("boucle infinie");
                            }
                        }

                        // 2ème Méthode
                        Long parentCategoryId = existingItem.getCategorieParent() != null ? existingItem.getCategorieParent().getId() : null;
                        if (existingItem.getId().equals(parentCategoryId) || categorieService.isChildCategory(existingItem.getId(), parentCategoryId))
                        {
                            throw new IllegalArgumentException("boucle infinie");
                        }
                        */

                        System.out.println("Categorie parent here " + categorieParent);
                        if (categorieParent == null)
                        {
                            System.out.println("Catégorie parente introuvable avec l'ID : " + item.getCategorieParent().getId());
                        }
                    }

                    existingItem.setCategorieParent(categorieParent);

                    categorieService.saveCategorie(existingItem);
                }
            }
        }
        else
        {
            // Si le nom n'est pas unique, ajoutez une erreur au résultat de liaison
            bindingResult.rejectValue("nom", "error.categorie", "Ce nom de catégorie existe déjà.");
        }

        // Redirection vers la page souhaitée après le traitement du formulaire
        return "redirect:/categories/list?page=" + (currentPage);  // Redirection vers la liste des catégories par exemple
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Categorie> apiCategories()
    {
        // return categorieService.getAllCategories();
        int page = 0, pageSize = 15;

        Page<Categorie> categoriePage = categorieService.getAllCategories(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));

        List<Categorie> items = categoriePage.getContent();

        return items;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id)
    {
        categorieService.deleteCategoryById(id);
        return ResponseEntity.ok("Donnée supprimée avec succès");
    }

}