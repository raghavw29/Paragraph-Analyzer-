import java.util.*;

/**
 * A class to represent a linked list of nodes.
 * @author Raghav Wadhwa
 */
public class LinkedList<T> implements Iterable<T> {
  // the first node of the list, or null if the list is empty 
  private LLNode<T> first;
  
  
  /**
   * Creates an initially empty linked list
   */
  public LinkedList() {
    first = null;
  }
  
  protected LLNode<T> getFirst() {
    return first;
  }
  
  /**
   * Changes the first node.
   * @param node  the node that will be the first node of the new linked list
   */
  protected void setFirst(LLNode<T> node) {
    this.first = node;
  }
  
  /**
   * Add an element to the front of the linked list
   */
  public void addToFront(T element) {
    setFirst(new LLNode<T>(element, getFirst()));
  }
  
  /**
   * Return whether the list is empty
   * @return true if the list is empty
   */
  public boolean isEmpty() {
    return (getFirst() == null);
  }
  
  /**
   * Returns the length of the linked list
   * @return the number of nodes in the list
   */
  public int length() {
    int lengthSoFar = 0;
    LLNode<T> nodeptr = getFirst();
    while (nodeptr != null) {
      lengthSoFar++;
      nodeptr = nodeptr.getNext();
    }
    return lengthSoFar;
  }
  
  /**
   * Returns a String representation of the list
   * @return a String representing the list
   */
  public String toString() {
    return null;
  }
  
  /**
   * Determines whether an element is stored in the list
   * @param element  the element to search for in the list
   * @return true if and only if the parameter element is in the list
   */
  public boolean contains(T element) {
    LLNode<T> node = first;
    while (node != null ) {
      if(node.getElement() == element) {
        return true;
      }
      node = node.getNext(); // this was error
    }
    return false;
  }
  
  /**
   * Deletes the first occurrence of an element in the list.
   * If the element is not in the list, the list is unchanged.
   * @param element  the element to remove
   */
  public void remove(T element) {
    
  }
  
  /**
   * Converts the linkedlist into an array list
   * @return Returns an arraylist with same info as linkedlist.
   */
  public ArrayList<T> toArrayList () {
    int size = length();
    ArrayList<T> newArr = new ArrayList<T>(size);
    LLNode<T> node = first;
    while(node != null) {
      newArr.add(node.getElement()); 
      node = node.getNext();
    }
    return newArr;
  }
  
  @Override
  public LLiterator<T> iterator(){
    
    return (LLiterator<T>) new Mylliterator( );
  }
  
  
  /**
   * Returns the first node.
   * @author Raghav Wadhwa
   */
  public  class Mylliterator implements LLiterator<T> {
    // keeps track of the node previous to one returned by next()
    private LLNode<T> iterPrevnode; 
    // keeps track of the node previous to one returned by next()
    private LLNode<T> iternode;  
    // should not need this
    private LLNode<T> iterNextnode; 
    
   /**
    * Sets up the iterators previous and nextnode
    */
    public Mylliterator() {
      iternode = null;  // initialize all to null to start with
      iterPrevnode = null;
      iterNextnode = null;
    }
    
    /**
    * Gets the element of the next node
    * @return Returns the element in the next node
    */
    public T getNextNodeElement(){
      return iternode.getNext().getElement();
    }
   
    /**
    * Adds element before the input element
    * @param element  takes an element to add
    */
    public void addBefore(T element){
      LLNode<T> newnode = null;
      
      if ( iternode == null) {
        //System.out.println("No Current node to add before");   
        throw new NoSuchElementException();
      }
      else {
        // add before iternode, so add between  iterPrevnode and iternode
        
        newnode = new LLNode<T>(element, (LLNode<T>) iternode);
        if (first == iternode) {
          setFirst(newnode);
        }
        if (iterPrevnode != null) {     
          iterPrevnode.setNext(newnode);
        } else {
          iterPrevnode = newnode;   // update previous node pointer
        }
      }
    }
    
     /**
    * Adds element after the input element
    * @param element  takes an element to add 
    */
    public  void addAfter(T element) {
      LLNode<T> nextnode = null;
      LLNode<T> newnode = null;
      
      if (isEmpty() || iternode == null) {
        addToFront(element) ;
      }
      else {
        // add between iternode and iternode->next
        nextnode = iternode.getNext(); // this should be same is iterNextnode  
        newnode = new LLNode<T>(element, (LLNode<T>) nextnode);
        iternode.setNext(newnode);
        iterNextnode = newnode;  // update next node pointer
      }
    }
    
    /**
     * Determines if the linkedlist has a next node
     * If the element is not in the list, the list is unchanged.
     * @param element  the element to remove
     * @return returns true if node is present and false if not
     */
    public boolean hasNext() {   
      if (iternode == null) {
        // iteration has not started so check first node
        return (first != null);
      }else {    
        return (iternode.getNext() != null);  // check if next exists
      }
    }
    
    /**
     * Returns the current element of the iterator
     * @Return returns a the element of a node in the linkedlikst
     */
    public T next() {
      if (first == null) {
        return null;
      }
      if (iternode == null) {    
        iternode = first;
        iterPrevnode = null;
        iterNextnode = iternode.getNext();    
        return first.getElement();
      } else {
        iterPrevnode = iternode;    
        iternode =  iternode.getNext();  
        iterNextnode = iternode.getNext();  
        return iternode.getElement();   // return current element
      }
    }
    
    /**
     * Does not have a function
     */
    public void remove() {
      throw new UnsupportedOperationException();   
    }
    
  } 
  
  /**
   * The node of a linked list
   * @author Raghav Wadhwa
   */
  public class LLNode<T> {
    // the element stored in the node 
    private T element;
    
    // a reference to the next node of the list 
    private LLNode<T> next;
    
    /**
     * the constructor
     * @param element  the element to store in the node
     * @param next  a reference to the next node of the list 
     */
    public LLNode(T element, LLNode<T> next) {
      this.element = element;
      this.next = next;
      
    }
    
    /**
     * Returns the element stored in the node
     * @return the element stored in the node
     */
    public T getElement() {
      return element;
    }
    
    /**
     * Returns the next node of the list
     * @return the next node of the list
     */
    public LLNode<T> getNext() {
      return next;
    }
    
    /**
     * Changes the node that comes after this node in the list
     * @param next  the node that should come after this node in the list.  It can be null.
     */
    public void setNext(LLNode<T> next) {
      this.next = next;
    }
    
    /**
     * Returns the length after this node to the end of the list. 
     * @return the length of the list, after this node and not including this node
     */
    public int lengthFromHere() {
      if (getNext() == null)                    // this is the end of the list so
        return 0;                               //   there are no more nodes
      else                                      // this is not the end of the list so
        return 1 + getNext().lengthFromHere();  //   the length is 1 more than next's length
    }
    
  }
}