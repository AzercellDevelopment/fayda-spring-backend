package com.fayda.command.repository;

import com.fayda.command.constants.CouponCodeStatus;
import com.fayda.command.model.CouponCodesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponCodeRepository extends CrudRepository<CouponCodesModel, UUID> {

  Optional<CouponCodesModel> findFirstByReferenceIdAndStatus(String id, CouponCodeStatus status);
}
