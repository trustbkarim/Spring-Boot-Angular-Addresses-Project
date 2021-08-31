package com.firstapp.firstapp.repositories;

import com.firstapp.firstapp.ws.entities.AddressEntity;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

}
