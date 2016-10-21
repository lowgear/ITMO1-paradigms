/**
 * Created by aydar on 28.02.16.
 */
public class ArrayQueueADT {
    private Object[] ar = new Object[1024];
    private int front = 0;
    private int size = 0;
    //Invariant:
    //  * size >= 0;
    //  * (a1, a2, ... , asize)

    //
    private static void ensureFreeSpaceAndRatSize(ArrayQueueADT q) {
        if (q.size == q.ar.length) {
            Object[] nar = new Object[q.ar.length * 2];
            System.arraycopy(q.ar, q.front, nar, 0, q.ar.length - q.front);
            System.arraycopy(q.ar, 0, nar, q.ar.length - q.front, q.front);
            /*for (int i = q.front, j = 0; i < q.ar.length; i++, j++) {
                nar[j] = q.ar[i];
            }
            for (int i = 0, j = q.ar.length - q.front; i < q.front; i++, j++) {
                nar[j] = q.ar[i];
            }*/
            q.front = 0;
            q.ar = nar;
            return;
        }
        if (q.size != 0 && q.size == q.ar.length / 4) {
            Object[] nar = new Object[q.ar.length / 2];
            System.arraycopy(q.ar, q.front, nar, 0, Math.min(q.ar.length - q.front, q.size));
            if (q.size - (q.ar.length - q.front) > 0) {
                System.arraycopy(q.ar, 0, nar, q.ar.length - q.front, q.size - (q.ar.length - q.front));
            }
            /*for (int i = q.front, j = 0; i < q.ar.length && i < q.front + q.size; i++, j++) {
                nar[j] = q.ar[i];
            }
            for (int i = 0, j = q.ar.length - q.front; i < q.size - (q.ar.length - q.front); i++, j++) {
                nar[j] = q.ar[i];
            }*/
            q.front = 0;
            q.ar = nar;
        }
    }
    //* q.size > q.ar.length / 4
    //* q.size < q.ar.length

    //q.(a1, a2, ... , asize)
    public static void enqueue(ArrayQueueADT q, Object el) {
        ensureFreeSpaceAndRatSize(q);

        int pos = q.front + q.size++;
        if (pos >= q.ar.length) {
            pos -= q.ar.length;
        }
        q.ar[pos] = el;
    }
    //q.(a1, a2, ... , asize, el)

    //q.(a1, a2, ... , asize).size != 0
    public static Object element(ArrayQueueADT q) {
        return q.ar[q.front];
    }
    //q.(a1, a2, ... , asize)
    //res = q.a1

    //q.(a1, a2, ... , asize).size != 0
    public static Object dequeue(ArrayQueueADT q) {
        q.size--;
        Object res = q.ar[q.front];
        q.ar[q.front++] = null;
        if (q.front >= q.ar.length) {
            q.front -= q.ar.length;
        }

        ensureFreeSpaceAndRatSize(q);

        return res;
    }
    //res = q.a1
    //q.(a2, a3, ... , asize)

    //
    public static int size(ArrayQueueADT q) {
        return q.size;
    }
    //res = q.(a1, a2, ... , asize).size

    //
    public static boolean isEmpty(ArrayQueueADT q) {
        return q.size == 0;
    }
    //res = q.(a1, a2, ... , asize).size != 0

    //
    public static void clear(ArrayQueueADT q) {
        q.ar = new Object[1024];
        q.front = 0;
        q.size = 0;
    }
    //q.(a1, a2, ... , asize).size == 0

    //
    public static Object[] toArray(ArrayQueueADT q) {
        Object[] res = new Object[q.size];
        for (int i = q.front, j = 0; i < q.ar.length && i < q.front + q.size; i++, j++) {
            res[j] = q.ar[i];
        }
        for (int i = 0, j = q.ar.length - q.front; i < q.size - (q.ar.length - q.front); i++, j++) {
            res[j] = q.ar[i];
        }

        return res;
    }
    //res = (a1, a2, ... , asize)
}
