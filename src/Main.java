import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String InputDir = "LogsFile";
        String OutputDir = "";
        final File folder = new File(InputDir);
        List<File> fileList = new LogReader().listFilesForFolder(folder);

    }
}
