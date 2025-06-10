import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class LogReader {
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
             if (fileEntry.isDirectory()) {
                 listFilesForFolder(fileEntry);
             } else {
                 System.out.println(fileEntry.getName());
             }
         }
    }

}
