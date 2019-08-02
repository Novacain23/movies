package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class CallableEmailNotificationSenderImpl implements NotificationSender {

    private JavaMailSender mailSender;
    private NotificationRepository notificationRepository;

    private Customer customer;
    private MovieScheduleInfoWrapper wrapper;

    @Autowired
    private RestTemplate restTemplate;

    private static Logger log = LoggerFactory.getLogger(CallableEmailNotificationSenderImpl.class);


    @Autowired
    public CallableEmailNotificationSenderImpl(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MovieScheduleInfoWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(MovieScheduleInfoWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public String call() {
        handleMessage(getCustomer(),getWrapper());
        return "Da";

    }

    @Override
    public void handleMessage(Customer customer, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        String email = customer.getEmail();
        String message = templateMessage(movieScheduleInfoWrapper);
        prepareMail(email, message);

    }
    private void prepareMail(String emailAddress, String message) {
        Mail mail = new Mail();
        mail.setMailSubject("A new movie release.");
        mail.setMailContent(message);
        try {
            sendEmail(mail, new InternetAddress(emailAddress));
        } catch (UnsupportedEncodingException | AddressException e) {
            e.printStackTrace();
        }
    }

    private String templateMessage(MovieScheduleInfoWrapper wrapper) {
        return "This is an email for the release of the movie: " + wrapper.getMovie().getMovieName() + ", being made by the director: " + wrapper.getMovie().getDirector() + " " +
                "has a duration of " + wrapper.getMovie().getDuration() + " and has the next genres: " + wrapper.getMovie().getGenres() + ". " +
                "It will be released on " + wrapper.getScheduleInfo().getReleaseDate().getTime().toString() + " until " + wrapper.getScheduleInfo().getEndDate().getTime().toString() +
                " in the " + wrapper.getScheduleInfo().getRegion() + " region.";
    }

    //    public void handleNotification(String message, List<String> emails) {
//        if(emails.isEmpty()) {
//            System.out.println("There is no subscriber interested in these genres or is in the appropriate region.");
//        } else {
//            prepareMail(message, emails);
//        }
//    }
//
    private void sendEmail(Mail mail, Address address) throws UnsupportedEncodingException {
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            msg.setRecipient(Message.RecipientType.TO, address);
            MimeMessageHelper mHelper = new MimeMessageHelper(msg, true);
            mHelper.setSubject(mail.getMailSubject());
            mHelper.setFrom(new InternetAddress("testsoftvision10@gmail.com", "Softvisioner"));
            mHelper.setText(mail.getMailContent(), true);
            //mailSender.send(mHelper.getMimeMessage());
            persistTransactions(msg, mail.getMailContent());
            log.info("Emails sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void persistTransactions(MimeMessage mimeMessage, String message) throws MessagingException {
        Arrays.stream(mimeMessage.getAllRecipients()).forEach(e -> {
            Notification notification = new Notification(e.toString(),message, Calendar.getInstance(),false);
            notificationRepository.save(notification);
            //log.info("Transactions persisted.");
        });

    }


}
