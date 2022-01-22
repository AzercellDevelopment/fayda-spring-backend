package com.fayda.command.repository;

import com.fayda.command.model.MerchantModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantDefinitionRepository extends CrudRepository<MerchantModel, UUID> {

  List<MerchantModel> findAllByIsActiveTrue();
}
