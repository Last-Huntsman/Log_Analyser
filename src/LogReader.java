import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogReader {
    public List<File> listFilesRecursively(final File folder) {
        List<File> fileList = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
             if (fileEntry.isDirectory()) {
                 fileList.addAll(listFilesRecursively(fileEntry));
             } else {
                 fileList.add(fileEntry);
             }
        }
        return  fileList;
    }
}
