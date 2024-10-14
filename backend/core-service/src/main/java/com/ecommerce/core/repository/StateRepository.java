package com.ecommerce.core.repository;

import com.ecommerce.core.entity.State;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, UUID> {

//    @Query("SELECT s FROM State s join s.country c where c.code = :code")
    @Query("select s from State s where s.country.code = :code order by s.id")
    List<State> findStatesByCountryCodeOrderById(@Param("code") String code);
}
