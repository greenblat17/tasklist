package com.greenblat.tasklist.service;

import com.greenblat.tasklist.domain.MailType;
import com.greenblat.tasklist.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType type, Properties params);

}
