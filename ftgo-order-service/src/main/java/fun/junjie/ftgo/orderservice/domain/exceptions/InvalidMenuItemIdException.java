package fun.junjie.ftgo.orderservice.domain.exceptions;

public class InvalidMenuItemIdException extends RuntimeException {
    public InvalidMenuItemIdException(String menuItemId) {
        super("Invalid menu item id " + menuItemId);
    }
}
