package vttp2022.assessment.csf.orderbackend.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private PricingService priceSvc;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public void createOrder(Order order) {

		orderRepo.insertOrder(order);
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {

		List<Order> orderList = new LinkedList<>();
		orderList = orderRepo.getOrders(email);

		List<OrderSummary> orderSummaryList = new LinkedList<>();

		// iterate through each order
		for (Order order: orderList) {
			OrderSummary orderSummary = new OrderSummary();
			orderSummary.setOrderId(order.getOrderId());
			orderSummary.setName(order.getName());
			orderSummary.setEmail(order.getEmail());
			
			// calculate the total
			Float total = 0.0f;

			// size first
			total += priceSvc.size(order.getSize());

			// sauce
			total += priceSvc.sauce(order.getSauce());

			// toppings

			for (String top: order.getToppings()) {
				total += priceSvc.topping(top);
			}

			orderSummary.setAmount(total);
			
			orderSummaryList.add(orderSummary);
		}

		return orderSummaryList;
	}
}
