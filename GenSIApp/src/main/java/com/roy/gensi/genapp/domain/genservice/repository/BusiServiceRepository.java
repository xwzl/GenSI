package com.roy.gensi.genapp.domain.genservice.repository;

import com.roy.gensi.genapp.interfaces.GsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

@Component
public class BusiServiceRepository {

    private List<GsService> busiServices = new ArrayList<>();

    @PostConstruct
    void loadBusiServices() {
        if (busiServices.isEmpty()) {
            final ServiceLoader<GsService> gsServices = ServiceLoader.load(GsService.class);
            final Iterator<GsService> iterator = gsServices.iterator();
            while (iterator.hasNext()) {
                busiServices.add(iterator.next());
            }
        }
    }

    public GsService getGsService(String serviceCode) {
        for (GsService gsService : busiServices) {
            if (serviceCode.equals(gsService.serviceCode())) {
                return gsService;
            }
        }
        return null;
    }
}