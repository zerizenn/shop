package by.vstu.shop.service;

import by.vstu.shop.dto.OrderDTO;
import by.vstu.shop.entity.*;
import by.vstu.shop.repository.OrderRepository;
import by.vstu.shop.repository.ProductRepository;
import by.vstu.shop.repository.UserRepository;
import by.vstu.shop.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<OrderDTO.Response> getAll() {
        return orderRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    //эндпоинт: заказы текущего пользователя
    public List<OrderDTO.Response> getMyOrders() {
        Long userId = getCurrentUserId();
        return orderRepository.findByUserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    //эндпоинт: заказы по статусу
    public List<OrderDTO.Response> getByStatus(String status) {
        Order.EStatus eStatus = Order.EStatus.valueOf(status.toUpperCase());
        return orderRepository.findByStatus(eStatus).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO.Response create(OrderDTO.CreateRequest request) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderDTO.CreateRequest.ItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPriceAtTime(product.getPrice());
            items.add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            //уменьшаем остаток на складе
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);
        }

        order.setItems(items);
        order.setTotalAmount(total);
        return toResponse(orderRepository.save(order));
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setDeleted(true);
        orderRepository.save(order);
    }

    private OrderDTO.Response toResponse(Order order) {
        OrderDTO.Response r = new OrderDTO.Response();
        r.setId(order.getId());
        r.setUsername(order.getUser().getUsername());
        r.setCreatedAt(order.getCreatedAt());
        r.setStatus(order.getStatus().name());
        r.setTotalAmount(order.getTotalAmount());
        r.setItems(order.getItems().stream().map(i -> {
            OrderDTO.Response.ItemResponse ir = new OrderDTO.Response.ItemResponse();
            ir.setProductId(i.getProduct().getId());
            ir.setProductName(i.getProduct().getName());
            ir.setQuantity(i.getQuantity());
            ir.setPriceAtTime(i.getPriceAtTime());
            return ir;
        }).collect(Collectors.toList()));
        return r;
    }

    private Long getCurrentUserId() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
}
