package com.sonumangal.tech.repo;

import com.sonumangal.tech.entity.ApiEntity;
import com.sonumangal.tech.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u join fetch u.apiEntities where u.userKey = :userKey")
    Optional<UserEntity> findByUserKey(@Param("userKey") String userKey);

    /*@Query("select a from UserEntity u left join u.apiEntities a where u.userKey = :userKey")
    List<ApiEntity> findByUserKey(@Param("userKey") String userKey);*/
}
