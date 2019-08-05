package com.example.notificationservice.notificationservice.service;


import com.example.notificationservice.notificationservice.model.ContactOption;
import com.example.notificationservice.notificationservice.model.Customer;
import com.example.notificationservice.notificationservice.model.MovieScheduleInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class NotificationSenderResolver {

    @Autowired
    private SNSNotificationSender snsNotificationSender;
    @Autowired
    private Map<ContactOption, NotificationSender> senderMap;
    @Autowired
    private BeanFactory beanFactory;


    private static Logger log = LoggerFactory.getLogger(NotificationSenderResolver.class);



//    public void resolve(List<Customer> customers, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
//        log.info("### STARTING THE RESOLVE ###");
//        snsNotificationSender.handleMessage(movieScheduleInfoWrapper);
//        log.info("### FINISHED WITH THE SNS ###");
//        log.info("### STARTING MOCKED EMAIL ON 10000 CUSTOMERS");
//        customers.forEach(c -> c.getContactOptions().forEach(option -> senderMap.get(option).handleMessage(c,movieScheduleInfoWrapper)));
//        log.info("### FINISHED SENDING MOCKED EMAIL ON 10000 CUSTOMERS");
//}
    public void resolve(List<Customer> customers, MovieScheduleInfoWrapper movieScheduleInfoWrapper) {
        ExecutorService executorService = (ExecutorService) beanFactory.getBean("executorService",10);
        customers.forEach(c -> c.getContactOptions().forEach(option -> {
            NotificationSender sender = senderMap.get(option);
            sender.setCustomer(c);
            sender.setWrapper(movieScheduleInfoWrapper);
            try {
              executorService.submit(sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        log.info("### FINISHED SENDING MOCKED EMAIL ON 10000 CUSTOMERS");
    }

}
