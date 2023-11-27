package com.jakala.distributor.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jakala.distributor.model.Contract;
import com.jakala.distributor.model.ContractRequest;
import com.jakala.distributor.model.User;
import com.jakala.distributor.repository.ContractRepository;
import com.jakala.distributor.repository.UserRepository;

@RestController
@RequestMapping("/distributor")
public class Controller 
{
	@Autowired
	ContractRepository contractRepo;
	@Autowired 
	UserRepository userRepo;
	
	
	/*
	 * Develop one API to insert new contracts.
	 * **A new contract can be made by a private user or business users**
	 */
	@PostMapping("/addcontracts")
	public ResponseEntity<List<Contract>> insertContract(@RequestBody ContractRequest request)
	{
		Optional<User> user = userRepo.findById(request.getUser().getID());
		
		if(user.isEmpty())
			return new ResponseEntity<List<Contract>>(HttpStatus.NOT_FOUND);
		
		if(!user.get().canCreateContracts())
			return new ResponseEntity<List<Contract>>(HttpStatus.FORBIDDEN);
		
		for(Contract c : request.getContracts())
			if(!c.isValid())
				return new ResponseEntity<List<Contract>>(HttpStatus.BAD_REQUEST);
		
		contractRepo.saveAll(request.getContracts());		
		return new ResponseEntity<List<Contract>>(request.getContracts(), HttpStatus.OK);
	}	
	
	/*
	 * 2. Develop one API to search contracts with the possibility to search by:
	 *		a. Customer’s name
	 *		b. Date of start contract.
	 *		c. Type of contract (gas |electricity | gas and electricity)
	 *		d. Type of user (private | business)
	 */
	@GetMapping("/searchcontracts")
	public ResponseEntity<Set<Contract>> searchContracts(
															@RequestParam(name="Name", 			required = false)String name,
															@RequestParam(name="Surname", 		required = false)String surname,
															@RequestParam(name="StartedDate", 	required = false)LocalDate startedDate,
															@RequestParam(name="ContractType", 	required = false)String contractType,
															@RequestParam(name="UserType", 		required = false)String userType
														 )
	{
		Set<Contract> matches = new HashSet<>();
		Set<Contract> results = new HashSet<>();	
		for(User user : userRepo.findAll())
		{
			if((name==null || user.getName().equalsIgnoreCase(name)) 			&&
			   (surname==null || user.getSurname().equalsIgnoreCase(surname)) 	&&
			   (userType==null || user.getType().equalsIgnoreCase(userType)))
			{
				matches.addAll(user.getContracts());
			}
		}
		
		for(Contract contract : matches) 
		{      
	        if((startedDate==null || contract.getStartDate().equals(startedDate))	&&
	           (contractType==null || contract.getType().equalsIgnoreCase(contractType)))
	        {
	            results.add(contract);
	        }
		}
		
		if(results.isEmpty())
			return new ResponseEntity<Set<Contract>>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Set<Contract>>(results, HttpStatus.OK);
	}	
	
	// Metodo creato inizialmente per il secondo punto della specifica. Funziona, ma non rispetta completamente la specifica. Usare quello sopra.
	@Deprecated
	@GetMapping("/searchcontracts/{keyword}")
	public ResponseEntity<Set<Contract>> findByKeyword(@PathVariable("keyword")String keyword)
	{
		HashSet<Contract> results = new HashSet<>();
		
		try
		{
			LocalDate keyToDate = LocalDate.parse(keyword);
			results.addAll(contractRepo.findByStartDate(keyToDate));
			
			if(results.isEmpty())
				return new ResponseEntity<Set<Contract>>(HttpStatus.NOT_FOUND);
			
			return new ResponseEntity<Set<Contract>>(results, HttpStatus.OK);			
			
		}catch(DateTimeParseException e)
		{
			if(Contract.VALID_TYPES.contains(keyword.toLowerCase()))
				results.addAll(contractRepo.findByTypeContaining(keyword));
				
			for(User u : userRepo.findByNameContainingOrSurnameContainingOrTypeContaining(keyword, keyword, keyword))
				results.addAll(u.getContracts());
			
			if(results.isEmpty())
				return new ResponseEntity<Set<Contract>>(HttpStatus.NOT_FOUND);
			
			return new ResponseEntity<Set<Contract>>(results, HttpStatus.OK);
		}	
	}
	
	// 3. Develop one API that retrieves the contracts of one user.
	@GetMapping("/contracts/{id}")
	public ResponseEntity<List<Contract>> findByUserId(@PathVariable("id")int id)
	{
		Optional<User> user = userRepo.findById(id);
		if(user.isEmpty())
			return new ResponseEntity<List<Contract>>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Contract>>(user.get().getContracts(), HttpStatus.OK);
	}
	
}