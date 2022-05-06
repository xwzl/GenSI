import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/7
 * @description:
 **/

public class DayTest {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddhhmmss")));
    }
}
