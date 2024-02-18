package com.info6250.unichef.service;

import com.info6250.unichef.model.Order;
import com.info6250.unichef.model.PlanType;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, Order order, Locale locale) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        final Context ctx = new Context(locale);
        ctx.setVariable("plans", order.getPlans());

        PlanType planType = order.getPlans().stream().findFirst().get().getPlanType();
        String template = switch (planType) {
            case RESERVATION -> "reservation-order-email-template";
            case INDIVIDUAL -> "individual-order-email-template";
            default -> "";
        };


        final String htmlContent = this.templateEngine.process(template, ctx);
        message.setText(htmlContent, "utf-8", "html");

        javaMailSender.send(message);
    }



}
