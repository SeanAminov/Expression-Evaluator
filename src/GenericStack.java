import java.util.ArrayList;
import java.util.EmptyStackException;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericStack.
 *
 * @param <E> the element type
 */

/**
 * @author Your name here
 *
 */
public class GenericStack<E> {

	/** Using an ArrayList as the data structure for the stack */
	private ArrayList<E> stack;
	
	/**
	 * Instantiates a new generic stack.
	 */
	public GenericStack() {
		stack = new ArrayList<E>();
	}
	
	public boolean empty() {
		if (stack.size() == 0) { // simple checker
			return true;
		}
		return false;
		
	}
	
	public int size() { // Just to get size
		
		return stack.size();
		
	}
	
	public E peek()  {
		if (stack.size() == 0) {
			throw new EmptyStackException(); // Need to throw here and not at the top of the method
		}
		return stack.get(stack.size() - 1); //size returns 1 higher than the actual
		
	}
	
	public E pop() throws EmptyStackException {
		if (stack.size() == 0) {
			throw new EmptyStackException();
		}
		return stack.remove(stack.size() - 1); // remove returns the object removed
	}
	
	public void push(E o) {
		stack.add(o);
	}
	
	/**
	 * You need to implement the following functions
	 * a) empty() - returns true if the element is empty
	 * b) size() - returns the size of the Stack
	 * c) peek() - returns the object that is at the top of the stack. Must throw
	 *             appropriate exception if attempt to peek empty stack
	 * d) pop() - gets the object at the top of stack, then removes it from 
	 *            the stack and returns the object. Must throw appropriate exception if
	 *            attempt to pop from empty stack.
	 * e) push(o) - adds the object to the top of stack/
	 * 
	 */
	
	/**
	 * 	 * To string
	 * 	 	 *
	 * 	 	 	 * @return the string
	 * 	 	 	 	 */
	@Override
   	public String toString() {
	   return("stack: "+stack.toString());
	}
}
