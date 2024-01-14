package com.groupeisi.categorie.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorie")
public class Categorie
{
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	@Column(nullable = false)
	private String nom;
	@Column(nullable = true)
	private String description;

	@Column(insertable=false, updatable=false, nullable = true)
	private Long categorie_id;

	@Formula("(SELECT categorie_view.depth FROM categorie_view WHERE categorie_view.id=id)")
	private Integer depth;

	@Formula("(SELECT COALESCE(COUNT(*), 0) FROM produit WHERE produit.categorie_id=id)")
	private Long nbre_produit;


	@ManyToOne
	@JsonBackReference
	@JsonManagedReference
	@JoinColumn(name = "categorie_id", nullable = true)
	@JsonIgnoreProperties({"enfants"})
	private Categorie categorieParent;

	@OneToMany(mappedBy = "categorie_id")
	@JsonIgnoreProperties({"enfants", "categorieParent"}) // Peut être supprimer, pas de regression
	// @JsonManagedReference
	private List<Categorie> enfants = new ArrayList<>();


	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getNom()
	 {
		 return nom;
	 }
	
	 public void setNom(String nom) 
	 {
		this.nom = nom;
	 }
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}

	public Categorie getCategorieParent()
	{
		return categorieParent;
	}

	public void setCategorieParent(Categorie categorieParent)
	{
		this.categorieParent = categorieParent;
	}


	public List<Categorie> getEnfants()
	{
		return enfants;
	}

	public void setEnfants(List<Categorie> enfants)
	{
		this.enfants = enfants;
	}


	public Integer getDepth()
	{
		return depth;
	}

	public void setDepth(Integer depth)
	{
		this.depth = depth;
	}

	public Long getNbre_produit()
	{
		return nbre_produit;
	}

	public void setNbre_produit(Long nbre_produit)
	{
		this.nbre_produit = nbre_produit;
	}

	/*@OneToMany(mappedBy = "categorieParent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Produit> produits;
*/
	public Categorie() { }

	public Categorie(Long id, String nom, Long nbre_produit, Integer depth)
	{
		this.id = id;
		this.nom = nom;
		this.nbre_produit = nbre_produit;
		this.depth = depth;
	}

	public Categorie(Long id, String nom, String description, Long categorie_id, Integer depth, Long nbre_produit)
	{
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.categorie_id = categorie_id;
		this.nbre_produit = nbre_produit;
		this.depth = depth;
	}


	@JsonIgnore
	public List<Categorie> getCategorieParents()
	{
		List<Categorie> parents = new ArrayList<>();
		Categorie parent = this.getCategorieParent();

		while (parent != null)
		{
			parents.add(parent);
			parent = parent.getCategorieParent();
		}

		return parents;
	}
}
