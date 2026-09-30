package dev.aihub.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) { SpringApplication.run(ShopApplication.class, args); }
}

record Product(long id, String name, String category, String description, BigDecimal price, String icon, String color, int stock, String badge) {
    Product withStock(int value) { return new Product(id, name, category, description, price, icon, color, value, badge); }
}
record OrderItem(long productId, String name, int quantity, BigDecimal unitPrice) {}
record Order(long id, String customer, String email, List<OrderItem> items, BigDecimal total, Instant createdAt) {}
record CartLine(@Min(1) long productId, @Min(1) int quantity) {}
record CheckoutRequest(@NotBlank String customer, @Email @NotBlank String email, @NotEmpty List<@Valid CartLine> items) {}
record ShopState(List<Product> products, List<Order> orders, long nextOrderId) {}
record ProductPage(List<Product> items, int total, int page, int size) {}

@RestController
@RequestMapping("/api")
class ShopController {
    private final Map<Long, Product> products = new LinkedHashMap<>();
    private final List<Order> orders = new ArrayList<>();
    private long nextOrderId = 1001;
    private final StateFile<ShopState> store;

    ShopController(ObjectMapper mapper, @Value("${aihub.data-file}") String filename) {
        store = new StateFile<>(mapper, filename, ShopState.class);
        var previous = store.read();
        if (previous.isPresent()) {
            ShopState state = previous.get();
            state.products().forEach(p -> products.put(p.id(), p));
            orders.addAll(state.orders());
            nextOrderId = state.nextOrderId();
            return;
        }
        seed(1, "Arc 台灯", "家居生活", "以温柔弧线点亮日常阅读时光。", "329.00", "💡", "peach", 18, "热卖");
        seed(2, "Daily 保温杯", "生活好物", "随身带走恰到好处的温度。", "169.00", "☕", "sage", 32, "新品");
        seed(3, "Soft Linen 抱枕", "家居生活", "天然织感，为沙发添一份松弛。", "199.00", "🛋️", "lilac", 24, "");
        seed(4, "Note 设计手帐", "文具办公", "把琐碎灵感变成有序的记录。", "89.00", "📓", "sand", 56, "人气");
        seed(5, "Minimal 无线音箱", "数码配件", "轻巧机身，丰盈好声音。", "459.00", "🔊", "blue", 11, "精选");
        seed(6, "Weekend 帆布包", "生活好物", "装下周末与下一程的期待。", "139.00", "👜", "rose", 40, "");
    }
    private void seed(long id, String name, String category, String description, String price, String icon, String color, int stock, String badge) {
        products.put(id, new Product(id, name, category, description, new BigDecimal(price), icon, color, stock, badge));
    }
    @GetMapping("/products") synchronized List<Product> products() { return List.copyOf(products.values()); }
    @GetMapping("/products/{id}") synchronized Product product(@PathVariable long id) {
        Product product = products.get(id);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品不存在");
        return product;
    }
    @GetMapping("/products/search") synchronized ProductPage search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "false") boolean inStock) {
        if (page < 0 || size < 1 || size > 100 || minPrice != null && minPrice.signum() < 0
                || maxPrice != null && maxPrice.signum() < 0
                || minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "筛选参数无效");
        }
        Comparator<Product> comparator = switch (sort) {
            case "default" -> Comparator.comparingLong(Product::id);
            case "price_asc" -> Comparator.comparing(Product::price).thenComparingLong(Product::id);
            case "price_desc" -> Comparator.comparing(Product::price).reversed().thenComparingLong(Product::id);
            case "newest" -> Comparator.comparingLong(Product::id).reversed();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "排序参数无效");
        };
        String term = keyword.trim().toLowerCase(Locale.ROOT);
        List<Product> matched = products.values().stream()
                .filter(p -> term.isEmpty() || p.name().toLowerCase(Locale.ROOT).contains(term)
                        || p.description().toLowerCase(Locale.ROOT).contains(term))
                .filter(p -> category.isBlank() || p.category().equals(category))
                .filter(p -> minPrice == null || p.price().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.price().compareTo(maxPrice) <= 0)
                .filter(p -> !inStock || p.stock() > 0)
                .sorted(comparator).toList();
        long start = (long) page * size;
        if (start >= matched.size()) return new ProductPage(List.of(), matched.size(), page, size);
        return new ProductPage(matched.subList((int) start, (int) Math.min(start + size, matched.size())), matched.size(), page, size);
    }
    @PostMapping("/orders") @ResponseStatus(HttpStatus.CREATED)
    synchronized Order checkout(@Valid @RequestBody CheckoutRequest request) {
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        for (CartLine line : request.items()) quantities.merge(line.productId(), line.quantity(), Integer::sum);
        List<OrderItem> lines = new ArrayList<>();
        for (var entry : quantities.entrySet()) {
            Product p = products.get(entry.getKey());
            if (p == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品不存在");
            if (entry.getValue() > p.stock()) throw new ResponseStatusException(HttpStatus.CONFLICT, p.name() + " 库存不足");
            lines.add(new OrderItem(p.id(), p.name(), entry.getValue(), p.price()));
        }
        BigDecimal total = lines.stream().map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<Long, Product> updatedProducts = new LinkedHashMap<>(products);
        lines.forEach(i -> updatedProducts.computeIfPresent(i.productId(), (id, p) -> p.withStock(p.stock() - i.quantity())));
        Order order = new Order(nextOrderId, request.customer().trim(), request.email().trim(), List.copyOf(lines), total, Instant.now());
        List<Order> updatedOrders = new ArrayList<>(orders);
        updatedOrders.add(order);
        store.write(new ShopState(List.copyOf(updatedProducts.values()), List.copyOf(updatedOrders), nextOrderId + 1));
        products.clear(); products.putAll(updatedProducts);
        orders.add(order); nextOrderId++;
        return order;
    }
}
