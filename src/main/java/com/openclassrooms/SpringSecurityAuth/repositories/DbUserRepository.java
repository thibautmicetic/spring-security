package com.openclassrooms.SpringSecurityAuth.repositories;

import com.openclassrooms.SpringSecurityAuth.models.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbUserRepository extends JpaRepository<DbUser, Integer> {
    public DbUser findByUsername(String username);
}
