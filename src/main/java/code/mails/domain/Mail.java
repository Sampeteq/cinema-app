package code.mails.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mails")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;

    private String subject;

    private String text;

    private MailType type;

    private LocalDateTime sentAt;

    public Mail(String receiver, String subject, String text, MailType type) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
        this.type = type;
    }

    public void sentAt(LocalDateTime dateTime) {
        this.sentAt = dateTime;
    }
}
