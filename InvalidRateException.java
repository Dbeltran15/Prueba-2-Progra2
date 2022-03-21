
package PruebaS9;

public class InvalidRateException extends IndexOutOfBoundsException{

    public InvalidRateException(int a)
    {
        super(a + "Is not a valid number for a review.");
    }
    
}
