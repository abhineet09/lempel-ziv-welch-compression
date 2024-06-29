package andrew.cmu.edu.abhineec;

/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 *      Project 5
 * */

/******************************************************************************
 * SinglyLinkedList class is used to maintain a singly linked list with
 * a head pointer reference
 * an iterator pointer which is used to traverse through the list and
 * a countNodes variable that keeps track of total number of nodes in the list at any given point
 ******************************************************************************/

public class SinglyLinkedList {
    protected ObjectNode head;
    protected ObjectNode iterator;
    protected int countNodes;

    /**
     * Initialize an empty SinglyLinkedList
     * @postcondition
     *   An empty SinglyLinkedList object is initialized
     **/
    public SinglyLinkedList(){
        this.head = null;
        this.iterator = null;
        this.countNodes = 0;
    }

    /**
     * Reset the iterator pointer to refer head of the list
     * @postcondition
     *   Iterator now points to the head of the list
     *   For an empty list it would be null
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public void reset(){
        iterator =  head;
    }

    /**
     * Returns the data object for the ObjectNode being referred by the iterator at any given point
     * @postcondition
     *   Object data for current iterator reference has been sent
     *   For an empty list it returns a null object
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public Object next(){
        if(iterator==null)
            return null;
        Object obj =  iterator.getData();
        iterator = iterator.getLink();
        return obj;
    }

    /**
     * Returns true if the ObjectNode being referred by the iterator has a link that points to another ObjectNode
     * @postcondition
     *   Has returned true if there are elements remaining to be traversed
     *   For an empty list it returns a null object
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public boolean hasNext(){
        return iterator != null;
    }

    /**
     * Inserts a new ObjectNode at the end of the list
     * @postcondition
     *   A new ObjectNode has been added to the list
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public ObjectNode addAtEnd(Object c){
        if(this.head == null){
            head = new ObjectNode(c, null);
            this.countNodes++;
            return this.head;
        }
        ObjectNode localIterator = this.head;
        while(localIterator.getLink()!=null){
            localIterator = localIterator.getLink();
        }
        localIterator.setLink(new ObjectNode(c, null));
        this.countNodes++;
        return localIterator.getLink();
    }

    /**
     * Returns value of the countNodes representing the number of ObjectNodes in list at any  point in time
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public int countNodes(){
        return countNodes;
    }

    /**
     * Returns the object at the start of the list i.e.
     * at the head position
     * @postcondition
     *   head now points to its link
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public Object removeFromFront(){
        if(head==null)
            return null;
        Object returnObject = this.head.getData();
        this.head  = this.head.getLink();
        this.countNodes--;
        return returnObject;
    }
}
