package code.mails.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mails")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;

    private String subject;

    private String text;

    private MailType type;

    private LocalDateTime sentAt;

    private Mail(String receiver, String subject, String text, MailType type) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
        this.type = type;
    }

    public static Mail create(String to, String subject, String text, MailType type) {
        return new Mail(
                to,
                subject,
                text,
                type
        );
    }

    public void sentAt(LocalDateTime dateTime) {
        this.sentAt = dateTime;
    }
}
