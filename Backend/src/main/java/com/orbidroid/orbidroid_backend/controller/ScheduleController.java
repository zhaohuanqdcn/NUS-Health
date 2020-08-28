package com.orbidroid.orbidroid_backend.controller;

import com.orbidroid.orbidroid_backend.email.ConfirmationSender;
import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Schedule;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;
import com.orbidroid.orbidroid_backend.helper.time.Adder;
import com.orbidroid.orbidroid_backend.helper.time.Comparator;
import com.orbidroid.orbidroid_backend.helper.time.Parser;
import com.orbidroid.orbidroid_backend.helper.time.Transformer;
import com.orbidroid.orbidroid_backend.repository.DoctorRepository;
import com.orbidroid.orbidroid_backend.repository.ScheduleRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
@Api(tags = "Doctor schedule related", description = "Manage doctors' work schedules, including searching, adding and deleting schedules.")
public class ScheduleController extends EntityController {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    DoctorRepository doctorRepository;


    // SEARCH METHODS

    // search all
    @ApiOperation("Search all schedules.")
    @GetMapping(value = "")
    public ResponseEntity index() {
        List<Schedule> schedules = scheduleRepository.findAll();
        if (schedules.isEmpty()) {
            return ResponseEntity.status(404).body("No schedule found.");
        }
        return ResponseEntity.ok(schedules);
    }

    // search by schedule id
    @ApiOperation("Search a specific schedule by the schedule number.")
    @GetMapping(value = "/get")
    public ResponseEntity getSchedule(
            @RequestParam(value = "ScheduleNum") Integer scheduleNum
    ) {
        Optional<Schedule> foundSchedule = scheduleRepository.getById(scheduleNum);

        if(foundSchedule.isPresent()){
            return ResponseEntity.ok(foundSchedule.get());
        }else {
            return ResponseEntity.status(404).body("No schedule with specified schedule number " + scheduleNum +" found.");
        }
    }

    // search by doc id
    // return a list of schedules
    @ApiOperation("Search schedules specified by the schedule doctor number.")
    @GetMapping(value = "/get/doc")
    public ResponseEntity getScheduleByDocNum(
            @RequestParam(value = "ScheduleDocNum") Integer scheduledocNum
    )
    {
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(scheduledocNum);

        if(!foundSchedules.isEmpty()){
            return ResponseEntity.ok(foundSchedules);
        }else {
            return ResponseEntity.status(404).body("No schedule with specified doctor number " + scheduledocNum + " found.");
        }
    }

