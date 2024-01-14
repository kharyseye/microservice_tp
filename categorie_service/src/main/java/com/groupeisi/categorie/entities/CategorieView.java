package com.groupeisi.categorie.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("SELECT id, nom, description, depth, nbre_produit FROM categorie_view")
public class CategorieView
{
	 @Id
	 private Long id;

	@Column(nullable = false)
	private String nom;
	@Column(nullable = true)
	private String description;

	@Column
	private int depth ;

	@Column
	private int nbre_produit;

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

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public int getNbre_produit()
	{
		return nbre_produit;
	}

	public void setNbre_produit(int nbre_produit)
	{
		this.nbre_produit = nbre_produit;
	}

	public CategorieView() { }

	public CategorieView(Long id, String nom, int nbre_produit, int depth)
	{
		this.id = id;
		this.nom = nom;
		this.nbre_produit = nbre_produit;
		this.depth = depth;
	}
}
