package startsteps.ECommerceShop.exeptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException (String message){
        super(message);
    }
}