    // search by doc email (unique key for doctor)
    // return a list of schedules
    @ApiOperation("Search schedules specified by the schedule doctor email.")
    @GetMapping(value = "/get/doc/email")
    public ResponseEntity getScheduleByDocEmail(
            @RequestParam(value = "ScheduleDocEmail") String scheduleDocEmail
    ) {
        Optional<Doctor> doc = doctorRepository.getByEmail(scheduleDocEmail.toLowerCase());
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + scheduleDocEmail.toLowerCase() + " found.");
        }
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(doc.get().getNum());
        if(!foundSchedules.isEmpty()){
            return ResponseEntity.ok(foundSchedules);
        }else {
            return ResponseEntity.status(404).body("No schedule with specified doctor email " + scheduleDocEmail.toLowerCase() + " found.");
        }
    }

    @ApiOperation("Search schedules specified by the schedule doctor contact number.")
    @GetMapping(value = "/get/doc/contact")
    public ResponseEntity getScheduleByDocContact(
            @RequestParam(value = "ScheduleDocContact") String scheduleDocContact
    ) {
        Optional<Doctor> doc = doctorRepository.getByContact(scheduleDocContact);
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor contact number " + scheduleDocContact + " found.");
        }
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(doc.get().getNum());
        if(!foundSchedules.isEmpty()){
            return ResponseEntity.ok(foundSchedules);
        } else {
            return ResponseEntity.status(404).body("No schedule with specified doctor contact number " + scheduleDocContact + " found.");
        }
    }

    @ApiOperation("Search schedules specified by the schedule doctor name. Note that multiple doctors' schedule might occur.")
    @GetMapping(value = "/get/doc/name")
    public ResponseEntity getScheduleByDocName(
            @RequestParam(value = "ScheduleDocName") String scheduleDocName
    ) {
        List<Doctor> doctors = doctorRepository.getByName(scheduleDocName);
        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor name " + scheduleDocName + " found.");
        }
        List<Schedule> foundSchedules = new ArrayList<>();
        for (Doctor doc : doctors) {
            foundSchedules.addAll(scheduleRepository.getByDocNum(doc.getNum()));
        }
        if (!foundSchedules.isEmpty()) {
            return ResponseEntity.ok(foundSchedules);
        } else {
            return ResponseEntity.status(404).body("No schedule with specified doctor name " + scheduleDocName + " found.");
        }
    }

    // search by a specific date
    // return a list of schedules
    @ApiOperation("Search schedules on a specific date.")
    @GetMapping(value = "/get/date")
    public ResponseEntity getScheduleByDate(
            @RequestParam(value = "ScheduleDate") String date
    ) {
        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx.");
        }

        List<Schedule> all = scheduleRepository.findAll();
        List<Schedule> result = new ArrayList<>();

        for (Schedule s : all) {
            if (Comparator.isDateTimeDateSameWithDate(s.getWorkStart(), date)) {
                result.add(s);
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("No schedule with specified date " + date + " found.");
        } else {
            return ResponseEntity.ok().body(result);
        }
    }

    // search by a specific date and boolean value representing morning start or afternoon start
    @ApiOperation("Search schedules on a specific date with starting from morning (true) / afternoon (false) constrains.")
    @GetMapping(value = "/get/date/specify")
    public ResponseEntity getScheduleByDateSpecify(
            @RequestParam(value = "ScheduleDate") String date,
            @RequestParam(value = "IsStartFromMorning") boolean isStartFromMorning
    ) {
        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx.");
        }

        List<Schedule> all = scheduleRepository.findAll();
        List<Schedule> result = new ArrayList<>();

        for (Schedule s : all) {
            if (Comparator.isDateTimeDateSameWithDate(s.getWorkStart(), date)) {
                if (isStartFromMorning) {
                    // add morning to morning
                    if (Comparator.isMorning(s.getWorkStart())) {
                        result.add(s);
                    }
                } else {
                    // add afternoon to afternoon
                    if (Comparator.isAfternoon(s.getWorkStart())) {
                        result.add(s);
                    }
                }
            }
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("No schedule starting from " + date + (isStartFromMorning? " morning" : " afternoon") + " found.");
        } else {
            return ResponseEntity.ok().body(result);
        }
    }



    // ADD METHODS

    // add a new schedule with proper checking
    // token is the token for admin users
    @ApiOperation("Add a new schedule.")
    @PostMapping(value = "/add")
    public ResponseEntity addToSchedule(
            @RequestParam(value = "ScheduleDocNum") Integer scheduleDocNum,
            @RequestParam(value = "ScheduleWorkStart") String scheduleWorkStart,
            @RequestParam(value = "ScheduleWorkEnd") String scheduleWorkEnd,
            @RequestParam(value = "Token") String token
    ) {
        // check token
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        // ensure that specified doctor exists
        if (!docExist(scheduleDocNum)) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + scheduleDocNum + " found.");
        }

        // ensure datetime format is correct
        if (!Parser.formatCheck(scheduleWorkStart) || !Parser.formatCheck(scheduleWorkEnd)) {
            return ResponseEntity.badRequest().body("Datetime format incorrect. Should be xxxx-xx-xx xx:xx:xx.");
        }

        // ensure that the duration satisfy the minimum length requirement
        if (Comparator.isDateTimeEarlier(scheduleWorkEnd, Adder.add(scheduleWorkStart, Bijection.getShortestScheduleDuration()))) {
            return ResponseEntity.status(403).body("The schedule start and schedule end time are too close or converted.");
        }

        // ensure that the doctor does not have any other schedule conflict
        if (haveConflictWithOtherSchedules(scheduleDocNum, scheduleWorkStart, scheduleWorkEnd)) {
            return ResponseEntity.status(403).body("The specified range from " + scheduleWorkStart + " to " + scheduleWorkEnd + " for this doctor is otherwise scheduled.");
        }

        // send email confirmation
        sendConfirmationAsync(scheduleDocNum, scheduleWorkStart, scheduleWorkEnd);
        return ResponseEntity.status(201).body(scheduleRepository.save(new Schedule(
                scheduleDocNum, scheduleWorkStart, scheduleWorkEnd
        )));
    }

    // FIXME
    // Add a default work schedule for a specific doctor on a specific day.
    // Holiday is not considered currently.
    @ApiOperation("Add a default schedule for a specific doctor on a specific day. Holiday is currently not considered.")
    @PostMapping(value =  "/add/default/oneDoc/oneDay")
    public ResponseEntity addDefaultScheduleForOneDocAndOneDay (
            @RequestParam(value = "ScheduleDocNum") Integer scheduleDocNum,
            @RequestParam(value = "ScheduleDate") String date,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx.");
        }

        int weekday = Transformer.weekdayMapper(date);
        if (Bijection.isDefaultWeekEnd(weekday)) {
            return ResponseEntity.status(403).body("Weekend schedule (" + date + ") creation is not allowed.");
        }

        if (!docExist(scheduleDocNum)) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number " + scheduleDocNum + " found.");
        }

        if (docHasScheduleOn(scheduleDocNum, date)) {
            return ResponseEntity.status(403).body("The doctor already has schedule on the specified date" + date + ".");
        }

        Schedule sch1 = new Schedule(
                scheduleDocNum, Bijection.getDefaultWorkStartForMorning(date), Bijection.getDefaultWorkEndForMorning(date)
        );
        Schedule sch2 = new Schedule(
                scheduleDocNum, Bijection.getDefaultWorkStartForAfternoon(date), Bijection.getDefaultWorkEndForAfternoon(date, weekday)
        );
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(scheduleRepository.save(sch1));
        schedules.add(scheduleRepository.save(sch2));
        // send email confirmation
        sendConfirmationForOneDayAsync(scheduleDocNum, date);
        return ResponseEntity.status(201).body(schedules.toString());
    }


    @ApiOperation("Add a default schedule for all doctors on a specific day. Holiday is currently not considered. " +
            "If one of the doctors are otherwise occupied, that doctor will be skipped.")
    @PostMapping(value =  "/add/default/allDoc/oneDay")
    public ResponseEntity addDefaultScheduleForAllDocAndOneDay (
            @RequestParam(value = "ScheduleDate") String date,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx.");
        }

        int weekday = Transformer.weekdayMapper(date);
        if (Bijection.isDefaultWeekEnd(weekday)) {
            return ResponseEntity.status(403).body("Weekend schedule (" + date + ") creation is not allowed.");
        }

        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(404).body("No doctor found.");
        }

        List<Schedule> result = new ArrayList<>();
        for (Doctor doc : doctors) {
            Schedule sch1 = new Schedule(
                    doc.getNum(), Bijection.getDefaultWorkStartForMorning(date), Bijection.getDefaultWorkEndForMorning(date)
            );
            Schedule sch2 = new Schedule(
                    doc.getNum(), Bijection.getDefaultWorkStartForAfternoon(date), Bijection.getDefaultWorkEndForAfternoon(date, weekday)
            );
            result.add(scheduleRepository.save(sch1));
            result.add(scheduleRepository.save(sch2));

            // send email confirmation
            sendConfirmationForOneDayAsync(doc.getNum(), date);
        }
        return ResponseEntity.status(201).body(result.toString());
    }

    // DELETE methods

    // Testing purposes only
    /*
    @Deprecated
    @ApiOperation("Backend testing purposes. Don't use it.")
    @Transactional
    @DeleteMapping(value = "/all")
    public ResponseEntity removeAll(
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        scheduleRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
    */

    // Delete a schedule by the specified schedule number.
    @ApiOperation("Delete a schedule by the specified schedule number.")
    @Transactional
    @DeleteMapping(value = "/num")
    public ResponseEntity removeByScheduleNum(
            @RequestParam(value = "ScheduleNum") Integer scheduleNum,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }
        scheduleRepository.deleteById(scheduleNum);
        return ResponseEntity.ok().body("Deleted successfully.");
    }

    // Delete all schedules by specified doctor number.
    /*
    @ApiOperation("Delete schedules by DocNum.")
    @Transactional
    @DeleteMapping(value = "/doc")
    public ResponseEntity removeByDocNum(
            @RequestParam(value = "DocNum") Integer docNum,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        scheduleRepository.deleteByDocNum(docNum);
        return ResponseEntity.ok().body("Deleted successfully.");
    }
    */
    /*
    // Delete all schedules by specified doctor number.
    @ApiOperation("Delete schedules by DocEmail.")
    @Transactional
    @DeleteMapping(value = "/doc/email")
    public ResponseEntity removeByDocEmail(
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        Optional<Doctor> doc = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor number found.");
        }
        scheduleRepository.deleteByDocNum(doc.get().getNum());
        return ResponseEntity.ok().body("Deleted successfully.");
    }
    */

    // Delete all schedules by specified doctor number and date.
    @ApiOperation("Delete schedules specified by the doctor email and date.")
    @Transactional
    @DeleteMapping(value = "/doc/email/date")
    public ResponseEntity removeByDocEmailWithDate (
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Date") String date,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!Parser.formatCheckForDate(date)) {
            return ResponseEntity.badRequest().body("Date format incorrect. Should be xxxx-xx-xx.");
        }

        Optional<Doctor> doc = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }
        scheduleRepository.deleteScheduleWithDocNumAndRange(doc.get().getNum(), Transformer.turnIntoDayEarliest(date), Transformer.turnIntoDayLatest(date));
        return ResponseEntity.ok().body("Deleted successfully.");
    }

    // Delete all schedules by specified doctor number and datetime range.
    @ApiOperation("Delete schedules specified by the doctor email and datetime range.")
    @Transactional
    @DeleteMapping(value = "/doc/email/date/range")
    public ResponseEntity removeByDocEmailWithRange (
            @RequestParam(value = "DocEmail") String docEmail,
            @RequestParam(value = "Start") String start,
            @RequestParam(value = "End") String end,
            @RequestParam(value = "Token") String token
    ) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Admin token incorrect.");
        }

        if (!(Parser.formatCheck(start) && Parser.formatCheck(end))) {
            return ResponseEntity.badRequest().body("Datetime format incorrect. Should be xxxx-xx-xx xx:xx:xx.");
        }

        Optional<Doctor> doc = doctorRepository.getByEmail(docEmail.toLowerCase());
        if (!doc.isPresent()) {
            return ResponseEntity.status(404).body("No doctor with specified doctor email " + docEmail.toLowerCase() + " found.");
        }

        scheduleRepository.deleteScheduleWithDocNumAndRange(doc.get().getNum(), start, end);
        return ResponseEntity.ok().body("Deleted successfully.");
    }

    // CHANGE methods
    // 'Change methods' in schedule controller are only for method consistency.

    // General one. Forbidden.
    /*
    @ApiOperation(value = "It is a safe bet not to use change functions.")
    @PutMapping(value = "")
    public ResponseEntity update() {
        return ResponseEntity.status(403).body("For data consistency reasons, schedule change function is not " +
                "available. If you need any changes, please delete original schedule and create a new one as you wish.");
    }

    // For testing purposes only
    @Deprecated
    @ApiOperation("TESTING purposes. Update a schedule's start time.")
    @PutMapping(value = "/start")
    public ResponseEntity updateStart(
                                    @RequestParam(value = "ScheduleNum") Integer scheduleNum,
                                    @RequestParam(value = "ScheduleWorkStart") String workStart,
                                    @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }

        if (!Parser.formatCheck(workStart)) {
            return ResponseEntity.badRequest().body("Datetime format should be xxxx-xx-xx aa:bb:cc");
        }

        Optional<Schedule> optionalSchedule = scheduleRepository.getById(scheduleNum);
        if(!optionalSchedule.isPresent()){
            return ResponseEntity.badRequest().body("No schedule with specified schedule number " + scheduleNum + " found.");
        }
        Schedule foundSchedule = optionalSchedule.get();
        foundSchedule.setWorkStart(workStart);
        return ResponseEntity.ok(scheduleRepository.save(foundSchedule));
    }

    // For testing purposes only
    @Deprecated
    @ApiOperation("TESTING purposes. Update a schedule's end time.")
    @PutMapping(value = "/end")
    public ResponseEntity updateEnd(
            @RequestParam(value = "ScheduleNum") Integer scheduleNum,
            @RequestParam(value = "ScheduleWorkEnd") String workEnd,
            @RequestParam(value = "Token") String token) {
        if (!Bijection.adminTokenCorrect(token)) {
            return ResponseEntity.status(403).body("Token incorrect.");
        }
        if (!Parser.formatCheck(workEnd)) {
            return ResponseEntity.badRequest().body("Datetime format should be xxxx-xx-xx aa:bb:cc");
        }
        Optional<Schedule> optionalSchedule = scheduleRepository.getById(scheduleNum);
        if(!optionalSchedule.isPresent()){
            return ResponseEntity.badRequest().body("No schedule with specified schedule number " + scheduleNum + " found.");
        }
        Schedule foundSchedule = optionalSchedule.get();
        foundSchedule.setWorkEnd(workEnd);
        return ResponseEntity.ok(scheduleRepository.save(foundSchedule));
    }
    */

    // HELPER methods

    private boolean docExist(Integer docNum) {
        Optional<Doctor> doc = doctorRepository.getById(docNum);
        if (doc.isPresent()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean haveConflictWithOtherSchedules(Integer scheduleDocNum, String scheduleWorkStart, String scheduleWorkEnd) {
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(scheduleDocNum);
        boolean result = false;
        for (Schedule s : foundSchedules) {
            if (Comparator.hasConflict(s.getWorkStart(), s.getWorkEnd(), scheduleWorkStart, scheduleWorkEnd)) {
                result = true;
            }
        }
        return result;
    }


    private boolean docHasScheduleOn(Integer scheduleDocNum, String date) {
        List<Schedule> foundSchedules = scheduleRepository.getByDocNum(scheduleDocNum);
        boolean result = false;
        for (Schedule s : foundSchedules) {
            if (Comparator.hasConflict(s.getWorkStart(), s.getWorkEnd(), Transformer.turnIntoDayEarliest(date), Transformer.turnIntoDayLatest(date))) {
                result = true;
            }
        }
        return result;
    }

    private void sendConfirmationAsync(Integer docNum, String datetimeStart, String datetimeEnd) {
        Optional<Doctor> doc = doctorRepository.getById(docNum);
        if (!doc.isPresent()) {
        }
        Thread newThread = new Thread(
                () -> {
                    ConfirmationSender.sendScheduleConfirmation(doc.get().getEmail(), doc.get(), datetimeStart, datetimeEnd);
                });
        newThread.start();
    }

    private void sendConfirmationForOneDayAsync(Integer docNum, String date) {
        Optional<Doctor> doc = doctorRepository.getById(docNum);
        if (!doc.isPresent()) {
        }
        Thread newThread = new Thread(
                () -> {
                    ConfirmationSender.sendScheduleConfirmationForOneDay(doc.get().getEmail(), doc.get(), date);
                });
        newThread.start();
    }
}
