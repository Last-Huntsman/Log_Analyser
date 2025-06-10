import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogReader {
    public List<File> listFilesForFolder(final File folder) {
        List<File> fileList = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
             if (fileEntry.isDirectory()) {
                 fileList.addAll(listFilesForFolder(fileEntry));
             } else {
                 fileList.add(fileEntry);
                 System.out.println(fileEntry.getName());
             }
        }
        return  fileList;
    }

}
