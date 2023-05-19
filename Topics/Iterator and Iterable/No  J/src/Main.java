import java.util.*;

public class Main {

    public static void processIterator(String[] array) {
        List<String> stringList = new ArrayList<>(Arrays.asList(array));

        ListIterator<String> iterator = stringList.listIterator();
        while (iterator.hasNext()) {
            String current = iterator.next();
            if (!current.startsWith("J")) {
                iterator.remove();
            }
        }

        for (int i = 0; i < stringList.size(); i++) {
            String current = stringList.get(i);
            if (current.startsWith("J")) {
                stringList.set(i, current.substring(1));
            }
        }

        for (int i = stringList.size() - 1; i >= 0; i--) {
            System.out.println(stringList.get(i));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processIterator(scanner.nextLine().split(" "));
    }
}
