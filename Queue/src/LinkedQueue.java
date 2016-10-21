/**
 * Created by aydar on 13.03.16.
 */
public class LinkedQueue extends AbstractQueue {
    Node front = null;
    Node back = null;

    class Node {
        public Object value;
        public Node next;

        Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void preEnqueue(Object el) {
        if (front == null) {
            front = back = new Node(el, null);
        } else {
            back.next = new Node(el, null);
            back = back.next;
        }
    }

    @Override
    public Object element() {
        return front.value;
    }

    @Override
    public void postDequeue() {
        front = front.next;
    }

    @Override
    public void preClear() {
        front = back = null;
    }

    @Override
    protected AbstractQueue newInstance() {
        return new LinkedQueue();
    }
}
