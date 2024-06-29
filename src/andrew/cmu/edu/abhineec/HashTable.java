package andrew.cmu.edu.abhineec;

/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 *      Project 5
 * */

import java.util.Arrays;

/******************************************************************************
 * HashTable class represents an implementation of a hashtable using
 * chained-hashing method where
 * entries represents an array of SinglyLinkedList where colliding entries are
 * kept in their corresponding linked list
 ******************************************************************************/
public class HashTable {

    SinglyLinkedList[] entries;

    /**
     * Initialize an empty HashTable of size 127
     * @postcondition
     *   An empty HashTable object is initialized
     **/
    public HashTable(){
        this.entries = new SinglyLinkedList[127];
        //initialize each index in array as a SinglyLinkedList
        Arrays.fill(this.entries, new SinglyLinkedList());
    }

    /**
     * Checks if hashtable contains a key stored
     * @param key
     *      key to search for
     * @postcondition
     *   Returns true if key is present in hashtable
     *   or false if not present
     **/
    public boolean containsKey(String key){
        int hashValue = hashCode(key);
        SinglyLinkedList listAtIndex = entries[hashValue];
        listAtIndex.reset();
        while(listAtIndex.hasNext()){
            Node elementInList = (Node) entries[hashValue].next();
            if(elementInList.getKey().equals(key))
                return true;
        }
        return false;
    }

    /**
     * Inserts a new entry or updates an existing entry in hashtable
     * @param key
     *      key to insert
     * @param value
     *      corresponding value to key
     * @postcondition
     *   Key, Value is inserted or updated in the hashtable
     **/
    public void put(String key, Integer value){
        int hashValue = hashCode(key);
        //update case
        if(containsKey(key)){
            while(entries[hashValue].hasNext()){
                Node elementInList = (Node) entries[hashValue].next();
                if(elementInList.getKey().equals(key))
                    elementInList.setValue(value);
            }
        }
        //new insert
        else{
            Node node = new Node(key, value);
            entries[hashValue].addAtEnd(node);
        }
        return;
    }

    /**
     * Returns corresponding value of a key if found in the table
     * @param key
     *      key to search for
     * @postcondition
     *   Value corresponding to Key is returned if found else null is returned
     **/
    public Integer get(String key){
        int hashValue = hashCode(key);
        SinglyLinkedList listAtIndex = entries[hashValue];
        listAtIndex.reset();
        while(listAtIndex.hasNext()){
            Node elementInList = (Node) entries[hashValue].next();
            if(elementInList.getKey().equals(key))
                return elementInList.getValue();
        }
        return null;
    }

    /**
     * Helper function that hashes a string to a sum of the
     * ASCII values of each character modulo 127
     * @param s
     *      string to hash
     * @postcondition
     *   Hashed value (between 0 and 126) is returned
     **/
    private int hashCode(String s) {
        int hashCode = 0;
        for(int i=0; i<s.length(); i++){
            hashCode += (int) s.charAt(i);
        }
        return hashCode%127;
    }
}
