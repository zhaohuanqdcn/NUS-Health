package com.orbidroid.orbidroid_backend.email;

import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Student;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;

public class Template {
    public static String getVeriHtml(String veriCode) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Verification code for NUS Health</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Verification code for NUS Health</h3>\n" +
                "<p>Hi! Thanks for registering for NUS Health! " +
                "Your verification code for registration is: </p>\n" +
                "<h2 style=\"font-family:Courier new;color:#3363F5;font-size:200%\">\n" +
                veriCode + "\n" +
                "</h2>\n" +
                "<p>Please complete registration in " + Bijection.getVeriCodeTimeOutInMinutes() + " minutes \n" +
                "after you saw this message. The code will expire afterwards.</p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this code to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from NUS Health development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getBookingConfirmationHtmlForStu(Doctor doc, Student stu, String time, int bookingType) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Booking Confirmation from NUS Health</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Booking Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (stu.getGender().equals("Male")? "Mr. ": "Ms. ") + stu.getName() + ", your booking is confirmed with the following details:</p>\n" +
                "<p style=\"font-weight: bold\">Student:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Name: " + stu.getName() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Gender: " + stu.getGender() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Email: " + stu.getEmail() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Contact: " + stu.getContact() + "</p>\n" +
                "<p style=\"font-weight: bold\">Doctor:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Name: " + doc.getName() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Gender: " + doc.getGender() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Position: " + doc.getPos() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Email: " + doc.getEmail() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Contact: " + doc.getContact() + "</p>\n" +
                "<p style=\"font-weight: bold\">Time:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + time + "</p>\n" +
                "<p style=\"font-weight: bold\">Booking type:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + Bijection.getBookingType(bookingType) + "</p>\n" +
                "<p>Please go to the site on time.<p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getBookingConfirmationHtmlForDoc(Doctor doc, Student stu, String time, int bookingType) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Booking Confirmation from NUS Health</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Booking Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (doc.getGender().equals("Male")? "Mr. ": "Ms. ") + doc.getName() + ", you have a new booking confirmed with the following details:</p>\n" +
                "<p style=\"font-weight: bold\">Student:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Name: " + stu.getName() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Gender: " + stu.getGender() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Email: " + stu.getEmail() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Contact: " + stu.getContact() + "</p>\n" +
                "<p style=\"font-weight: bold\">Doctor:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Name: " + doc.getName() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Gender: " + doc.getGender() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Position: " + doc.getPos() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Email: " + doc.getEmail() + "</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;Contact: " + doc.getContact() + "</p>\n" +
                "<p style=\"font-weight: bold\">Time:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + time + "</p>\n" +
                "<p style=\"font-weight: bold\">Booking type:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + Bijection.getBookingType(bookingType) + "</p>\n" +
                "<p>Please go to the site on time.<p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getScheduleConfirmationHtml(Doctor doc, String timeStart, String timeEnd) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Doctor Work Schedule Confirmation from NUS Health</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Doctor Work Schedule " +
                "Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (doc.getGender().equals("Male") ?"Mr. ":"Ms. ") + doc.getName() + ", a new work schedule is added for you as " + doc.getPos() + " by admin.</p>" +
                "<p style=\"font-weight: bold\">From:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + timeStart + "</p>\n" +
                "<p style=\"font-weight: bold\">To:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + timeEnd + "</p>\n" +
                "<p>Please go to the site on time.<p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getScheduleConfirmationHtmlForOneDay(Doctor doc, String date) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Doctor Work Schedule Confirmation from NUS Health</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Doctor Work Schedule " +
                "Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (doc.getGender().equals("Male") ? "Mr. ": "Ms. ") + doc.getName() + ", a new work schedule is added for you as " + doc.getPos() + " by admin.</p>" +
                "<p style=\"font-weight: bold\">Date:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + date + "</p>\n" +
                "<p style=\"font-weight: bold\">Detail:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + "A regular work schedule for the specified date. </p>\n" +
                "<p>Please go to the site on time.<p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getRegistrationSuccessHtmlForStu(String stuEmail, String stuName, String stuContact, String stuGender) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Registration is successful!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Registration Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (stuGender.equals("Male") ? "Mr. ": "Ms. ") + stuName + ", your registration for NUS Health is successful. Below are the registration details.</p>" +
                "<p style=\"font-weight: bold\">Name:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + stuName + "</p>\n" +
                "<p style=\"font-weight: bold\">Gender:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + stuGender + "</p>\n" +
                "<p style=\"font-weight: bold\">Email:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + stuEmail + "</p>\n" +
                "<p style=\"font-weight: bold\">Contact:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + stuContact + "</p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getDoctorAppointmentHtml(String docName, String docEmail, String docContact, String docPos, String docGender) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "body {background-color: #EFFBFB}\n" +
                "p {" +
                "color:#0B0B3B," +
                "font-family: calibri" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>Doctor appointment confirmation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h3 style=\"font-family: calibri; font-size:150%; color:#0B0B3B\">Doctor Appointment Confirmation from NUS Health</h3>\n" +
                "<p>Hi " + (docGender.equals("Male") ? "Mr. ": "Ms. ") + docName + ", you are appointed as a doctor. Below are the appointment details.</p>" +
                "<p style=\"font-weight: bold\">Name:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + docName + "</p>\n" +
                "<p style=\"font-weight: bold\">Gender:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + docGender + "</p>\n" +
                "<p style=\"font-weight: bold\">Position:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + docPos + "</p>\n" +
                "<p style=\"font-weight: bold\">Email:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + docEmail + "</p>\n" +
                "<p style=\"font-weight: bold\">Contact:</p>\n" +
                "<p style=\"font-weight: bold\">&nbsp;&nbsp;&nbsp;&nbsp;" + docContact + "</p>\n" +
                "<p>For your information security, please do not disclose \n" +
                "this information to anyone else.</p>\n" +
                "<p>Please be assured that all your personal information is \n" +
                "held private.</p>\n" +
                "<P>If you encounter any issues, please contact admin staff or developers for help.\n" +
                "<p>Regards from Orbidroid development team.</p>\n" +
                "</body>\n" +
                "</html>";
    }
}
