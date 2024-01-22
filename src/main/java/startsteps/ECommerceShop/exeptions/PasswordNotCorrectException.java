package startsteps.ECommerceShop.exeptions;

public class PasswordNotCorrectException extends RuntimeException{
    public PasswordNotCorrectException(String message){
        super(message);
    }
}
