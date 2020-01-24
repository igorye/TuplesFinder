package task;

import java.util.*;

public class TuplesFinder {

    public static void main(String[] args) {
        try {
            Map<String, Integer> params = parseArgs(args);
            if (args.length == 0 || params.containsKey("-h")) {
                printUsage();
                return;
            }
            final int LENGTH = params.getOrDefault("-l", 10);
            final int UPPER_BOUND = params.getOrDefault("-m", 20);
            final int SUM_OF_ELEMENTS = params.getOrDefault("-s", 100);
            final boolean containsNegativeValues = params.containsKey("-n");
            int[] first = generateArray(LENGTH, UPPER_BOUND, containsNegativeValues);
            System.out.printf("array#1: %s%n", Arrays.toString(first));
            int[] second = generateArray(LENGTH, UPPER_BOUND, containsNegativeValues);
            System.out.printf("array#2: %s%n", Arrays.toString(second));
            printTuplesWithSumForArrays(SUM_OF_ELEMENTS, first, second);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static Map<String, Integer> parseArgs(String[] args) {
        Map<String, Integer> mArgs = new HashMap<>();
        String valueForKey = "";
        for (String arg : args) {
            if (arg.matches("-\\D")) {
                valueForKey = arg;
                if (!valueForKey.matches("-[lhmns]")) {
                    throw new IllegalArgumentException("Invalid param: " + valueForKey);
                }
                mArgs.put(valueForKey, 0);
            } else {
                if (valueForKey.isEmpty()) throw new IllegalArgumentException("Value without a param: " + arg);
                mArgs.put(valueForKey, Integer.parseInt(arg));
                valueForKey = "";
            }
        }
        return mArgs;
    }

    private static void printUsage() {
        System.out.println("Usage: TuplesFinder [param] [value] ...");
        System.out.println("\t -h - this help");
        System.out.println("\t -l - length of arrays");
        System.out.println("\t -m - upper bound of a value");
        System.out.println("\t -s - sum of a pair");
        System.out.println("\t -n - generate negative values");
    }

    private static int[] generateArray( int length, int upperBound, boolean allowNegativeValues ) {
        if (length <= 0) throw new IllegalArgumentException("Length should be grater than 0!");
        if (upperBound <= 0) throw new IllegalArgumentException("Upper bound should be grater than 0!");
        final int[] ints = new int[length];
        final Random random = new Random(System.nanoTime());
        for(int i = 0; i < ints.length; i++ ) {
            final int sign = (allowNegativeValues && random.nextInt(upperBound + 1) % 2 == 0) ? -1 : 1;
            ints[i] = random.nextInt(upperBound + 1) * sign;
        }
        return ints;
    }

    private static void printTuplesWithSumForArrays( int sum_of_elements, int[] first, int[] second ) {
        if (first.length != second.length) {
            String msg = String.format(
                    "Matched arrays should be of the same length but first.length = %d and second.length = %d",
                    first.length,
                    second.length
            );
            throw new IllegalArgumentException(msg);
        }
        Map<Integer, List<Integer>> secondIndexes = new HashMap<>();
        for(int i = 0; i < second.length; i++) {
            List<Integer> bucket = secondIndexes.computeIfAbsent(second[i], index -> new ArrayList<>());
            bucket.add(i);
        }
        int[] copyOfSecond = Arrays.copyOf(second, second.length);
        Arrays.sort(copyOfSecond);
        for(int i = 0; i < first.length; i++) {
            int indexOfTarget = Arrays.binarySearch(copyOfSecond, sum_of_elements - first[i]);
            if (indexOfTarget >= 0) {
                final int firstIndex = i;
                final int secondInTuple = copyOfSecond[indexOfTarget];
                secondIndexes.getOrDefault(secondInTuple, Collections.emptyList())
                             .forEach(secondIndex -> System.out.printf("(first[%d] = %d) + (second[%d] = %d) = %d%n",
                                                                       firstIndex,
                                                                       first[firstIndex],
                                                                       secondIndex,
                                                                       second[secondIndex],
                                                                       sum_of_elements
                             ));
            }
        }
    }

}