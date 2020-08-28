package com.orbidroid.orbidroid_backend.controller;

import com.orbidroid.orbidroid_backend.email.ConfirmationSender;
import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Student;
import com.orbidroid.orbidroid_backend.helper.auth.Password;
import com.orbidroid.orbidroid_backend.email.Email;
import com.orbidroid.orbidroid_backend.email.VeriCode;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;
import com.orbidroid.orbidroid_backend.repository.StudentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/student")

@Api(tags = "Student related", description = "Manage students, including searching, adding, updating and deleting students.")
public class StudentController extends EntityController {

    @Autowired
    StudentRepository studentRepository;

    // Cache for student's email registration authentication feature
    HashMap<String, VeriCode> stuEmailCache = new HashMap<>();

    // SEARCH methods
    @ApiOperation("Search all students.")
    @GetMapping(value = "")
    public ResponseEntity index(
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No student found.");
        }
        return ResponseEntity.ok(students);
    }

    @ApiOperation("Search a student specified by the student number.")
    @GetMapping(value = "/get")
    public ResponseEntity getStudent(
            @RequestParam(value = "StuNum") Integer stuNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> foundStudent = studentRepository.getById(stuNum);
        if(foundStudent.isPresent()){
            return ResponseEntity.ok(foundStudent.get());
        } else {
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
    }

    @ApiOperation("Search a student specified by the student name.")
    @GetMapping(value = "/get/name")
    public ResponseEntity getStudentByName(
            @RequestParam(value = "StuName") String stuName,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Student> foundStudents = studentRepository.getByName(stuName);
        if(!foundStudents.isEmpty()){
            return ResponseEntity.ok(foundStudents);
        } else {
            return ResponseEntity.status(404).body("No student with specified student name " + stuName + " found.");
        }
    }

    @ApiOperation("Search a student specified by the student email.")
    @GetMapping(value = "/getByEmail")
    public ResponseEntity getByEmail(
            @RequestParam(value = "StuEmail") String stuEmail,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> foundStudent = studentRepository.getByEmail(stuEmail.toLowerCase());
        if (foundStudent.isPresent()) {
            return ResponseEntity.ok(foundStudent.get());
        } else {
            return ResponseEntity.status(404).body("No student with specified student email " + stuEmail.toLowerCase() + " found.");
        }
    }

    @ApiOperation("Search a student specified by the student email and the student password.")
    @GetMapping(value = "/getByEmailWithPwd")
    public ResponseEntity getByEmailWithPwd(
            @RequestParam(value = "StuEmail") String stuEmail,
            @RequestParam(value = "StuPwd") String stuPwd
    ) {
        Optional<Student> foundStudent = studentRepository.getByEmail(stuEmail.toLowerCase());
        if (foundStudent.isPresent()) {
            if (foundStudent.get().getPwd().equals(Password.encrypt(stuPwd))) {
                return ResponseEntity.ok(foundStudent.get());
            } else {
                return ResponseEntity.status(403).body("Password incorrect");
            }
        } else {
            return ResponseEntity.status(404).body("No student with specified student email " + stuEmail.toLowerCase() + " found.");
        }
    }


    @ApiOperation("Authenticate a student with email and original password.")
    @GetMapping(value = "/authenticate")
    public ResponseEntity stuAuth(
            @RequestParam(value = "StuEmail") String stuEmail,
            @RequestParam(value = "StuPwd") String oriPwd
    ) {
        if (null == oriPwd || "".equals(oriPwd)) {
            return ResponseEntity.badRequest().body("Original password cannot be null or empty.");
        }
        Optional<Student> foundStudent = studentRepository.getByEmail(stuEmail.toLowerCase());
        if (!foundStudent.isPresent()) {
            return ResponseEntity.badRequest().body("No student with specified student email " + stuEmail.toLowerCase() + " found.");
        } else if (Password.encrypt(oriPwd).equals(foundStudent.get().getPwd())) {
            return ResponseEntity.ok().body("OK");
        } else {
            return ResponseEntity.status(403).body("password incorrect");
        }
    }

    // ADD methods

    // add method (test version)
    /*
    @Deprecated
    @ApiOperation("For testing purposes only. Register a student. Gender should be either Male or Female. Pwd should be unencrypted. The encrypted " +
            "password is stored in the database. After the final product is released you should use the more advanced function" +
            " with email authentication instead.")
    @PostMapping(value = "/add")
    public ResponseEntity addToStudent(
                                        @RequestParam(value = "StuName") String stuName,
                                        @RequestParam(value = "StuGender") String stuGender,
                                        @RequestParam(value = "StuContact") String stuContact,
                                        @RequestParam(value = "StuEmail") String stuEmail,
                                        @RequestParam(value = "StuPwd") String stuPwd,
                                        @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (null == stuPwd || "".equals(stuPwd)) {
            return ResponseEntity.badRequest().body("Original password cannot be null or empty.");
        }
        if (Bijection.isGenderCorrect(stuGender)) {
            if (haveDuplicate(stuEmail)) {
                return ResponseEntity.badRequest().body("Duplicate email address.");
            }
            return ResponseEntity.status(201).body(studentRepository.save(new Student(stuName,
                    stuGender, stuContact, stuEmail.toLowerCase(), Password.encrypt(stuPwd))));
        } else {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
    }
     */

    // two consecutive methods to realize registration feature
    // no admin token is needed for these two consecutive functions
    @Deprecated
    @ApiOperation("Register a student with NUS email authentication. This route is for sending out a verification code.")
    @PostMapping(value = "/add/email/withNusAuth")
    public ResponseEntity addStudenEmailSending (
            @RequestParam(value = "StuEmail") String stuEmail
    ) {
            if (!Email.isNusDomain(stuEmail.toLowerCase())) {
                return ResponseEntity.status(403).body("Non-nus users cannot register.");
            }
            if (haveDuplicate(stuEmail)) {
                return ResponseEntity.badRequest().body("Duplicate email address.");
            }
            // email sending logic
            try {
                VeriCode vc = new VeriCode(stuEmail.toLowerCase());
                stuEmailCache.put(stuEmail.toLowerCase(), vc);
                return ResponseEntity.ok().body(String.format("Email sent"));
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("An exception is occurred while sending email.");
            }
    }

    @ApiOperation("Register a student after the authentication code is sent.")
    @PostMapping(value = "/add/email/after")
    public ResponseEntity addStudenAfterEmailSending (
            @RequestParam(value = "StuName") String stuName,
            @RequestParam(value = "StuGender") String stuGender,
            @RequestParam(value = "StuContact") String stuContact,
            @RequestParam(value = "StuEmail") String stuEmail,
            @RequestParam(value = "StuPwd") String stuPwd,
            @RequestParam(value = "VeriCode") String veriCode) {

        if (null == stuPwd || "".equals(stuPwd)) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty.");
        }
        if (Bijection.isGenderCorrect(stuGender)) {
            // Actually this check is already done, but considering there might be a timeout,
            // we should check if it is unique here again.
            if (haveDuplicate(stuEmail)) {
                return ResponseEntity.badRequest().body("Duplicate email address");
            }
            if (haveDuplicateContact(stuContact)) {
                return ResponseEntity.badRequest().body("Duplicate contact number");
            }

            if (stuEmailCache.containsKey(stuEmail.toLowerCase())) {
                if (stuEmailCache.get(stuEmail.toLowerCase()).isVerCodeCorrect(veriCode)) {
                    if (stuEmailCache.get(stuEmail.toLowerCase()).isExpired()) {
                        stuEmailCache.remove(stuEmail.toLowerCase());
                        return ResponseEntity.status(403).body(String.format("Timeout (>%d seconds)", Bijection.getVeriCodeTimeOutInMinutes() * 60));
                    }
                    // delete content in our cache
                    stuEmailCache.remove(stuEmail.toLowerCase());
                    // add the student
                    studentRepository.save(new Student(stuName,
                            stuGender, stuContact, stuEmail.toLowerCase(), Password.encrypt(stuPwd)));
                    sendRegistrationSuccessEmailAsync(stuEmail, stuName, stuContact, stuGender);
                    return ResponseEntity.status(201).body("Sign up successfully");
                } else {
                    return ResponseEntity.status(403).body("false verification code");
                }
            } else {
                return ResponseEntity.status(404).body("Student VeriCode not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
    }

    // two consecutive methods to realize registration feature
    // no admin token is needed for these two consecutive functions
    @ApiOperation("Register a student with email authentication feature. This route is for sending out a verification code.")
    @PostMapping(value = "/add/email")
    public ResponseEntity addStudenEmailSendingWithOutNusAuth (
            @RequestParam(value = "StuEmail") String stuEmail
    ) {
        if (haveDuplicate(stuEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }
        // email sending logic
        try {
            VeriCode vc = new VeriCode(stuEmail.toLowerCase());
            stuEmailCache.put(stuEmail.toLowerCase(), vc);
            return ResponseEntity.ok().body(String.format("Email sent", Bijection.getVeriCodeTimeOutInMinutes() * 60));
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An exception is occurred while sending email.");
        }
    }



    // DELETE methods

    /*
    @Deprecated
    @ApiOperation("Dangerous. Backend testing purposes. Delete Token Needed. Don't use it.")
    @DeleteMapping(value = "/all")
    public ResponseEntity removeAll(
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
            studentRepository.deleteAll();
            return ResponseEntity.ok().body("OK");
    }
     */

    @ApiOperation("Delete a student by the student number.")
    @DeleteMapping(value = "/num")
    public ResponseEntity removeById(
        @RequestParam(value = "Token") String token,
        @RequestParam(value = "StuNum") Integer stuNum
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        Optional<Student> stu = studentRepository.getById(stuNum);
        if (!stu.isPresent()) {
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        studentRepository.deleteByStuId(stuNum);
        return ResponseEntity.ok().body("Deleted successfully.");
    }

    @ApiOperation("Delete a student by the student email.")
    @DeleteMapping(value = "/email")
    public ResponseEntity removeByEmail(
            @RequestParam(value = "Token") String token,
            @RequestParam(value = "StuEmail") String stuEmail
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        Optional<Student> stu = studentRepository.getByEmail(stuEmail.toLowerCase());
        if (!stu.isPresent()) {
            return ResponseEntity.status(404).body("No student with specified student email " + stuEmail.toLowerCase() + " found.");
        }
        studentRepository.deleteByStuId(stu.get().getNum());
        return ResponseEntity.ok().body("Deleted successfully.");
    }

    // CHANGE methods

    @ApiOperation("Update a student.")
    @PutMapping(value = "")
    public ResponseEntity update(@RequestParam(value = "StuNum") Integer stuNum,
                                 @RequestParam(value = "NewName") String newName,
                                 @RequestParam(value = "NewGender") String newGender,
                                 @RequestParam(value = "NewContact") String newContact,
                                 @RequestParam(value = "NewEmail") String newEmail,
                                 @RequestParam(value = "NewPwd") String newPwd,
                                 @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (null == newPwd || "".equals(newPwd)) {
            return ResponseEntity.badRequest().body("New password cannot be null or empty.");
        }
        if (!Bijection.isGenderCorrect(newGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if(!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }

        Student foundStudent = optionalStudent.get();
        if (foundStudent.getPwd().equals(Password.encrypt(newPwd))) {
            return ResponseEntity.badRequest().body("New password and old password can not be the same.");
        }

        foundStudent.setName(newName);
        foundStudent.setGender(newGender);
        foundStudent.setContact(newContact);
        foundStudent.setEmail(newEmail.toLowerCase());
        foundStudent.setPwd(Password.encrypt(newPwd));
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }

    @ApiOperation("Update a student's name.")
    @PutMapping(value = "/name")
    public ResponseEntity updateName(@RequestParam(value = "StuNum") Integer stuNum,
                                 @RequestParam(value = "NewName") String newName,
                                     @RequestParam(value = "Token") String adminToken) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if (!optionalStudent.isPresent()) {
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        Student foundStudent = optionalStudent.get();

        if (foundStudent.getName().equals(newName)) {
            return ResponseEntity.badRequest().body("New name and old name can not be the same.");
        }

        foundStudent.setName(newName);
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }
    /*
    @Deprecated
    @ApiOperation("Update a student's gender. This is for testing purposes only. You will need admin access to do it.")
    @PutMapping(value = "/gender")
    public ResponseEntity updateGender(
            @RequestParam(value = "StuNum") Integer stuNum,
            @RequestParam(value = "NewGender") String newGender,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if (!Bijection.isGenderCorrect(newGender)) {
            return ResponseEntity.badRequest().body("Incorrect gender format.");
        }
        if(!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        Student foundStudent = optionalStudent.get();
        if (foundStudent.getGender().equals(newGender)) {
            return ResponseEntity.badRequest().body("New gender and old gender should not be the same.");
        }
        foundStudent.setGender(newGender);
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }
    */

    @ApiOperation("Update a student's contact.")
    @PutMapping(value = "/contact")
    public ResponseEntity updateContact(@RequestParam(value = "StuNum") Integer stuNum,
                                     @RequestParam(value = "NewContact") String newContact,
                                        @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if(!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        Student foundStudent = optionalStudent.get();
        if (foundStudent.getContact().equals(newContact)) {
            return ResponseEntity.badRequest().body("New contact and old contact should not be the same.");
        }
        foundStudent.setContact(newContact);
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }

    @ApiOperation("Update a student's email.")
    @PutMapping(value = "/email")
    public ResponseEntity updateEmail(@RequestParam(value = "StuNum") Integer stuNum,
                                     @RequestParam(value = "NewEmail") String newEmail,
                                      @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (haveDuplicate(newEmail)) {
            return ResponseEntity.badRequest().body("Duplicate email address.");
        }
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if(!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        Student foundStudent = optionalStudent.get();
        if (foundStudent.getEmail().equals(newEmail.toLowerCase())) {
            return ResponseEntity.badRequest().body("New email and old email can not be the same.");
        }
        foundStudent.setEmail(newEmail.toLowerCase());
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }

    @ApiOperation("Update a student's password by student.")
    @PutMapping(value = "/pwd")
    public ResponseEntity updatePwd(   @RequestParam(value = "StuNum") Integer stuNum,
                                       @RequestParam(value = "OldPwd") String oldPwd,
                                       @RequestParam(value = "NewPwd") String newPwd
    ) {
        Optional<Student> optionalStudent = studentRepository.getById(stuNum);
        if (null == oldPwd || "".equals(oldPwd)) {
            return ResponseEntity.badRequest().body("Original password cannot be null or empty.");
        }
        if (null == newPwd || "".equals(newPwd)) {
            return ResponseEntity.badRequest().body("New password cannot be null or empty.");
        }
        if(!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        Student foundStudent = optionalStudent.get();
        if (foundStudent.getPwd().equals(Password.encrypt(newPwd))) {
            return ResponseEntity.badRequest().body("New password and old password can not be the same.");
        }

        if (!foundStudent.getPwd().equals(Password.encrypt(oldPwd))) {
            return ResponseEntity.badRequest().body("Original password incorrect.");
        }
        foundStudent.setPwd(Password.encrypt(newPwd));
        return ResponseEntity.ok(studentRepository.save(foundStudent));
    }

    // HELPER methods
    public boolean haveDuplicate(String stuEmail) {
        List<Student> students = studentRepository.findAll();
        for (Student stu : students) {
            if (stu.getEmail().equals(stuEmail.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    private boolean haveDuplicateContact(String stuContact) {
        List<Student> students = studentRepository.findAll();
        for (Student stu : students) {
            if (stu.getContact().equals(stuContact)) {
                return true;
            }
        }
        return false;
    }


    private void sendRegistrationSuccessEmailAsync(String stuEmail, String stuName, String stuContact, String stuGender) {
        Thread newThread = new Thread(() -> {
            ConfirmationSender.sendRegistrationSuccessEmail(stuEmail, stuName, stuContact, stuGender);
        });
        newThread.start();
    }
}