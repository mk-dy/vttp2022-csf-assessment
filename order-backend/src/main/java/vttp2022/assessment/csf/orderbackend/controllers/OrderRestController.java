package vttp2022.assessment.csf.orderbackend.controllers;

import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path="/api",produces=MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {

    @Autowired
    private OrderService orderSvc;

    @PostMapping(path="/order")
    public ResponseEntity<String> addContact(@RequestBody String payload) {
        
        System.out.println("Payload: %s".formatted(payload));

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();
        Order order = new Order();
        order.setName(data.getString("name"));
        order.setEmail(data.getString("email"));
        order.setSize(data.getInt("size"));
        order.setThickCrust(data.getBoolean("base"));
        order.setSauce(data.getString("sauce"));
        JsonArray jArray= data.getJsonArray("toppings");
        List<String> tempHolder = new LinkedList<>();
        for(int i = 0; i < jArray.size(); i++) {
            tempHolder.add(jArray.getString(i));
        }
        order.setToppings(tempHolder);
        System.out.println(">>>> TEST: "+ order.getToppings().toString());
        order.setComments(data.getString("comments"));
        
        try {
            orderSvc.createOrder(order);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                Json.createObjectBuilder()
                .add("message","Successfully added")
                .build()
                .toString());
            

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Json.createObjectBuilder()
                .add("message","Unable to process order")
                .build()
                .toString()  
            );
                
        }
    }

    @GetMapping(path="order/{email}/all")
    public ResponseEntity<String> getOrder(@PathVariable String email) {
        
        List<OrderSummary> orderSummaryList = new LinkedList<>(); 

        try {
            orderSummaryList = orderSvc.getOrdersByEmail(email);
            System.out.println(">>>> test: " + orderSummaryList.get(0).getAmount());
            
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for(OrderSummary orderSum : orderSummaryList){
                JsonObject jsonObj = Json.createObjectBuilder()
                        .add("orderId", orderSum.getOrderId())
                        .add("name",orderSum.getName())
                        .add("email",orderSum.getEmail())
                        .add("amount",orderSum.getAmount())
                        .build();

                arrBuilder.add(jsonObj);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                arrBuilder.build().toString());
            

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Json.createObjectBuilder()
                .add("message","Unable to retrieve order")
                .build()
                .toString()  
            );
                
        }
        // return null;
    }



}
