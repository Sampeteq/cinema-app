package code;

import code.mails.domain.Mail;
import code.mails.domain.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class MockMailService implements MailService {

    @Override
    public void sendMail(Mail mail) {
      log.info("MailService is mocked");
    }
}
