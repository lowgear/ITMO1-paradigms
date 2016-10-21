/**
 * Created by aydar on 26.02.16.
 */
public class ArrayQueueModule {
    private static Object[] ar = new Object[1024];
    private static int front = 0;
    private static int size = 0;
    //Invariant:
    //  * size >= 0;
    //  * (a1, a2, ... , asize)

    //
    private static void ensureFreeSpaceAndRatSize() {
        if (size == ar.length) {
            Object[] nar = new Object[ar.length * 2];
            System.arraycopy(ar, front, nar, 0, ar.length - front);
            System.arraycopy(ar, 0, nar, ar.length - front, front);
            /*for (int i = front, j = 0; i < ar.length; i++, j++) {
                nar[j] = ar[i];
            }
            for (int i = 0, j = ar.length - front; i < front; i++, j++) {
                nar[j] = ar[i];
            }*/
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
            /*for (int i = front, j = 0; i < ar.length && i < front + size; i++, j++) {
                nar[j] = ar[i];
            }
            for (int i = 0, j = ar.length - front; i < size - (ar.length - front); i++, j++) {
                nar[j] = ar[i];
            }*/
            front = 0;
            ar = nar;
        }
    }
    //* size > ar.length / 4
    //* size < ar.length

    //(a1, a2, ... , asize)
    public static void enqueue(Object el) {
        ensureFreeSpaceAndRatSize();

        int pos = front + size++;
        if (pos >= ar.length) {
            pos -= ar.length;
        }
        ar[pos] = el;
    }
    //((a1, a2, ... , asize, el)

    //(a1, a2, ... , asize).size != 0
    public static Object element() {
        return ar[front];
    }
    //(a1, a2, ... , asize)
    //res = a1

    //(a1, a2, ... , asize).size != 0
    public static Object dequeue() {
        size--;
        Object res = ar[front];
        ar[front++] = null;
        if (front >= ar.length) {
            front -= ar.length;
        }

        ensureFreeSpaceAndRatSize();

        return res;
    }
    //res = a1
    //(a2, a3, ... , asize)

    //
    public static int size() {
        return size;
    }
    //res = (a1, a2, ... , asize).size

    //
    public static boolean isEmpty() {
        return size == 0;
    }
    //res = (a1, a2, ... , asize).size != 0

    //
    public static void clear() {
        ar = new Object[1024];
        front = 0;
        size = 0;
    }
    //(a1, a2, ... , asize).size == 0

    //
    public static Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = front, j = 0; i < ar.length && i < front + size; i++, j++) {
            res[j] = ar[i];
        }
        for (int i = 0, j = ar.length - front; i < size - (ar.length - front); i++, j++) {
            res[j] = ar[i];
        }
        return res;
    }
    //res = (a1, a2, ... , asize)
}
