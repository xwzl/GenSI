import com.roy.gensi.genapp.interfaces.GsService;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/7
 * @description:
 **/

public class SPITest {

    public static void main(String[] args) {
        ServiceLoader<GsService> gsServices = ServiceLoader.load(GsService.class);
        final Iterator<GsService> iterator = gsServices.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
