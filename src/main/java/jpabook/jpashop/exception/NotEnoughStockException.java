package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
    // 오ㅓ라이딩한이유 메세지, 메소
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    //메세지, 예외가 발생한 근원적인 예외를넣어서  trace가 쭉나오게
    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }


}
