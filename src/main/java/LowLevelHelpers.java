/**
 * A Collection of low level function, that need to be fast.
 * Typically kotlin is not great for that while java is a bit more decent.
 */
public class LowLevelHelpers {

    /** Implements a permutation
     * If result buffer is provided, the result is stored in it,
     * which is not very java like but allows to save a precious time of allocation.
     * Otherwise we create a new array. */
     public static int[] permut(int[] original, int[] permutation, int[] result){
         int[] returnedResult = result != null ? result : new int[original.length];
         for (int i=0; i< permutation.length; i++){
             returnedResult[i] = original[permutation[i]];
         }
         return returnedResult;
    }

    /** Will return the number of difference between the actual array
     *  and a source of truth. If the groundtruth feature a -1, we
     *  count no difference for this position (it's kindof a joker) */
    public static int num_differences(int[] actual, int[] pattern) {
        int numDifferences = 0;
        for (int i=0; i< actual.length; i++){
            if (pattern[i] != -1 && actual[i] != pattern[i]) {
                numDifferences++;
            }
        }
        return numDifferences;
    }

    /**
     * Same as above, except it early returns that as soon as there is one difference.
     * */
    public static boolean matches(int[] actual, int[] pattern) {
        for (int i=0; i< actual.length; i++){
            if (pattern[i] != -1 && actual[i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

}
