package com.orbidroid.orbidroid_backend.controller;

import com.orbidroid.orbidroid_backend.email.ConfirmationSender;
import com.orbidroid.orbidroid_backend.entity.Booking;
import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Schedule;
import com.orbidroid.orbidroid_backend.entity.Student;
import com.orbidroid.orbidroid_backend.helper.time.Adder;
import com.orbidroid.orbidroid_backend.helper.time.Comparator;
import com.orbidroid.orbidroid_backend.helper.time.Parser;
import com.orbidroid.orbidroid_backend.helper.time.Transformer;
import com.orbidroid.orbidroid_backend.repository.BookingRepository;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;
import com.orbidroid.orbidroid_backend.repository.DoctorRepository;
import com.orbidroid.orbidroid_backend.repository.ScheduleRepository;
import com.orbidroid.orbidroid_backend.repository.StudentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
@Api(tags = "Booking related", description = "Manege bookings, including searching, adding and deleting bookings.")
public class BookingController extends EntityController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    StudentRepository studentRepository;

    // SEARCH methods

    @ApiOperation("Search all bookings.")
    @GetMapping(value = "")
    public ResponseEntity index(
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) {
            return ResponseEntity.status(404).body("No booking found.");
        }
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @ApiOperation("Search a specific booking by booking number.")
    @GetMapping(value = "/get")
    public ResponseEntity getBooking(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Booking> foundBooking = bookingRepository.getById(bookingNum);
        if(foundBooking.isPresent()){
            return ResponseEntity.ok(foundBooking.get());
        }else {
            return ResponseEntity.status(404).body("No booking with specified booking number " + bookingNum + " found.");
        }
    }

    // FIXME
    @ApiOperation("Search all bookings related to the student specified by the student number.")
    @GetMapping(value = "/getHistory/stu")
    public ResponseEntity getHistoryByStuNum(
            @RequestParam(value = "StuNum") Integer stuNum
    ) {
        List<Booking> foundBookings = bookingRepository.getByStuNum(stuNum);
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified student number " + stuNum + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the student specified by the student email.")
    @GetMapping(value = "/getHistory/stu/email")
    public ResponseEntity getHistoryByStuEmail(
            @RequestParam(value = "StuEmail") String stuEmail,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> stu = studentRepository.getByEmail(stuEmail.toLowerCase());
        if (!stu.isPresent()) {
            return ResponseEntity.status(404).body("No student with specified student email " + stuEmail.toLowerCase() + " found.");
        }
        Integer stuNum = stu.get().getNum();
        List<Booking> foundBookings = bookingRepository.getByStuNum(stuNum);
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified student email " + stuEmail + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the student specified by the student contact number.")
    @GetMapping(value = "/getHistory/stu/contact")
    public ResponseEntity getHistoryByStuContact(
            @RequestParam(value = "StuContact") String stuContact,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Student> stu = studentRepository.getByContact(stuContact);
        if (!stu.isPresent()) {
            return ResponseEntity.status(404).body("No student with specified student contact " + stuContact + " found.");
        }
        Integer stuNum = stu.get().getNum();
        List<Booking> foundBookings = bookingRepository.getByStuNum(stuNum);
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified student contact " + stuContact + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the student specified by the student name. Note that multiple students' booking might occur.")
    @GetMapping(value = "/getHistory/stu/name")
    public ResponseEntity getHistoryByStuName(
            @RequestParam(value = "StuName") String stuName,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Student> students = studentRepository.getByName(stuName);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body("No student with specified student name " + stuName + " found.");
        }
        List<Booking> foundBookings = new ArrayList<>();
        for (Student s : students) {
            foundBookings.addAll(bookingRepository.getByStuNum(s.getNum()));
        }
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified student name " + stuName + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the doctor specified by the doctor number.")
    @GetMapping(value = "/getHistory/doc")
    public ResponseEntity getHistoryByDocNum(
            @RequestParam(value = "DocNum") Integer docNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Booking> foundBookings = bookingRepository.getByDocNum(docNum);
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified doctor number " + docNum + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the doctor specified by the doctor email.")
    @GetMapping(value = "/getHistory/doc/email")
    public ResponseEntity getHistoryByDocEmail(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> doc = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
        List<Booking> foundBookings = bookingRepository.getByDocNum(doc.get().getNum());
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the doctor specified by the doctor contact.")
    @GetMapping(value = "/getHistory/doc/contact")
    public ResponseEntity getHistoryByDocContact(
            @RequestParam(value = "DocContact") String docContact,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        Optional<Doctor> doc = doctorRepository.getByContact(docContact);
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor found with specified doctor contact number " + docContact + " found.");
        }
        List<Booking> foundBookings = bookingRepository.getByDocNum(doc.get().getNum());
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified doctor contact number " + docContact + " found.");
        }
    }

    @ApiOperation("Search all bookings related to the doctor specified by the doctor name.")
    @GetMapping(value = "/getHistory/doc/name")
    public ResponseEntity getHistoryByDocName(
            @RequestParam(value = "DocName") String docName,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        List<Doctor> doctors = doctorRepository.getByName(docName);
        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body("No doctor found with specified doctor name " + docName + " found.");
        }
        List<Booking> foundBookings = new ArrayList<>();
        for (Doctor d : doctors) {
            foundBookings.addAll(bookingRepository.getByDocNum(d.getNum()));
        }
        if (!foundBookings.isEmpty()) {
            return ResponseEntity.ok(foundBookings);
        } else {
            return ResponseEntity.badRequest().body("No history booking with specified doctor name " + docName + " found.");
        }
    }

    @ApiOperation("Search all bookings on the day specified.")
    @GetMapping(value = "/getHistory/date")
    public ResponseEntity getHistoryByDate(
            @RequestParam(value = "Date") String date,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect, should be xxxx-xx-xx");
        }
        List<Booking> foundBookings = bookingRepository.getBookingsInBetween(Transformer.turnIntoDayEarliest(date), Transformer.turnIntoDayLatest(date));
        if (foundBookings.isEmpty()) {
            return ResponseEntity.status(404).body("No history booking with specified date " + date + " found.");
        }
        return ResponseEntity.ok(foundBookings);
    }

    @ApiOperation("Search all bookings in the specified range.")
    @GetMapping(value = "/getHistory/range")
    public ResponseEntity getHistoryByRange(
            @RequestParam(value = "DatetimeStart") String rangeStart,
            @RequestParam(value = "DatetimeEnd") String rangeEnd,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!Parser.formatCheck(rangeStart)) {
            return ResponseEntity.badRequest().body("Range start datetime format incorrect, should be xxxx-xx-xx xx:xx:xx");
        }
        if (!Parser.formatCheck(rangeEnd)) {
            return ResponseEntity.badRequest().body("Range end datetime format incorrect, should be xxxx-xx-xx xx:xx:xx");
        }
        List<Booking> foundBookings = bookingRepository.getBookingsInBetween(rangeStart, rangeEnd);
        if (foundBookings.isEmpty()) {
            return ResponseEntity.status(404).body("No history booking with specified range from " + rangeStart + " to " + rangeEnd + " found.");
        }
        return ResponseEntity.ok(foundBookings);
    }

    // FIXME
    @ApiOperation("Search all available slots with doctors with specified BookingType and Date.")
    @GetMapping(value = "/getAvl")
    public ResponseEntity getSlotsWithDocs(
            @RequestParam(value = "BookingType") int bookingType,
            @RequestParam(value = "Date") String date
    ) {
        if (!Bijection.isBookingTypeCorrect(bookingType)) {
            return ResponseEntity.badRequest().body("Booking type format incorrect.");
        }
        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx");
        }
        List<String> result = new ArrayList<>();

        String dateTimeStart = date + " " + Bijection.getGeneralWorkStartTime();
        String dateTimeEnd = date + " " + Bijection.getGeneralWorkEndTime();

        int duration = Bijection.getDuration(bookingType);
        int shortestDuration = Bijection.getShortestDurationForBooking();

        // FOR ALL SLOTS
        while (Comparator.isDateTimeEarlier(dateTimeStart, dateTimeEnd)) {
            String durationStart = dateTimeStart;
            String durationEnd = Adder.add(durationStart, duration);

            // should search schedule that contains the period
            List<Schedule> allSchedules = scheduleRepository.getScheduleContains(durationStart, durationEnd);

            boolean hasAvailableDoc = false;
            Integer docIndicator = 1;
            JSONObject js = new JSONObject();
            List<JSONObject> docList = new ArrayList<>();
            //JSONObject docJs = new JSONObject();

            // try to put as many doctors as possible
            for (int i = 0; i < allSchedules.size(); i++) {
                Schedule sch = allSchedules.get(i);
                Integer docNum = sch.getDocNum();

                // if the doctor represented by this num is not booked
                if (notBooked(docNum, durationStart, durationEnd)) {
                    Optional<Doctor> doc = doctorRepository.getById(Integer.valueOf(docNum));
                    if (doc.isPresent()) {
                        hasAvailableDoc = true;
                        JSONObject docSubJs = new JSONObject();
                        docSubJs.put("num", doc.get().getNum());
                        docSubJs.put("name", doc.get().getName());
                        docSubJs.put("gender", doc.get().getGender());
                        docSubJs.put("pos", doc.get().getPos());
                        docSubJs.put("contact", doc.get().getContact());
                        docSubJs.put("email", doc.get().getEmail());
                        docList.add(docSubJs);
                    }
                }
            }

            if (hasAvailableDoc) {
                js.put("Start", durationStart);
                js.put("BookingType", bookingType);
                js.put("Doctors", docList);
            }

            if (!js.isEmpty()) {
                result.add(js.toString());
            }
            dateTimeStart = Adder.add(dateTimeStart, shortestDuration);
        }
        if (result.isEmpty()) {
            return ResponseEntity.status(403).body(
                    String.format("No available slots with specified date %s and booking type %d found.",date, bookingType));
        } else {
            return ResponseEntity.ok(result.toString());
        }
    }

    // ADD methods

    // FIXME
    // In case of high concurrency，availability checking is done here again, though checking shall be done before.
    @ApiOperation("Add a new Booking. Adding will not be successful if the slot is not available.")
    @PostMapping(value = "/add")
    public ResponseEntity addToBooking(
            @RequestParam(value = "BookingTime") String bookingTime,
            @RequestParam(value = "BookingType") int bookingType,
            @RequestParam(value = "BookingDocNum") Integer bookingDocNum,
            @RequestParam(value = "BookingStuNum") Integer bookingStuNum
    ) {
        if (!Parser.formatCheck(bookingTime)) {
            return ResponseEntity.badRequest().body("Datetime format incorrect. Should be xxxx-xx-xx xx:xx:xx");
        }
        if (!Bijection.isBookingTypeCorrect(bookingType)) {
            return ResponseEntity.badRequest().body("Booking type incorrect.");
        }
        if (!docExist(bookingDocNum)) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + bookingDocNum + " found");
        }
        if (!stuExist(bookingStuNum)) {
            return ResponseEntity.status(404).body("No student with specified student number " + bookingStuNum + " found");
        }
        if (!haveSchedule(bookingDocNum, bookingTime, bookingType)) {
            return ResponseEntity.status(403).body("Slot reservation failed because no schedule exists for specified doctor number " + bookingDocNum + ".");
        }
        if (!notBooked(bookingDocNum, bookingTime, Adder.add(bookingTime, Bijection.getDuration(bookingType)))) {
            return ResponseEntity.status(403).body("Slot reservation failed because the slot is booked by someone else.");
        }
        if (studentHaveOtherBookingsAtThisTime(bookingStuNum, bookingTime, Adder.add(bookingTime, Bijection.getDuration(bookingType)))) {
            return ResponseEntity.status(403).body("Slot reservation failed because the student has other bookings at the specified time.");
        }
        sendConfirmationAsync(bookingTime, bookingDocNum, bookingStuNum, bookingType);
        return ResponseEntity.status(201).body(bookingRepository.save(new Booking(
                bookingTime, Bijection.getDuration(bookingType), bookingType, bookingDocNum, bookingStuNum
        )));
    }

    // DELETE methods
    // Do note that you are only expected to delete one booking with specified booking number at one time, and it is only done
    // in a situation that it must be done. All other deletion methods are for testing and not allowed to use.

    /*
    @Deprecated
    @ApiOperation("Backend testing purposes. Don't use it.")
    @Transactional
    @DeleteMapping(value = "/all")
    public ResponseEntity removeAll(
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        bookingRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
    */

    @ApiOperation("Delete all bookings of a student specified by the student number.")
    @Transactional
    @DeleteMapping(value = "/stu")
    public ResponseEntity removeByStuNum(
            @RequestParam(value = "BookingStuNum") Integer stuNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!stuExist(stuNum)) {
            return ResponseEntity.status(404).body("No student with specified student number " + stuNum + " found.");
        }
        bookingRepository.deleteByStuNum(stuNum);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Delete a booking specified by the booking number.")
    @Transactional
    @DeleteMapping(value = "/num")
    public ResponseEntity removeByBookingNum(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "Token") String adminToken
    ) {
        if (!Bijection.adminTokenCorrect(adminToken)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        if (!bookingExist(bookingNum)) {
            return ResponseEntity.status(404).body("No booking with specified booking number " + bookingNum + " found.");
        }
        bookingRepository.removeById(bookingNum);
        return ResponseEntity.ok().build();
    }

    // CHANGE methods

    // NOTE that currently, there is no need and you are not expected to 'change' a booking, since re-checking for availability is tricky and buggy.
    // You are expected to delete the booking and add a new one.
/*
    @Deprecated
    @ApiOperation("Update a booking's StuNum.")
    @PutMapping(value = "/stu")
    public ResponseEntity updateStuNum(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "BookingStuNum") Integer stuNum,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!stuExist(stuNum)) {
            return ResponseEntity.status(404).body("No student with specified booking number " + bookingNum + " found.");
        }
        Optional<Booking> optionalBooking = bookingRepository.getById(bookingNum);
        if (!optionalBooking.isPresent()) {
            return ResponseEntity.badRequest().body("No booking with specified booking number " + bookingNum + " found.");
        } else {
            Booking foundBooking = optionalBooking.get();
            foundBooking.setStuNum(stuNum);
            return ResponseEntity.ok(bookingRepository.save(foundBooking));
        }
    }

    @Deprecated
    @ApiOperation("Update a booking's DocNum.")
    @PutMapping(value = "/doc")
    public ResponseEntity updateDocNum(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "BookingDocNum") Integer docNum,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!docExist(docNum)) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + docNum + "found.");
        }
        Optional<Booking> optionalBooking = bookingRepository.getById(bookingNum);
        if (!optionalBooking.isPresent()) {
            return ResponseEntity.badRequest().body("No booking with specified booking number " + bookingNum + " found.");
        } else {
            Booking foundBooking = optionalBooking.get();
            foundBooking.setDocNum(docNum);
            return ResponseEntity.ok(bookingRepository.save(foundBooking));
        }
    }

    @Deprecated
    @ApiOperation("Update a booking's BookingTime.")
    @PutMapping(value = "/time")
    public ResponseEntity updateTime(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "BookingTime") String newTime,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!Parser.formatCheck(newTime)) {
            return ResponseEntity.badRequest().body("Datetime format should be xxxx-xx-xx aa:bb:cc");
        }

        Optional<Booking> optionalBooking = bookingRepository.getById(bookingNum);
        if (!optionalBooking.isPresent()) {
            return ResponseEntity.badRequest().body("No booking with specified booking number " + bookingNum + " found.");
        } else {
            Booking foundBooking = optionalBooking.get();
            foundBooking.setTime(newTime);
            return ResponseEntity.ok(bookingRepository.save(foundBooking));
        }
    }

    @Deprecated
    @ApiOperation("Update a booking's BookingType.")
    @PutMapping(value = "/type")
    public ResponseEntity updateType(
            @RequestParam(value = "BookingNum") Integer bookingNum,
            @RequestParam(value = "NewType") int newType,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!Bijection.isBookingTypeCorrect(newType)) {
            return ResponseEntity.badRequest().body("Booking type format incorrect.");
        }
        Optional<Booking> optionalBooking = bookingRepository.getById(bookingNum);
        if (!optionalBooking.isPresent()) {
            return ResponseEntity.badRequest().body("No booking with specified booking number " + bookingNum + " found.");
        } else {
            Booking foundBooking = optionalBooking.get();
            foundBooking.setType(newType);
            foundBooking.setDuration(Bijection.getDuration(newType));
            return ResponseEntity.ok(bookingRepository.save(foundBooking));
        }
    }
*/
    // HELPER methods

    private boolean notBooked(Integer docNum, String durationStart, String durationEnd) {

        List<Booking> bookings = bookingRepository.getByDocNum(docNum);
        boolean result = true;
        for (int i = 0; i < bookings.size(); i++) {
            Booking bk = bookings.get(i);
            if (Comparator.hasConflict(durationStart, durationEnd, bk.getTime(), Adder.add(bk.getTime(), bk.getDuration()))) {
                result = false;
            }
        }
        return result;
    }

    private boolean docExist(Integer docNum) {
        Optional<Doctor> doc = doctorRepository.getById(docNum);
        if (doc.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    private boolean stuExist(Integer stuNum) {
        Optional<Student> stu = studentRepository.getById(stuNum);
        if (stu.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    private boolean bookingExist(Integer bookingNum) {
        Optional<Booking> booking = bookingRepository.getById(bookingNum);
        if (booking.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean haveSchedule(Integer bookingDocNum, String bookingTime, int bookingType) {
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(bookingDocNum);
        boolean result = false;
        for (Schedule s : foundSchedules) {
            if (!Comparator.isDateTimeEarlier(bookingTime, s.getWorkStart())
                && !Comparator.isDateTimeEarlier(s.getWorkEnd(), Adder.add(bookingTime, Bijection.getDuration(bookingType)))) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void sendConfirmationAsync(String bookingTime, Integer bookingDocNum, Integer bookingStuNum, int bookingType) {
        Optional<Student> stu = studentRepository.getById(bookingStuNum);
        Optional<Doctor> doc = doctorRepository.getById(bookingDocNum);
        if (!stu.isPresent() || !doc.isPresent()) {
        }
        Thread newThread = new Thread(() -> {
            ConfirmationSender.sendBookingConfirmationForStu(stu.get().getEmail(), doc.get(), stu.get(), bookingTime, bookingType);
            ConfirmationSender.sendBookingConfirmationForDoc(stu.get().getEmail(), doc.get(), stu.get(), bookingTime, bookingType);
        });
        newThread.start();
    }

    private boolean studentHaveOtherBookingsAtThisTime(Integer stuNum, String bookingStart, String bookingEnd) {
        List<Booking> bookings = bookingRepository.getByStuNum(stuNum);
        for (Booking b : bookings) {
            if (Comparator.hasConflict(bookingStart, bookingEnd, b.getTime(), Adder.add(b.getTime(), Bijection.getDuration(b.getType())))) {
                return true;
            }
        }
        return false;
    }
}
