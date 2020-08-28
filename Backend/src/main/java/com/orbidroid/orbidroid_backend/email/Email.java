package com.orbidroid.orbidroid_backend.email;



import com.orbidroid.orbidroid_backend.helper.misc.Bijection;

import java.util.Properties;
import javax.mail.*;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    // we assume right now other forms of domain is incorrect and
    // should not be able to register as a user
    // now it supports nus students, nus staff, yale/duke-nus students/staff
    public static boolean isNusDomain(String email) {
        // nus students
        if (email.endsWith("u.nus.edu")) {
            return true;
        }
        // nus staff
        if (email.endsWith("nus.edu.sg")) {
            return true;
        }
        // yale-nus students / staff
        if (email.endsWith("u.yale-nus.edu.sg")) {
            return true;
        }
        // duke-nus students / staff
        if (email.endsWith("u.duke.nus.edu")) {
            return true;
        }
        return false;
    }

}
