package com.fayda.command.repository;

import com.fayda.command.model.CouponModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends CrudRepository<CouponModel, UUID> {

  List<CouponModel> findAllByIsActive(boolean isActive);

  Optional<CouponModel> findFirstByStaticId(String staticId);
}
