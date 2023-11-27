package com.jakala.distributor.model;

import java.util.ArrayList;
import java.util.List;

public class ContractRequest 
{
	private User user;
	private List<Contract> contracts = new ArrayList<>();
	
	public User getUser() 
	{
		return user;
	}
	
	public void setUser(User user) 
	{
		this.user = user;
	}
	
	public List<Contract> getContracts() 
	{
		return contracts;
	}
	
	public void setContracts(List<Contract> contracts) 
	{
		this.contracts = contracts;
	}
		
}