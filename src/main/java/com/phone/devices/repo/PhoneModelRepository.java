package com.phone.devices.repo;

import com.phone.devices.entity.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing phone model entities.
 */
@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, String> {
}
