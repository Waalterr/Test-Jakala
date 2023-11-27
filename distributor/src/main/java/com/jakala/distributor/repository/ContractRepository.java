package com.jakala.distributor.repository;

import java.time.LocalDate;
import java.util.HashSet;
import org.springframework.data.repository.CrudRepository;

import com.jakala.distributor.model.Contract;

public interface ContractRepository extends CrudRepository<Contract, Integer>
{
	HashSet<Contract> findByStartDate(LocalDate key);
	HashSet<Contract> findByTypeContaining(String key);
}
