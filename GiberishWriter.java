
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Collections;
import java.util.Comparator;

/**
 * A class to represent a giberish writer containing multiple inner classes
 * This includes the extra credit tasks 
 * @author Raghav Wadhwa
 */
public class GiberishWriter implements Iterator<String> { 
  
  private ArrayList<ContexData>  ctxData;
  // Contains the size of the context
  private int ctxSize; 
  // Contains the next context after current
  private Contex lastIterCtx; 
  // parameters for extra credit 
  
// Keeps string used to begin iteration
  private String iterString;  
  // stop iteration if context not found during iteration
  private boolean stopIteration;  
  // used to attach the initial string only once
  private boolean attachOutputString; 
  // used to make output string
  private String GWstring;  
  // used to switch between running without extra input parameters
  private boolean ignoreExtra;  
  
  /**
   * Creates a GiberishWriter object which reads a context and outputs new context
   * @param size  Contains size of the context to be searched for
   */
  GiberishWriter(int size) {
    ctxData = new ArrayList<ContexData>();
    ctxSize = size;  // size of context inside ContextData
    lastIterCtx = null;
    ignoreExtra = true;
    iterString = null;
    stopIteration = true;
    attachOutputString = false;
    GWstring = "";
  }
  
  /**
   * Getter for ctxSize field
   * @return Returns the size of context set by user
   */
  public int getContextSize () {
    return ctxSize;
  }
  
  public void remove(){  
    throw new UnsupportedOperationException();  
  } 
  
  /**
   * Gets the context data at given index from array list 
   * @param index  for which the context data is needed 
   * @return Returns the size of context set by user
   */
  public ContexData getContextData(int index) {
    return ctxData.get(index);
  }
  
  /**
   * appends the word to output string for final output 
   * needed for extra credit task
   * @param word  returned by nextExtra call to be appended to output string
   */
  public void addOutStringExtra(String word) {
    GWstring = GWstring + " " + word;
  }
  
  /**
   * Gets the output string
   * @return returns output string for final output 
   */
  public String getOutStringExtra() {
    return GWstring;
  }
  
  /**
   * sets the boolean to switch task between normal Giberish writer task versus extra tasks
   * @param  value to set
   */
  public void setExtra(boolean val) {
    this.ignoreExtra = val;
  }
  
  /**
   * Determines if array list has a next element
   * @return Returns a boolean value if the ctxData size is greater than 0
   */
  public boolean hasNext() {
    return (ctxData.size() > 0);
  }
  
  /**
   * sets the parameters for iteration for extra tasks
   * @param input  is string given by user and used as last context to start iteration
   */
  public  void iterationStart(String input) {
    // save the start input string
    this.iterString = input;
    this.stopIteration = false;
    this.attachOutputString = true;
    // contextSize from length of input string
  }
  
  /**
   * Determines if arraylist has a next element based on extra task 
   * @return  Returns a boolean value if context data is empty or stopIteration has been set in nextExtra call
   */
  public boolean hasNextExtra() {
    boolean ret = false;
    ret = (ctxData.size() > 0) && stopIteration == false;
    //System.out.println("size stop val" + ctxData.size() + stopIteration + ret);
    return (ret);
  }
  
  /**
   * validates words read from file 
   * a words is valid if it is seqience of letters or digits or pure seqence of non alphanumeric characters
   * @param input  is string given by user and used as last context to start iteration
   */
  public static String validateWord(String input) {
    String regexDigit = "\\d+";
    String regexAlpha = "[a-zA-Z]+";
    char a = input.charAt(0);
    if (Character.isDigit(a)) {
      if (input.matches(regexDigit)) {
        input = removePunctuation(input);
        return input;
      }
    } else if (Character.isLetter(a)) {
      if (input.matches(regexAlpha)) {
        input = removePunctuation(input);
        return input;
      }
    } else {
      // iterates through the input string and determines if word is a whitespace
      //weakest precondition is that i+1 is a char
      for (int i = 0; i < input.length(); i++) {
        a = input.charAt(i);
        if (Character.isDigit(a) || Character.isLetter(a) || Character.isWhitespace(a)) {
          System.out.println("has Char letter or digit or ws ");
          return "";
        }  
      }
      //System.out.println("special seq  ");
      return input;
    }
    return "";
  }
  
