package com.orbidroid.orbidroid_backend.controller;

import com.orbidroid.orbidroid_backend.email.ConfirmationSender;
import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Student;
import com.orbidroid.orbidroid_backend.helper.auth.Password;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;
import com.orbidroid.orbidroid_backend.repository.DoctorRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@Api(tags = "Doctor related", description = "Manage doctors, including searching, adding, updating and deleting doctors.")
public class DoctorController extends EntityController {

    @Autowired
    DoctorRepository doctorRepository;

    // SEARCH methods

    @ApiOperation("Search all doctors.")
    @GetMapping(value = "")
    public ResponseEntity index(
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body("No doctors found.");
        }
        return ResponseEntity.ok(doctors);
    }

    @ApiOperation("Search a doctor specified by the doctor number.")
    @GetMapping(value = "/get")
    public ResponseEntity getDoctor(
            @RequestParam(value = "DocNum") Integer docNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> foundDoctor = doctorRepository.getById(docNum);
        if(foundDoctor.isPresent()){
            return ResponseEntity.ok(foundDoctor.get());
        }else {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + docNum + " found.");
        }
    }

    @ApiOperation("Search a doctor specified by the doctor email.")
    @GetMapping(value = "/get/email")
    public ResponseEntity getDoctorByEmail(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> foundDoctor = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (foundDoctor.isPresent()) {
            return ResponseEntity.ok(foundDoctor.get());
        } else {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
    }

    @ApiOperation("Search a doctor specified by the doctor name.")
    @GetMapping(value = "/get/name")
    public ResponseEntity getDoctorByName(
            @RequestParam(value = "DocName") String docName,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Doctor> foundDoctor = doctorRepository.getByName(docName);
        if (foundDoctor.isEmpty()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor name " + docName + " found.");
        } else {
            return ResponseEntity.ok(foundDoctor);
        }
    }

    // not accessible to admin users but accessible to doctor himself / herself
    @ApiOperation("Search a doctor specified by the doctor email and authenticated by doctor unencrypted password.")
    @GetMapping(value = "/get/email/pwd")
    public ResponseEntity getDoctorByEmailWithDocPwd(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "DocPwd") String docPwd
    ) {
        if (null == docPwd || "".equals(docPwd)) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty.");
        }
        Optional<Doctor> foundDoctor = doctorRepository.getByEmail(docEmail.toLowerCase());
        if(foundDoctor.isPresent()){
            if (foundDoctor.get().getPwd().equals(Password.encrypt(docPwd))) {
                return ResponseEntity.ok(foundDoctor.get());
            } else {
                return ResponseEntity.status(403).body("Password incorrect.");
            }
        } else {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
    }

    @ApiOperation("Authenticate a doctor specified by the doctor email and doctor's unencrypted password.")
    @GetMapping(value = "/authenticate")
    public ResponseEntity docAuth(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "DocPwd") String oriPwd
    ) {
        if (null == oriPwd || "".equals(oriPwd)) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty.");
        }
        Optional<Doctor> foundDoctor = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (!foundDoctor.isPresent()) {
            return ResponseEntity.badRequest().body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        } else if (Password.encrypt(oriPwd).equals(foundDoctor.get().getPwd())) {
            return ResponseEntity.ok().body("OK");
        } else {
            return ResponseEntity.status(403).body("Password incorrect.");
        }
    }

    // ADD methods
    /*
    @ApiOperation("Admin channel to register a doctor. " +
            "Add a doctor with password. " +
            "Gender should be either Male or Female. " +
            "Pwd should be unencrypted.")
    @PostMapping(value = "/add")
    public ResponseEntity add(
            @RequestParam(value = "DocName") String docName,
            @RequestParam(value = "DocGender") String docGender,
            @RequestParam(value = "DocContact") String docContact,
            @RequestParam(value = "DocPos") String docPos,
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "DocPwd") String docPwd,
            @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!Bijection.isGenderCorrect(docGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }

        if (!Bijection.isPosCorrect(docPos)) {
            return ResponseEntity.badRequest().body("Incorrect pos format.");
        }

        if (haveDuplicate(docEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }

        sendAppointmentConfirmationEmailAsync(docName, docEmail, docContact, docPos, docGender);

        return ResponseEntity.status(201).body(doctorRepository.save(new Doctor(docName,
                docGender, docContact, docPos, docEmail.toLowerCase(), Password.encrypt(docPwd))));
    }
    */
    @ApiOperation("Appoint a doctor with default password.")
    @PostMapping(value = "/add/defaultPwd")
    public ResponseEntity addWithDefaultPwd(
            @RequestParam(value = "DocName") String docName,
            @RequestParam(value = "DocGender") String docGender,
            @RequestParam(value = "DocContact") String docContact,
            @RequestParam(value = "DocPos") String docPos,
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!Bijection.isGenderCorrect(docGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
        /*
        if (!Bijection.isPosCorrect(docPos)) {
            return ResponseEntity.badRequest().body("Incorrect position format.");
        }
        */

        if (haveDuplicate(docEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }

        sendAppointmentConfirmationEmailAsync(docName, docEmail, docContact, docPos, docGender);

        return ResponseEntity.status(201).body(doctorRepository.save(new Doctor(docName,
                docGender, docContact, docPos, docEmail.toLowerCase(), Password.encrypt(Bijection.getDefaultPwdForDoc()))));
    }

/*
    @Deprecated
    @ApiOperation("This is for testing purposes. Add a doctor with password. Gender should be either Male or Female. " +
            "Pwd should be unencrypted.")
    @PostMapping(value = "/withPwd")
    public ResponseEntity addToDoctorWithPwd(
                                        @RequestParam(value = "DocName") String docName,
                                        @RequestParam(value = "DocGender") String docGender,
                                        @RequestParam(value = "DocContact") String docContact,
                                        @RequestParam(value = "DocPos") String docPos,
                                        @RequestParam(value = "DocEmail") String docEmail,
                                        @RequestParam(value = "DocPwd") String docPwd,
                                        @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }

        if (!Bijection.isGenderCorrect(docGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }

        if (!Bijection.isPosCorrect(docPos)) {
            return ResponseEntity.badRequest().body("Incorrect pos format.");
        }

        if (haveDuplicate(docEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }

        sendAppointmentConfirmationEmailAsync(docName, docEmail, docContact, docPos, docGender);

        return ResponseEntity.status(201).body(doctorRepository.save(new Doctor(docName,
                docGender, docContact, docPos, docEmail.toLowerCase(), docPwd)));
    }
    */
    /*
    @Deprecated
    @ApiOperation("This is for testing purposes. Add a doctor without password. Gender should be either Male or Female.")
    @PostMapping(value = "/withoutPwd")
    public ResponseEntity addToDoctorWithoutPwd(
            @RequestParam(value = "DocName") String docName,
            @RequestParam(value = "DocGender") String docGender,
            @RequestParam(value = "DocContact") String docContact,
            @RequestParam(value = "DocPos") String docPos,
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!Bijection.isGenderCorrect(docGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }

        if (!Bijection.isPosCorrect(docPos)) {
            return ResponseEntity.badRequest().body("Incorrect pos format.");
        }

        if (haveDuplicate(docEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }
        sendAppointmentConfirmationEmailAsync(docName, docEmail, docContact, docPos, docGender);
        return ResponseEntity.status(201).body(doctorRepository.save(new Doctor(docName,
                docGender, docContact, docPos, docEmail.toLowerCase())));
    }
    */

    // DELETE methods
    /*
    @Deprecated
    @Transactional
    @ApiOperation("Backend testing purposes. Don't use it.")
    @DeleteMapping(value = "/all")
    public ResponseEntity removeAll(
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        doctorRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
    */

    @Transactional
    @ApiOperation("Delete a doctor specified by the doctor number.")
    @DeleteMapping(value = "")
    public ResponseEntity removeDoctor(@RequestParam(value = "DocNum") Integer docNum,
                                       @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> doc = doctorRepository.getById(docNum);
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + docNum + " found.");
        }
        doctorRepository.removeById(docNum);
        return ResponseEntity.ok().build();
    }

    // CHANGE methods

    @PutMapping(value = "/name")
    @ApiOperation("Update a doctor's name specified by the doctor number.")
    public ResponseEntity updateName(@RequestParam(value = "DocNum") Integer docNum,
                                     @RequestParam(value = "DocName") String newName,
                                     @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        if (newName.equals(optionalDoctor.get().getName())) {
            return ResponseEntity.badRequest().body("Old name and new name cannot be the same.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        foundDoctor.setName(newName);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    /*
    @Deprecated
    @PutMapping(value = "/gender")
    @ApiOperation("Update a doctor's gender specified by the doctor number.")
    public ResponseEntity updateGender(@RequestParam(value = "DocNum") Integer docNum,
                                       @RequestParam(value = "DocGender") String newGender,
                                       @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!Bijection.isGenderCorrect(newGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (newGender.equals(foundDoctor.getGender())) {
            return ResponseEntity.badRequest().body("Old gender and new gender cannot be the same.");
        }
        foundDoctor.setGender(newGender);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }
    */

    @PutMapping(value = "/pos")
    @ApiOperation("Update a doctor's position specified by the doctor number.")
    public ResponseEntity updatePos(@RequestParam(value = "DocNum") Integer docNum,
                                    @RequestParam(value = "DocPos") String newPos,
                                    @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!Bijection.isPosCorrect(newPos)) {
            return ResponseEntity.badRequest().body("Position format incorrect.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (newPos.equals(foundDoctor.getPos())) {
            return ResponseEntity.badRequest().body("New position and old position cannot be the same.");
        }
        foundDoctor.setPos(newPos);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    @PutMapping(value = "/contact")
    @ApiOperation("Update a doctor's contact specified by the doctor number.")
    public ResponseEntity updateContact(@RequestParam(value = "DocNum") Integer docNum,
                                        @RequestParam(value = "DocContact") String newContact,
                                        @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (newContact.equals(foundDoctor.getContact())) {
            return ResponseEntity.badRequest().body("New contact and old contact cannot be the same.");
        }
        foundDoctor.setContact(newContact);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    // Admin channel to update a doctor's email.
    @PutMapping(value = "/email")
    @ApiOperation("Update a doctor's email specified by the doctor number.")
    public ResponseEntity updateEmail(@RequestParam(value = "DocNum") Integer docNum,
                                      @RequestParam(value = "DocEmail") String newEmail,
                                      @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (haveDuplicate(newEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (newEmail.toLowerCase().equals(foundDoctor.getEmail())) {
            return ResponseEntity.badRequest().body("New email and old email cannot be the same.");
        }
        foundDoctor.setEmail(newEmail.toLowerCase());
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    @PutMapping(value = "/pwd/num")
    @ApiOperation("Update a doctor's password specified by the doctor number.")
    public ResponseEntity updatePwd(@RequestParam(value = "DocNum") Integer docNum,
                                            @RequestParam(value = "NewPwd") String newPwd,
                                            @RequestParam(value = "OriPwd") String oriPwd) {

        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (!Password.encrypt(oriPwd).equals(foundDoctor.getPwd())) {
            return ResponseEntity.status(403).body("Password incorrect.");
        }
        foundDoctor.setPwd(newPwd);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    @PutMapping(value = "/pwd/email")
    @ApiOperation("Update a doctor's password specified by the doctor email.")
    public ResponseEntity updatePwdUsingEmail(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "NewPwd") String newPwd,
            @RequestParam(value = "OriPwd") String oriPwd
    ) {

        Optional<Doctor> optionalDoctor = doctorRepository.getByEmail(docEmail.toLowerCase());
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        if (!Password.encrypt(oriPwd).equals(foundDoctor.getPwd())) {
            return ResponseEntity.status(403).body("Password incorrect.");
        }
        foundDoctor.setPwd(newPwd);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }

    // Admin channel to change a doctor's password, should not be functional
    /*
    @Deprecated
    @PutMapping(value = "/pwd/admin")
    @ApiOperation("Update a doctor's password specified by the doctor number.")
    public ResponseEntity updatePwdForAdmin(@RequestParam(value = "DocNum") Integer docNum,
                                    @RequestParam(value = "DocPwd") String newPwd,
                                    @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.getById(docNum);
        if(!optionalDoctor.isPresent()){
            return ResponseEntity.badRequest().body("No doctor with specified doctor number " + docNum + " found.");
        }
        Doctor foundDoctor = optionalDoctor.get();
        foundDoctor.setPwd(newPwd);
        return ResponseEntity.ok(doctorRepository.save(foundDoctor));
    }
    */

    // HELPER methods

    // Just input the original email, and this method will do the lower case transformation for you.
    private boolean haveDuplicate(String docEmail) {
        List<Doctor> doctors = doctorRepository.findAll();
        for (Doctor doc : doctors) {
            if (doc.getEmail().equals(docEmail.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void sendAppointmentConfirmationEmailAsync(String docName, String docEmail, String docContact, String docPos, String docGender) {
        Thread newThread = new Thread(() -> {
            ConfirmationSender.sendDoctorAppointmentConfirmationEmail(docName, docEmail, docContact, docPos, docGender);
        });
        newThread.start();
    }
}
