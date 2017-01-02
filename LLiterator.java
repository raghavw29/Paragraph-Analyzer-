import java.util.*;
/**
*Iterface for the LLiterator
* @author Raghav Wadhwa
*/
public interface  LLiterator<T> extends Iterator <T> {

/**
*Adds element before the current element
* @param element  element to add
*/
public void addBefore(T element);

/**
*Adds element after the current element
* @param element  element to add
*/
public void addAfter(T element);
 
}
