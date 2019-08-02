package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.Mail;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import com.example.notificationservice.notificationservice.model.Notification;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class EmailNotificationSenderImpl implements NotificationSender {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private NotificationRepository notificationRepository;

    private static Logger log = LoggerFactory.getLogger(EmailNotificationSenderImpl.class);

    @Autowired
    public EmailNotificationSenderImpl(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void handleMessage(Customer customer, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        String email = customer.getEmail();
        String message = templateMessage(movieScheduleInfoWrapper);
        prepareMail(email, message);
    }

    @Override
    public void setCustomer(Customer customer) {

    }

    @Override
    public void setWrapper(MovieScheduleInfoWrapper wrapper) {

    }

    @Override
    public String call() {
        return null;
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
            //persistTransactions(msg, mail.getMailContent());
            //log.info("Emails sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void persistTransactions(MimeMessage mimeMessage, String message) throws MessagingException {
        Arrays.stream(mimeMessage.getAllRecipients()).forEach(e -> {
                Notification notification = new Notification(e.toString(),message,Calendar.getInstance(),false);
                notificationRepository.save(notification);
                //log.info("Transactions persisted.");
        });

    }



}