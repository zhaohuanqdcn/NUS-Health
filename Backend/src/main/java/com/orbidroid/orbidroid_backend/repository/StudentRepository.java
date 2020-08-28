package com.orbidroid.orbidroid_backend.repository;

import com.orbidroid.orbidroid_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    public static final String DELETE_BY_STU_ID = "DELETE FROM students where id=:stuNum";

    Optional<Student> getByEmail(String email);

    Optional<Student> getById(Integer num);

    @Transactional
    @Modifying
    @Query(value = DELETE_BY_STU_ID, nativeQuery = true)
    void deleteByStuId(
            @Param("stuNum") Integer stuNum
    );

    Optional<Student> getByContact(String contact);

    List<Student> getByName(String name);
}
