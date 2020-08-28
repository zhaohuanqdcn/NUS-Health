package com.orbidroid.orbidroid_backend.email;

import com.orbidroid.orbidroid_backend.entity.Doctor;
import com.orbidroid.orbidroid_backend.entity.Student;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ConfirmationSender {
    public static boolean sendBookingConfirmationForStu(String receiver, Doctor doc, Student stu, String time, int bookingType) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Booking Confirmation from NUS Health");
            // set email content
            String content = Template.getBookingConfirmationHtmlForStu(doc, stu, time, bookingType);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(receiver)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendBookingConfirmationForDoc(String receiver, Doctor doc, Student stu, String time, int bookingType) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Booking Confirmation from NUS Health");
            // set email content
            String content = Template.getBookingConfirmationHtmlForDoc(doc, stu, time, bookingType);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(receiver)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendScheduleConfirmation(String receiver, Doctor doc, String timeStart, String timeEnd) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Doctor Work Schedule Confirmation from NUS Health");
            // set email content
            String content = Template.getScheduleConfirmationHtml(doc, timeStart, timeEnd);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(receiver)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendScheduleConfirmationForOneDay(String receiver, Doctor doc, String date) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Doctor Work Schedule Confirmation from NUS Health");
            // set email content
            String content = Template.getScheduleConfirmationHtmlForOneDay(doc, date);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(receiver)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendRegistrationSuccessEmail(String stuEmail, String stuName, String stuContact, String stuGender) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Registration is successful!");
            // set email content
            String content = Template.getRegistrationSuccessHtmlForStu(stuEmail, stuName, stuContact, stuGender);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(stuEmail)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendDoctorAppointmentConfirmationEmail(String docName, String docEmail, String docContact, String docPos, String docGender) {
        try {
            Properties props = new Properties();
            // debug testing
            props.setProperty("mail.debug", "false");
            // identity authentication
            props.setProperty("mail.smtp.auth", "true");
            // host name
            props.setProperty("mail.host", Bijection.getSmtpHostAddress());
            // protocol name
            props.setProperty("mail.transport.protocol", "smtp");
            // environment setup
            Session session = Session.getInstance(props);
            // create mailing object
            Message msg = new MimeMessage(session);
            // set email subject
            msg.setSubject("Doctor Appointment from NUS Health");
            // set email content
            String content = Template.getDoctorAppointmentHtml(docName, docEmail, docContact, docPos, docGender);
            msg.setContent(content,"text/html;charset=utf-8");
            // set sender address
            msg.setFrom(new InternetAddress(Bijection.getEmailSenderAccount()));
            Transport transport = session.getTransport();
            // connect to email server
            transport.connect(Bijection.getEmailSenderAccount(), Bijection.getEmailSenderPwd());
            // send email
            transport.sendMessage(msg, new Address[]{new InternetAddress(docEmail)});
            // close connection
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
