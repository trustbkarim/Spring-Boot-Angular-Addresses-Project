package com.firstapp.firstapp.repositories;

import com.firstapp.firstapp.ws.entities.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>
{
    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
    
    @Query(value = "select * from users u where u.first_name like 'bouarfa'", nativeQuery = true)
    Page<UserEntity> findAllUserByFirstName(Pageable pageableRequest);
}