  /**
   * Creates a new Current Iterator Context
   * copy word as last word of old context
   * if passed parameter oldcontex has only one word then neword will become the context
   * @param newword  Contains a string which represents a new word
   * @param oldContex Contains the OldContex previous to current.
   * @return Returns the new Context
   */
  public Contex makeNewIterContex(String newword, Contex oldContex) {
    String[] s = new String[oldContex.length()];
    int i = 0;
    // copies the context into s shifted up by one word`
    for ( i=0 ; i< oldContex.length()-1 ; i++) {
      s[i] = oldContex.getWord(i+1);
    }
    
    s[i] = newword;
    
    Contex newC  = new Contex(s);
    return newC;
  }
  
  /**
   * implements next call for iterator
   * this function gets a random following word from last context and changes the context to new one
   * @return Returns next word from context data based on last context and random number generated
   */
  public String next() {
    int index;
    ContexData lastcd;
    int num = 0;
    Random rand = new Random(); 
    
    if (lastIterCtx == null) {
      index = rand.nextInt(ctxData.size()); // generate random integer between 0 and size of ArrayList   
    } else {
      ContexData searchKey = new ContexData(lastIterCtx);
      index = Collections.binarySearch(ctxData, searchKey, new binComp());
    }
    if (index >= ctxData.size() || index < 0 ) {
      index = 0;
    }
    
    lastcd = ctxData.get(index);
    num = lastcd.numOccurances();
    int rnum = rand.nextInt(num);
    
    if (rnum < 0 || rnum > lastcd.numOccurances()) {
      rnum = 0;
    }
    
    String word = lastcd.getFollowingWord(rnum);
    lastIterCtx = makeNewIterContex(word, lastcd.getContex());
    return word;  
  }
  
  /**
   * Modified next call for extra credit 
   * this function gets a random following word from last context and changes the context to new one
   * This uses iterString from user to begin the iteration and also attaches the input string 
   * to output before adding other Giberish words.
   * @return Returns next word from context data based on last context and random number generated
   */
  public String nextExtra() {
    int index;
    ContexData lastcd;
    int num = 0;
    Random rand = new Random(); 
    
    if (attachOutputString) { // attach the starter iterator string to output in first nextExtra call
      GWstring = this.iterString;
      attachOutputString = false;
      if (iterString != null) {
        String[] words = this.iterString.split("\\s+");
        String[] ctx_input = new String[this.getContextSize()];
        if (words.length >  this.getContextSize()) { // shift old context one down        
          int j = this.getContextSize() - 1;
          // copies starter iterstring to last context 
          for (int i = words.length - 1 ; j >= 0; i--) { 
            ctx_input[j--] = words[i]; 
          }
        } else if (words.length <  this.getContextSize()){
          stopIteration = true;     // stop iteration if size too small
          return " ";
        } else {
          ctx_input = words;
        }
        
        lastIterCtx  = new Contex(ctx_input);
      }
    }
    if (lastIterCtx == null) {
      index = rand.nextInt(ctxData.size()); // generate random integer between 0 and size of ArrayList   
    } else {
      ContexData searchKey = new ContexData(lastIterCtx);
      index = Collections.binarySearch(ctxData, searchKey, new binComp());
    }
    if (index >= ctxData.size() || index < 0 ) {
      index = 0;
      stopIteration = true;  // stop iteration if searched context is not found
      return " ";
    }
    lastcd = ctxData.get(index);
    num = lastcd.numOccurances();
    
    int rnum = rand.nextInt(num);  // get random following word for selected context data
    
    if (rnum < 0 || rnum > lastcd.numOccurances()) {
      rnum = 0;
    }
    String word = lastcd.getFollowingWord(rnum); // get following word usign random numbers
    lastIterCtx = makeNewIterContex(word, lastcd.getContex()); // make new context for next iteration
    return word;  // return word for output string 
  }
  
  /**
   * this function adds context to the ctxData list in sorted way
   * it use the compareTo functions to find the right position to insert the context
   * addBefore addAfter and addToFront functions from linked list are used appropriately
   * @param ctx  Context to be added
   * @param clist Linked list to add the context in sorted way
   */
  public ContexData addContextData(Contex ctx, LinkedList<ContexData> clist) {
    LLiterator<ContexData> iter = clist.iterator();
    // runs while iterator has a next node.
    // weakest precondition is that iterator has a next node
    while (iter.hasNext()) {
      ContexData ctd = (ContexData) iter.next();
      int res = ctd.getContex().compareTo(ctx);
      if (res == 0) {
        return ctd;
      }
      if (res > 0) {
        ContexData ctdnew = new ContexData(ctx);
        iter.addBefore(ctdnew);
        return ctdnew;
      }
      
    }
    ContexData ctd2 = new ContexData(ctx);
    iter.addAfter(ctd2);
    return ctd2;
  }
  
