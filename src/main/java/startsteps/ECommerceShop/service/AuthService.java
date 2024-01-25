package startsteps.ECommerceShop.service;

import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.request.SighInRequest;
import startsteps.ECommerceShop.request.SighUpRequest;
import startsteps.ECommerceShop.responce.JwtAuthResponse;

public interface AuthService {
    JwtAuthResponse signup(SighUpRequest sighUpRequest);
    JwtAuthResponse signin(SighInRequest sighInRequest);

    User getAuthenticatedUser();
}
