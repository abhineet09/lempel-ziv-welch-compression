package andrew.cmu.edu.abhineec;

/******************************************************************************
 * Node class is used as a POJO to store key, value pairs in SinglyLinkedList
 * which internally casts it to Main's ObjectNode class
 ******************************************************************************/

public class Node {
    private String key;
    private Integer value;

    public Node(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
