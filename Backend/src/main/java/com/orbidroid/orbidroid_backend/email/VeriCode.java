package com.orbidroid.orbidroid_backend.email;

import com.orbidroid.orbidroid_backend.helper.auth.Password;
import com.orbidroid.orbidroid_backend.helper.misc.Bijection;
import com.orbidroid.orbidroid_backend.helper.time.Adder;
import com.orbidroid.orbidroid_backend.helper.time.Comparator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

// The process for authentication is, when user provides his email,
// we create a VeriCode object in frontend.
// At the same time with creation, an email is sent.
// Then after the user gives an input (in 10 minutes),
// we can use the non-static method isVerCodeCorrect to check if user's input is correct.
// Note that during the process the vericode is encrypted using our encryption function!

// Also, please note that the domain check should be done in fronter place. (Function is already provided.)
public class VeriCode {
    private final String hashedCode;
    private final String sentTime;
    private final String expireTime;

    public VeriCode(String receiver) throws MessagingException {
        this.hashedCode = sendCode(receiver);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        this.sentTime = sdfDate.format(now);

        this.expireTime = Adder.add(this.sentTime, Bijection.getVeriCodeTimeOutInMinutes());
    }

    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    // return hashed version of the code
    public static String sendCode(String receiver) throws MessagingException {
        Properties props = new Properties();
        // debug testing
        props.setProperty("mail.debug", "true");
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
        msg.setSubject("NUS Health Verification Code");
        // set email content
        String code = randomCode();
        String content = Template.getVeriHtml(code);
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
            return Password.encrypt(code);
    }

    public boolean isExpired() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String dateNow = sdfDate.format(now);
        return Comparator.isDateTimeEarlier(this.expireTime, dateNow);
    }

    public boolean isVerCodeCorrect(String userInput) {
        return this.hashedCode.equals(Password.encrypt(userInput));
    }

}

