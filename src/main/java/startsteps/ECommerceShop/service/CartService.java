package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.repository.CartRepository;

@Service
@AllArgsConstructor
public class CartService {
    final CartRepository cartRepository;



}
