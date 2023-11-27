package com.jakala.distributor.repository;

import java.util.HashSet;

import org.springframework.data.repository.CrudRepository;

import com.jakala.distributor.model.User;

public interface UserRepository extends CrudRepository<User, Integer> 
{
	HashSet<User> findByNameContainingOrSurnameContainingOrTypeContaining(String keyword1, String keyword2, String keyword3);
}
