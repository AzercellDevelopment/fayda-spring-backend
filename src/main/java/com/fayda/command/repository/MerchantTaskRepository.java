package com.fayda.command.repository;

import com.fayda.command.constants.MerchantTaskStatuses;
import com.fayda.command.model.MerchantTaskModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantTaskRepository extends CrudRepository<MerchantTaskModel, UUID> {

  Optional<MerchantTaskModel> findFirstByUserIdAndStatus(UUID userId, MerchantTaskStatuses status);

}
