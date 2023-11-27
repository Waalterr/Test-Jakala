package com.jakala.distributor.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Contract 
{
	public static final Set<String> VALID_TYPES = new HashSet<>(Arrays.asList("gas", "electricity", "gas and electricity"));
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String type;
	private LocalDate startDate, endDate;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public LocalDate getStartDate() 
	{
		return startDate;
	}

	public void setStartDate(LocalDate startDate) 
	{
		this.startDate = startDate;
	}

	public LocalDate getEndDate() 
	{
		return endDate;
	}

	public void setEndDate(LocalDate endDate) 
	{
		this.endDate = endDate;
	}
	
	public void setUser(User user) 
	{
		this.user = user;
	}	
	
	public boolean isValid()
	{
		return 	startDate 	!= null	&&
				endDate 	!= null	&&
				user 		!= null	&&
				VALID_TYPES.contains(type.toLowerCase());
	}
}