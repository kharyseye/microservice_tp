package com.groupeisi.categorie.repositories;

import com.groupeisi.categorie.entities.CategorieView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CategorieViewRepository extends JpaRepository<CategorieView, Long>
{
    Page<CategorieView> findAll(Pageable pageable);

    Optional<CategorieView> findByNom(String nom);

}
