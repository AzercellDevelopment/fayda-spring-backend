package com.fayda.command.repository;

import com.fayda.command.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {

  boolean existsTransactionModelByUserId(UUID userId);
}
