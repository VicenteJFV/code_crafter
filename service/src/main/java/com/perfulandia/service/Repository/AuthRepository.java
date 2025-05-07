package com.perfulandia.service.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.perfulandia.service.Entity.Auth;

@Repository
// @Query("SELECT a FROM Auth a WHERE a.username = :username OR a.email = :email")
public interface AuthRepository extends JpaRepository<Auth, Long>, JpaSpecificationExecutor<Auth> {
    Optional<Auth> findByUsernameOrEmail(String username, String email);
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraphType.FETCH)
    Optional<Auth> findByUsername(String username);
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraphType.FETCH)
    Optional<Auth> findByEmail(String email);
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraphType.FETCH)
    @Query("SELECT a FROM Auth a WHERE a.username = :username OR a.email = :email")
    Optional<Auth> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    

}
