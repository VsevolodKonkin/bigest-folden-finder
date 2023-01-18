import javax.security.auth.login.Configuration;
import java.io.File;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ParametersBag bag = new ParametersBag(args);

        String folderPath = bag.getPath();
        long sizeLimit = bag.getLimit();
        File file = new File(folderPath);
        Node root = new Node(file, sizeLimit);
        long start = System.currentTimeMillis();

        FolderSizeCalculator calculator = new FolderSizeCalculator(root);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(calculator);
        System.out.println(root);

        long duration = (System.currentTimeMillis() - start);
        System.out.println(duration + " ms");
    }
    public static long getFolderSize(File folder) {
        if (folder.isFile()) {
            return folder.length();
        }
        File[] files = folder.listFiles();
        long sum = 0;
        for (File file : files) {
            sum += getFolderSize(file);
        }
        return sum;
    }
}