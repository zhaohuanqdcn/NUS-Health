package com.orbidroid.orbidroid_backend.repository;

import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    Optional<Doctor> getById(Integer id);

    Optional<Doctor> getByEmail(String docEmail);

    String removeById(Integer docNum);

    Optional<Doctor> getByContact(String docContact);

    List<Doctor> getByName(String docName);
}