  /**
   * 
   * this function reads words from data file using Scanner 
   * It ends the program if file is not found.
   * It  makes context as per context size set by user and adds to a linked list
   * It looks for context in the list and if the context exists it adds the following word to the context
   * after incrementing the count of number of occurrence in context data and count in word data 
   * @param filename   reads words from file
   */
  public void addDataFile(String filename)  {
    // open file and read word by word using Scanner next() method
    LinkedList <ContexData> ll = new LinkedList<ContexData>();
    Scanner s2;
    try {
      s2 = new Scanner(new File(filename));
    } catch (FileNotFoundException ex) {
      System.out.println("Error: File Not Found: " + filename);
      return;
    }
    int size = this.getContextSize();  // context size
    String [] ctxInput = new String[size];
    
    int cnt = 0;
    while (cnt < size && s2.hasNext()) {
      String out = s2.next();
      
      if (this.ignoreExtra) { // validate the word
        out = removePunctuation(out);
      } else {  //  definition of word modified for extra credit
        out = validateWord(out);
      }
      if (out.length() > 0) { // copy word as context
        ctxInput[cnt] = out;
        cnt++;
      }
    }
    if (cnt < size) {  // check if correct context size word is made
      // context could not be made
      s2.close();
      return;
    }
    // gets following words for above context
    while (s2.hasNext()) {   
      String fword = s2.next(); // get new following word;       
      
      if (this.ignoreExtra) { // validate the following word 
        fword = removePunctuation(fword);
      } else {  //  definition of word modified for extra credit
        fword = validateWord(fword);
      }
      
      if (fword.length() > 0) { // find or create new one and add to list
        Contex ctx = new Contex(ctxInput);
        ContexData ctd = addContextData(ctx, ll); 
        ctd.addFollowingWord(fword);
        
        // move cntInput index by one to make next context
        for (int i = 0; i <  size -1  ; i++) {
          ctxInput[i] = ctxInput[i+1];
        }
        ctxInput[size-1] = fword; // fword becomes the last word in context input 
      }
    }
    ctxData = ll.toArrayList();  // convert link list to array list in same order
    s2.close();    // close the file
  }
  
  
  
  
  /**
   * WordData Class
   * A class to represent a  a word and the number of times it occurs in a contex.
   * @author Raghav Wadhwa
   */
  public static class WordData{
    // Holds the word being added to the word data linkedlist
    private String word;
    // Holds the number of times word occurs
    private int count;
    /**
     * Creates a Word Data containing a word and a count
     * @param wordToSet  Contains the string to set to the word data
     */
    public WordData(String wordToSet){
      word = wordToSet;
      count = 1;
    }
    /**
     * Increments the count variable each time it is called
     */
    public void incrementCount(){
      count++;
    }
    
    /**
     * Returns the value being held by the count variable
     * @return returns the number of times the word occurs
     */
    public int getCount(){
      return count;
    }
    /**
     * Returns the word of the of the word data
     * @return returns the word in the word data object
     */
    public String getWord(){
      return this.word;
    }
    
  }
  
  
  /**
   * Contex Class
   * A class to a represent a  contex which consists of one or more words.
   * @author Raghav Wadhwa
   */
  public static class Contex implements Comparable<Contex>{
    // Holds the String which represents a context.
    String contex;
    // Contains an array of context.
    String [] ContexArray;
    
    /**
     * Creates a Context which consists of an array of string
     * @param inputContexArray  Contains the  input String array to represent the Contex
     */
    public Contex(String[] inputContexArray){
      // Contains the length of the contex Array
      int lengthOfInputContexArray = inputContexArray.length;
      // Contains Array of String.
      ContexArray = new String[lengthOfInputContexArray];
      // loops through input Contex Array and adds elements to the Contex Array
      // weak precondition is ContexArray[i+1] is an element
      for(int i =0; i< inputContexArray.length; i++)
        ContexArray[i] = inputContexArray[i];
    }
    
