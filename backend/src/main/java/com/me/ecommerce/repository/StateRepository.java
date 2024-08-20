package com.me.ecommerce.repository;

import com.me.ecommerce.entity.State;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

//    @Query("SELECT s FROM State s join s.country c where c.code = :code")
    @Query("SELECT s FROM State s WHERE s.country.code = :code")
    List<State> findStatesByCountryCode(@Param("code") String code);
}
