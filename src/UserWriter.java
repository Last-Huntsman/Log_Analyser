import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserWriter {
    private String outputDir;

    public UserWriter(String outputDir) throws IOException {
        this.outputDir = outputDir;
        Files.createDirectories(Paths.get(outputDir)); // Создаём чистую папку
    }

    public void writeUsersLog(Map<String, List<Transaction>> mapTransaction) {
        for (String key : mapTransaction.keySet()) {
            writeUserLog(key, mapTransaction.get(key));
        }
    }

    private void writeUserLog(String username, List<Transaction> logs) {
        Collections.sort(logs); // Сортировка по времени
        String filename = outputDir + "/" + username + ".log";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Transaction log : logs) {
                writer.println(log.getString());
            }
            writer.println(writeTotalAmount(username,logs));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal calculateTotalAmount(List<Transaction> logs) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Transaction log : logs) {
            if (log.getOperationType() == OperationType.BALANCE_INQUIRY) {
                totalAmount = log.getAmount();
            } else if (log.getOperationType() == OperationType.RECEIVED) {
                totalAmount = totalAmount.add(log.getAmount());
            } else {
                totalAmount = totalAmount.subtract(log.getAmount());
            }
        }
        return totalAmount;
    }

    private String writeTotalAmount(String user,List<Transaction> logs) {
        BigDecimal totalAmount = calculateTotalAmount(logs);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        [2025-05-10 11:00:03] user001 final balance 1196.92
        return "["+ LocalDateTime.now().format(formatter) +"] "+user+" final balance "+totalAmount;}

}