    /**
     * Returns the length of the Contex Array
     * @return Returns the length of the Contex Array
     */
    public int length(){
      return ContexArray.length;
    }
    
    /**
     * Converts the Contex Array to A String
     * @return  Returns the contex Array in String Form
     */ 
    public String toString(){
      // Contains the first element of the contex array
      contex = ContexArray[0];
      // Loops through contex array and adds each element to a string
      for(int i = 1; i <ContexArray.length; i++){
        contex = contex + " " +  ContexArray[i];
      }
      return contex;
    }
    
    /**
     * Returns the word in the Contex Array
     * @param wordIndex  Contains index of word to return
     * @return Returns the specific word
     */
    public String getWord(int wordIndex){
      return ContexArray[wordIndex];
    }
    
    /**
     * Determines if the two Contexs are equal.
     * @param other  Contains the second contex for comparison
     * @return Returns true if is equal and false if not
     */
    public boolean equals(Contex other){
      // contains number of matches
      int match = 0;
      if(this.ContexArray.length == other.ContexArray.length){
        // Loops through contex arrays for each contex to see if strings are equals
        // Weakest precondition is ContexArray[i+1] is a string
        for(int i = 0; i < other.ContexArray.length; i++){
          if(this.ContexArray[i].equals(other.ContexArray[i]))
            match++;
        }
        if(match == other.ContexArray.length)
          return true;
        else
          return false;
      }
      else{
        return false;
      }
    }
    
    /**
     * Compares two Contexts based on their Strings
     * @param other  Contains the second context for comparison
     * @return Returns -1 if first context is less than second. 0 if they are equal and 1 if second less than first
     */
    public int compareTo(Contex other){
      
      if (this.ContexArray.length > other.ContexArray.length) {
        return 1;
      } 
      if (this.ContexArray.length < other.ContexArray.length) {
        return -1;
      } 
      
      // Loops through Contex Array and and compares each context
      //weakest precondition is ContexArray[i+1] is a context
      for(int i = 0;i < this.ContexArray.length;i++){
        if(this.ContexArray[i].compareTo(other.ContexArray[i]) > 0) {
          return 1;
        }
        if(this.ContexArray[i].compareTo(other.ContexArray[i]) < 0) {
          return -1;
        }    
      }
      return 0;
    }
    
  }  
  
  
  /**
   * ContexData Class 
   * A class that represents a contex data object.
   * @author Raghav Wadhwa
   */
  public static class ContexData implements Comparable<ContexData>{
    // Contains the Context part of Context Data
    private Contex wordContex;
    // Contains the number of times the context occurs
    private int numOfOccur;
    // Contains linked list of words
    LinkedList<WordData> ListOfWords;
    
    /**
     * Creates a Contex Data containing a word and a number of occurrence of context
     * @param contex  Contains the context to set
     */
    public ContexData(Contex contex){
      wordContex = contex;
      numOfOccur = 0;
      ListOfWords = new LinkedList<WordData>();
      
    }
    
    /**
     * Return the context of the context data
     * @return  returns the word of the context
     */
    public Contex getContex(){
      return wordContex;
    }
    
    /**
     * Return the num of occurances of the contex
     * @return  returns num of occurances of the contex data
     */
    public int numOccurances(){
      return numOfOccur;
    }
    
    /**
     * Incremenets the numOfOccurance Variable
     */
    public void incrementNumOccurances(){
      this.numOfOccur++;    
    }
    
    /**
     * Compares two ContexDatas based on their Contexts
     * @param other  Contains the second contexData for comparison
     * @return Returns -1 if first contexData is less than second. 0 if they are equal and 1 if second less than first
     */
    public int compareTo(ContexData other){
      if(this.wordContex.compareTo(other.wordContex) == 1)
        return 1;
      else if(this.wordContex.compareTo(other.wordContex) == -1)
        return -1;
      else
        return 0;
    }
    
