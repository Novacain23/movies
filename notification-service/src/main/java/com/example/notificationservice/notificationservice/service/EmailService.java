package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service("mailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerRepository customerRepository;


    private void sendEmail(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            mimeMessage.setRecipients(Message.RecipientType.TO, getInternetAddresses());
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress("testsoftvision10@gmail.com", "Softvisioner"));
            mimeMessageHelper.setText(mail.getMailContent(), true);


            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void prepareMail(String message){
        Mail mail = new Mail();
        mail.setMailSubject("A new movie release.");
        mail.setMailContent(message);
        try {
            sendEmail(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private InternetAddress[] getInternetAddresses() {
        List<Customer> nonCorporateCustomers = customerRepository.findAllNonCorporateCustomers();
        InternetAddress[] addresses = new InternetAddress[nonCorporateCustomers.size()];
        int count = 0;
        for(Customer c : nonCorporateCustomers) {
            try {
                addresses[count] = new InternetAddress(c.getEmail());
                count++;
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return addresses;
    }


}