package com.jakala.distributor.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User 
{
	private static final Set<String> VALID_TYPES = new HashSet<>(Arrays.asList("business user", "private user", "other"));
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name, surname, type;
	private LocalDate dob;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Contract> contracts = new ArrayList<>();

	
	public int getID() 
	{
		return id;
	}

	public void setID(int id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getSurname() 
	{
		return surname;
	}

	public void setSurname(String surname) 
	{
		this.surname = surname;
	}

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public LocalDate getDob() 
	{
		return dob;
	}

	public void setDob(LocalDate dob) 
	{
		this.dob = dob;
	}

	public List<Contract> getContracts() 
	{
		return contracts;
	}

	public void setContracts(ArrayList<Contract> contracts) 
	{
		this.contracts = contracts;
	}
	
	public boolean isValid()
	{
		return	name != null 		&&
				!name.isBlank() 	&&
				surname != null 	&&
				!surname.isBlank() 	&&
				dob != null			&&
				VALID_TYPES.contains(type.toLowerCase());				
	}
	
	public boolean canCreateContracts()
	{
		return 	type.equalsIgnoreCase("business user") ||
				type.equalsIgnoreCase("private user");				
	}
		
}