    /**
     * Adds a word to the List of Contexts and sorts them
     * @param word  Takes a word to add
     */
    public void addFollowingWord(String word){
      LLiterator<WordData>l1 = ListOfWords.iterator();
      WordData curWordData;
      
      this.incrementNumOccurances();
      
      if (ListOfWords.isEmpty()) {
        // add as first word
        ListOfWords.addToFront(new WordData(word));
        return;
      }
      // Loops through the linked list and adds the word while sorting
      // weakest precondition is when l1 has a next node
      while(l1.hasNext()) {
        curWordData = (WordData)l1.next();
        if(word.equals(curWordData.getWord())) {
          curWordData.incrementCount();
          return;
        }
        else if(word.compareTo(curWordData.getWord()) == -1) {
          l1.addBefore(new WordData(word));
          return;
        }
        
      }
      l1.addAfter(new WordData(word));  // if did not find any thing so far it should be last word.
      
    }
    
    /**
     * Adds a word to the List of Contexts and sorts them
     * @param wordNum  Takes an int index to return
     * @return Returns the word at the index inputted.
     */
    public String getFollowingWord(int wordNum) {
      LLiterator<WordData>l1 = ListOfWords.iterator();
      int totalNumOfOccurances = 0;
      String wordToReturn = null;
      WordData curWordData;
      
      // Loops through the linked list and returns word if numOfOccurances match
      while(l1.hasNext() && totalNumOfOccurances < this.numOccurances()){
        curWordData = (WordData)l1.next();
        totalNumOfOccurances = totalNumOfOccurances + curWordData.getCount();  
        if(totalNumOfOccurances >= wordNum) {
          wordToReturn = curWordData.getWord(); 
          return wordToReturn;
        }
        
      }
      throw new NoSuchElementException();
    }      
  } 
  
  
  /**
   * binComp class 
   * A class that represents an object which compares two other objects
   * This is used for calling Collections.binary_search
   * @author Raghav Wadhwa
   */
  public static class  binComp implements Comparator<ContexData>{  
    /**
     * Compares two ContexData objects
     * @param c1  First Contex Data for comparison
     * @param c2  Second Contex Data for comparison
     * @return Returns the result of the compareTo function
     */ 
    public int compare(ContexData c1, ContexData c2) {   
      return c1.compareTo(c2);
    }
  }
  
  
  /**
   * Method Removes Punctuation and converts to lower case
   * @param takes input string 
   * @return Returns changed word
   */
  public static String removePunctuation (String input) {
    String out = input.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
    out = out.toLowerCase(); 
    //System.out.println(out);
    return out;
  }
  
  /**
   * main method for running the program
   *  Minimum 3 parameters are needed to run
   * @param arg1 filename
   * @param arg2  context size
   * @param arg3  number of output words
   * @param greater than 3 parameters are optional input string for starting context 
   */
  public static void main(String [] args) {
    if (args.length < 3) {
      System.out.println("Need 3 inputs");
      return;
    }
    
    String fname = args[0];
    // System.out.println("Reading file ");
    int size = Integer.parseInt(args[1]);
    int numWords = Integer.parseInt(args[2]);
    boolean ignoreExtra = true;
    String inputString = "";
    if (args.length > 3) {
      String [] iterString = new String[args.length -3 ];
      int j = 0;
      // loops through number of args and checks to see if words are valid
      for (int i = 3; i< args.length; i++) {
        String s = args[i];
        s = validateWord(s);
        if (s.length() == 0) {
          System.out.println("Invalid words in argument : Ignoring "); 
          ignoreExtra = true;
        } else {
          iterString[j++] = s;
        }
      } 
      ignoreExtra = false;
      // extra credit part
      inputString = iterString[0];
      for (int i = 1; i< j; i++) {
        inputString = inputString + " " + iterString[i] ;
      }
      
    }
    
    System.out.println("Reading file " + fname);
    GiberishWriter gw = new GiberishWriter(size);
    gw.setExtra(ignoreExtra);
    gw.addDataFile(fname);
    int i = 0; 
    if (ignoreExtra) {
      System.out.println(" GiberishWriter with no starter input context:  ");
      String outStr = "";
      while (gw.hasNext() && i < numWords) {
        i++;
        outStr += gw.next() + " " ;
      }
      System.out.println(outStr);
      return;
    }
    else {
      // extra credit part
      System.out.println(" GiberishWriter Extra with starter input context:  " + inputString);
      gw.iterationStart(inputString);
      i = 0;
      while (gw.hasNextExtra() && i < numWords) {
        i++;
        String w = gw.nextExtra();
        gw.addOutStringExtra (w);
      }
      System.out.println(gw.getOutStringExtra());
    }
    
  }
  
  
}