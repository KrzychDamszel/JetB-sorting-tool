package sorting;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(final String[] args) {

        List<String> argsList = new ArrayList<>(Arrays.asList(args));

        if (!start(argsList)) {
            return;
        }

        Scanner scanner = new Scanner(System.in);

        try {
            if (argsList.contains("-inputFile")) {
                int idx = argsList.indexOf("-inputFile");
                scanner = new Scanner(Paths.get(argsList.get(idx + 1)));
            }

            boolean writeToFile = false;
            PrintWriter out = null;
            if (argsList.contains("-outputFile")) {
                int idx = argsList.indexOf("-outputFile");
                File file = new File(argsList.get(idx + 1));
                out = new PrintWriter(file);
                writeToFile = true;
            }

            if (argsList.contains("long")) {
                Map<Long, Integer> longToCount = longDate(scanner);
                if (argsList.contains("byCount")) {
                    byCount(longToCount, writeToFile, out);
                } else {
                    naturally(longToCount, writeToFile, out);
                }
            } else if (argsList.contains("line")) {
                Map<String, Integer> lineToCount = lineDate(scanner);
                if (argsList.contains("byCount")) {
                    byCount(lineToCount, writeToFile, out);
                } else {
                    naturallyLine(lineToCount, writeToFile, out);
                }
            } else {
                Map<String, Integer> wordToCount = wordDate(scanner);
                if (argsList.contains("byCount")) {
                    byCount(wordToCount, writeToFile, out);
                } else {
                    naturally(wordToCount, writeToFile, out);
                }
            }

            if (writeToFile) {
                out.close();
            }
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        scanner.close();
    }

    private static boolean start(List<String> argsList) {
        List<String> arguments = new ArrayList<>(argsList);
        List<String> correctList = new ArrayList<>(List
                .of("-sortingType", "natural", "byCount", "-dataType", "long", "line", "word"));
        boolean sort = false;
        boolean data = false;
        if (argsList.size() == 0) {
            return true;
        }
        if (argsList.contains("-sortingType") && argsList.size() == 1) {
            System.out.println("No sorting type defined!");
        } else {
            sort = true;
        }
        if (argsList.contains("-dataType") && argsList.size() == 1) {
            System.out.println("No data type defined!");
        } else {
            data = true;
        }
        delArgs(arguments, "-inputFile");
        delArgs(arguments, "-outputFile");
        arguments.removeAll(correctList);
        if (arguments.size() > 0) {
            for (String el : arguments) {
                System.out.printf("\"%s\" isn't a valid parameter. It's skipped.\n", el);
            }
        }
        return sort && data;
    }

    private static Map<Long, Integer> longDate(Scanner scanner) {
        List<Long> arrLong = new ArrayList<>();
        List<String> errArray = new ArrayList<>();
        while (scanner.hasNext()) {
            String nextText = scanner.next();
            try {
                Long number = Long.parseLong(nextText);
                arrLong.add(number);
            } catch (NumberFormatException e) {
                errArray.add(nextText);
            }
        }
        if (errArray.size() > 0) {
            for (String el : errArray) {
                System.out.printf("\"%s\" isn't a long. It's skipped.\n", el);
            }
        }
        Map<Long, Integer> dataMap = new TreeMap<>();
        for (Long el : arrLong) {
            dataMap.put(el, dataMap.getOrDefault(el, 0) + 1);
        }
        return dataMap;
    }

    private static Map<String, Integer> lineDate(Scanner scanner) {
        List<String> arrString = new ArrayList<>();
        while (scanner.hasNextLine()) {
            arrString.add(scanner.nextLine());
        }
        Map<String, Integer> dataMap = new TreeMap<>();
        for (String el : arrString) {
            dataMap.put(el, dataMap.getOrDefault(el, 0) + 1);
        }
        return dataMap;
    }

    private static Map<String, Integer> wordDate(Scanner scanner) {
        List<String> arrString = new ArrayList<>();
        while (scanner.hasNext()) {
            arrString.add(scanner.next());
        }
        Map<String, Integer> dataMap = new TreeMap<>();
        for (String el : arrString) {
            dataMap.put(el, dataMap.getOrDefault(el, 0) + 1);
        }
        return dataMap;
    }

    private static <K> void byCount(Map<K, Integer> longToCount, boolean writeToFile, PrintWriter out) {
        int valueCount = mapSize(longToCount);
        if (writeToFile) {
            out.println("Total numbers: " + valueCount + ".");
        } else {
            System.out.println("Total numbers: " + valueCount + ".");
        }
        List<Map.Entry<K, Integer>> list = new ArrayList<>(longToCount.entrySet());
        list.sort(Map.Entry.comparingByValue());
        for (Map.Entry<K, Integer> el : list) {
            if (writeToFile) {
                out.println(el.getKey() + ": " + el.getValue() + " time(s), " + String.format("%.0f", (double) el.getValue() / valueCount * 100) + "%");
            } else {
                System.out.println(el.getKey() + ": " + el.getValue() + " time(s), " + String.format("%.0f", (double) el.getValue() / valueCount * 100) + "%");
            }
        }
    }

    private static <K> void naturally(Map<K, Integer> longToCount, boolean writeToFile, PrintWriter out) {
        if (writeToFile) {
            out.println("Total numbers: " + mapSize(longToCount) + ".");
        } else {
            System.out.println("Total numbers: " + mapSize(longToCount) + ".");
        }
        for (K el : longToCount.keySet()) {
            for (int i = 0; i < longToCount.get(el); i++) {
                if (writeToFile) {
                    out.print(el + " ");
                } else {
                    System.out.print(el + " ");
                }
            }
        }
        System.out.println();
    }

    private static void naturallyLine(Map<String, Integer> longToCount, boolean writeToFile, PrintWriter out) {
        if (writeToFile) {
            out.println("Total numbers: " + mapSize(longToCount));
        } else {
            System.out.println("Total numbers: " + mapSize(longToCount));
        }
        for (String el : longToCount.keySet()) {
            for (int i = 0; i < longToCount.get(el); i++) {
                if (writeToFile) {
                    out.println(el);
                } else {
                    System.out.println(el);
                }
            }
        }
        System.out.println();
    }

    private static <K> int mapSize(Map<K, Integer> numbersItem) {
        int valueCount = 0;
        for (Integer el : numbersItem.values()) {
            valueCount += el;
        }
        return valueCount;
    }

    private static void delArgs(List<String> list, String text) {
        if (list.contains(text)) {
            int idx = list.indexOf(text);
            list.remove(idx);
            list.remove(idx);
        }

    }

}
