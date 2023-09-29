package com.testAssignment.demo.repository;

import com.testAssignment.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

   @Query("SELECT u FROM Users u WHERE u.birthDate >= :fromDate AND u.birthDate <= :toDate")
   List<Users> findUsersByBirthDate(LocalDate fromDate, LocalDate toDate);
}