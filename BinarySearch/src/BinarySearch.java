/**
 * Created by aydar on 17.02.16.
 */
public class BinarySearch {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] ar = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            ar[i] = Integer.parseInt(args[i + 1]);
        }

        System.out.println(binarySearchRec(ar, 0, ar.length, x));
        System.out.println(binarySearchItr(ar, 0, ar.length, x));
    }

    //Pre:
    //  * ar[] is sorted by not increasing
    //Post:
    //  * ar[] is sorted by not increasing
    //  * the return is the first elem in the interval [l, r) which is not greater then x
    public static int binarySearchRec(int[] ar, int l, int r, int x) {
        //Pre:
        //  * l and r are initialized
        if (l + 1 > r) {
            System.out.println("Something is wrong! binarySearchRec is aborted.");
        }
        //Post:
        //  * the interval [l, r) is not empty

        //Pre:
        //  * the interval [l, r) is not empty
        if (l + 1 == r) {
            if (ar[l] >= x) {
                return l;
            } else {
                return -1;
            }
        }
        //Post: (how to deal with return statements???)
        //  * the interval [l, r) consists more then one element

        //Pre:
        //  * the interval [l, r) consists more then one element
        //  * ar[] is sorted by not increasing
        int m = (l + r) / 2;
        if (ar[m] < x) {
            //Pre:
            //  * ar[] is sorted by not increasing
            //  * the first elem in the interval [l, r) which is not greater then x is in the interval [l, m)
            return binarySearchRec(ar, l, m, x);
            //Post:
            //  * ar[] is sorted by not increasing,
            //  * the return is the first elem in the interval [l, r) which is not greater then x
        } else {
            //Pre:
            //  * ar[] is sorted by not increasing
            //  * the first elem in the interval [l, r) which is not greater then x is in the interval [m, r)
            return binarySearchRec(ar, m, r, x);
            //Post:
            //  * ar[] is sorted by not increasing,
            //  * the return is the first elem in the interval [l, r) which is not greater then x
        }
        //Post:
        //  * ar[] is sorted by not increasing,
        //  * the return is the first elem in the interval [l, r) which is not greater then x
    }

    //Pre:
    //  * ar[] is sorted by not increasing
    //Post:
    //  * ar[] is sorted by not increasing
    //  * the return is the first elem in the interval [l, r) which is not greater then x
    public static int binarySearchItr(int[] ar, int l, int r, int x) {
        //Pre:
        //  * the interval [l, r) is sorted by not increasing
        while (l + 1 < r) {
            int m = (l + r) / 2;
            if (ar[m] < x) {
                //Pre:
                //  * the first elem in the interval [l, r)
                //    which is not greater then x is in the interval [l, m)
                r = m;
                //Post:
                //  * the first elem in the old interval [l, r) which is not greater then x
                //    is in the new one and the new one is smaller
            } else {
                //Pre:
                //  * the first elem in the interval [l, r)
                //    which is not greater then x is in the interval [m, r)
                l = m;
                //Post:
                //  * the first elem in the old interval [l, r) which is not greater then x
                //    is in the new one and the new one is smaller
            }
        }
        //Post:
        //  * new interval [l, r) is a subinterval of the old one
        //    and consists the first elem in the old interval [l, r)
        //    which is not greater then x or -1 if it exists


        //Pre:
        //  * the interval [l, r) is one element long
        if (l < r || ar[l] >= x) {
            return l;
        } else {
            return -1;
        }
        //Post:
        //  * the return is the first elem in the interval [l, r)
        //    which is not greater then x or -1 if doesn't exist
    }
}
