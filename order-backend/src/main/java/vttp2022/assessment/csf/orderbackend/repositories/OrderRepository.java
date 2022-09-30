package vttp2022.assessment.csf.orderbackend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;

import static vttp2022.assessment.csf.orderbackend.repositories.Queries.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Repository
public class OrderRepository {
    
    @Autowired
    private JdbcTemplate template;
    
    public boolean insertOrder(Order order) {
        int count = template.update(
            SQL_INSERT_ORDER,
            order.getName(),
            order.getEmail(),
            order.getSize(),
            order.isThickCrust(),
            order.getSauce(),
            String.join(",", order.getToppings()),
            order.getComments());
        return count == 1;
    }

    public List<Order> getOrders(String email) {
        
        List<Order> list = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ORDERS_BY_EMAIL,email);
        while (rs.next()) {
            Order order = new Order();
            order.setOrderId(rs.getInt("order_id"));
            order.setName(rs.getString("name"));
            order.setEmail(rs.getString("email"));
            order.setSize(rs.getInt("pizza_size"));
            order.setThickCrust(rs.getBoolean("thick_crust"));
            order.setSauce(rs.getString("sauce"));
            order.setToppings(
                new LinkedList<String>(Arrays.asList(rs.getString("toppings").split(",")))    
                );
            order.setComments(rs.getString("comments"));
            list.add(order);
        }
        return list;
    }

}
