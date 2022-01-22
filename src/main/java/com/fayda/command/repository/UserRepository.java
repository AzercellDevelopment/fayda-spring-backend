package com.fayda.command.repository;

import com.fayda.command.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserModel, UUID> {

  Optional<UserModel> findByEmail(String email);

}
