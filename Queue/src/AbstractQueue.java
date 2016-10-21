import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by aydar on 13.03.16.
 */
public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    @Override
    public void enqueue(Object el) {
        preEnqueue(el);
        size++;
    }

    protected abstract void preEnqueue(Object el);

    @Override
    public Object dequeue() {
        Object res = element();
        size--;
        postDequeue();
        return res;
    }

    protected abstract void postDequeue();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        preClear();
        size = 0;
    }

    protected abstract void preClear();

    @Override
    public AbstractQueue filter(Predicate<Object> predicate) {
        AbstractQueue res = this.newInstance();
        for (Object it : toArray()) {
            if (predicate.test(it)) {
                res.enqueue(it);
            }
        }
        return res;
    }

    protected abstract AbstractQueue newInstance();

    @Override
    public AbstractQueue map(Function<Object, Object> function) {
        AbstractQueue res = this.newInstance();
        for (Object it : toArray()) {
            res.enqueue(function.apply(it));
        }
        return res;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = element();
            enqueue(dequeue());
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object it : toArray()) {
            sb.append(it);
            sb.append(", ");
        }
        String res = sb.toString();
        res = res.substring(0, res.length() - 2) + "]";
        return res;
    }
}
