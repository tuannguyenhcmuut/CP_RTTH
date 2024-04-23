package org.ut.server.omsserver.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public class RestParamUtils {
    private RestParamUtils() {
    }
    public static String getParam(String param) {
        return param;
    }

    public static List<Order> getSortOrder(String[] sort) {
        List<Order> orders = new ArrayList<Order>();
        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        return orders;
    }

    public static Pageable getPageable(int page, int size, String[] sort) {
        return PageRequest.of(page, size, Sort.by(getSortOrder(sort)));
    }

    private static Direction getSortDirection(String direction) {
        return direction.equals("asc") ? Direction.ASC : Direction.DESC;
    }
}
