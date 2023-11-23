package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.domain.MailType;
import com.greenblat.tasklist.domain.user.User;
import com.greenblat.tasklist.service.MailService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(User user, MailType type, Properties params) {
        switch (type) {
            case REGISTRATION -> sendRegistrationEmail(user, params);
            case REMINDER -> sendReminderEmail(user, params);
            default -> {}
        }
    }


    @SneakyThrows
    private void sendRegistrationEmail(User user, Properties params) {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("Thanks for registration, " + user.getName());
        helper.setTo(user.getUsername());
        String mailContent = getRegistrationEmailContent(user, params);
        helper.setText(mailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendReminderEmail(User user, Properties params) {
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("You have task todo in 1 hour");
        helper.setTo(user.getUsername());
        String mailContent = getReminderEmailContent(user, params);
        helper.setText(mailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getReminderEmailContent(User user, Properties params) {
        var writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", params.getProperty("task.title"));
        model.put("description", params.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user, Properties params) {
        var writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

}
