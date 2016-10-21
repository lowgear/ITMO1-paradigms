/**
 * Created by aydar on 13.03.16.
 */
public class ArrayQueue extends AbstractQueue {
    private Object[] ar = new Object[4];
    private int front = 0;

    //(a1, a2, ... , asize)
    private void ensureFreeSpaceAndRatSize() {
        if (size == ar.length) {
            Object[] nar = new Object[ar.length * 2];
            System.arraycopy(ar, front, nar, 0, ar.length - front);
            System.arraycopy(ar, 0, nar, ar.length - front, front);
            front = 0;
            ar = nar;
            return;
        }
        if (size != 0 && size == ar.length / 4) {
            Object[] nar = new Object[ar.length / 2];
            System.arraycopy(ar, front, nar, 0, Math.min(ar.length - front, size));
            if (size - (ar.length - front) > 0) {
                System.arraycopy(ar, 0, nar, ar.length - front, size - (ar.length - front));
            }
            front = 0;
            ar = nar;
        }
    }
    //(a1, a2, ... , asize)
    //* size > ar.length / 4
    //* size < ar.length

    @Override
    public void preEnqueue(Object el) {
        ensureFreeSpaceAndRatSize();

        int pos = front + size;
        if (pos >= ar.length) {
            pos -= ar.length;
        }
        ar[pos] = el;
    }

    @Override
    public Object element() {
        return ar[front];
    }

    @Override
    public void postDequeue() {
        ar[front++] = null;
        if (front >= ar.length) {
            front -= ar.length;
        }
        ensureFreeSpaceAndRatSize();
    }

    @Override
    public void preClear() {
        ar = new Object[4];
        front = 0;
    }

    @Override
    protected AbstractQueue newInstance() {
        return new ArrayQueue();
    }
}
