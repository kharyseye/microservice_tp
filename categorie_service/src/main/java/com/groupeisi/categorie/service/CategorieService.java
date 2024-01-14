package com.groupeisi.categorie.service;

import com.groupeisi.categorie.entities.Categorie;
import com.groupeisi.categorie.entities.CategorieView;
import com.groupeisi.categorie.repositories.CategorieRepository;
import com.groupeisi.categorie.repositories.CategorieViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;  // Assurez-vous d'avoir une interface CategorieRepository
    @Autowired
    private CategorieViewRepository categorieViewRepository;  // Assurez-vous d'avoir une interface CategorieRepository

    public List<Categorie> getAllCategories()
    {
        return categorieRepository.findAll();
    }

    public Page<Categorie> getAllCategories(Pageable pageable)
    {
        return categorieRepository.findAll(pageable);
    }


    public Categorie getCategorieById(Long id)
    {
        return categorieRepository.findById(id).orElse(null);
    }

    public void saveCategorie(Categorie categorie)
    {
        categorieRepository.save(categorie);
    }

    public void deleteCategorie(Long id)
    {
        categorieRepository.deleteById(id);
    }

    public boolean isNomCategorieUnique(String nom, Long id)
    {
        Optional<Categorie> existingCategorie = categorieRepository.findByNom(nom);
        return existingCategorie.isEmpty() || (existingCategorie.get().getId().equals(id));
    }

    public Page<Categorie> getAllCategoriesWithCustomFields(Pageable pageable)
    {
        return categorieRepository.findAllWithCustomFields(pageable);
    }

    // CategorieView
    public Page<CategorieView> getAllCategoriesView(Pageable pageable)
    {
        return categorieViewRepository.findAll(pageable);
        //return categorieRepository.findAllSpecific(pageable);
    }

    @Transactional
    public void deleteCategoryById(Long id)
    {
        if (categorieRepository.existsById(id))
        {
            categorieRepository.deleteById(id);
        }
        else
        {
            throw new IllegalArgumentException("La catégorie avec l'ID " + id + " n'existe pas.");
        }
    }


    // Méthode pour vérifier si la catégorie est un enfant de la catégorie parente
    public boolean isChildCategory(Long categoryId, Long parentCategoryId)
    {
        // Récupérez la catégorie parente
        Categorie parentCategory = categorieRepository.findById(parentCategoryId).orElse(null);

        // Si la catégorie parente n'existe pas, alors la catégorie n'est pas un enfant
        if (parentCategory == null)
        {
            return false;
        }

        // Vérifiez si la catégorie est un enfant de manière récursive
        return isChildCategoryRecursive(categoryId, parentCategory);
    }

    // Méthode récursive pour vérifier si la catégorie est un enfant
    private boolean isChildCategoryRecursive(Long categoryId, Categorie parentCategory)
    {
        // Vérifiez si la catégorie spécifiée est un enfant de la catégorie parente
        if (parentCategory.getId().equals(categoryId))
        {
            return true;
        }

        // Si la catégorie parente a des enfants, vérifiez récursivement chaque enfant
        for (Categorie childCategory : parentCategory.getEnfants())
        {
            if (isChildCategoryRecursive(categoryId, childCategory))
            {
                return true;
            }
        }

        // La catégorie n'est pas un enfant
        return false;
    }
}

