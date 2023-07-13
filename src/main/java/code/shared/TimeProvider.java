package code.shared;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime getCurrentDate();
}
