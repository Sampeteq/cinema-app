package code.shared.time;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime getCurrentDate();
}
