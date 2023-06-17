package org.itstep.addressservice.repository;

import org.itstep.addressservice.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
