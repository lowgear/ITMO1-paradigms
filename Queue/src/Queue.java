import java.util.function.Predicate;
import java.util.function.Function;

/**
 * Created by aydar on 13.03.16.
 */
public interface Queue {
    //Invariant:
    //  * size >= 0;
    //  * (a1, a2, ... , asize)
    //  * ai != null

    //(a1, a2, ... , asize)
    //el != null
    void enqueue(Object el);
    //((a1, a2, ... , asize, el)

    //(a1, a2, ... , asize).size != 0
    Object element();
    //(a1, a2, ... , asize)
    //res = a1 != 0

    //(a1, a2, ... , asize).size != 0
    Object dequeue();
    //res = a1 != 0
    //(a2, a3, ... , asize)

    //(a1, a2, ... , asize)
    int size();
    //(a1, a2, ... , asize)
    //res = (a1, a2, ... , asize).size

    //(a1, a2, ... , asize)
    boolean isEmpty();
    //(a1, a2, ... , asize)
    //res = (a1, a2, ... , asize).size != 0

    //
    void clear();
    //(a1, a2, ... , asize).size == 0

    //(a1, a2, ... , asize)
    Object[] toArray();
    //(a1, a2, ... , asize)
    //res = (a1, a2, ... , asize) as array

    //(a1, a2, ... , asize)
    Queue filter(Predicate<Object> predicate);
    //(a1, a2, ... , asize)
    //res = //(ai1, ai2, ... , aisize1)
    //i1 < i2 < ... < isize1
    //there is q in (ij) <=> predicate(aq)

    //(a1, a2, ... , asize)
    Queue map(Function<Object, Object> function);
    //(a1, a2, ... , asize)
    //res = //(function(a1), function(a2), ... , function(asize))

    //(a1, a2, ... , asize)
    String toString();
    //(a1, a2, ... , asize)
    //res = "[" + a1 + a2 + ... + asize +"]"
}
