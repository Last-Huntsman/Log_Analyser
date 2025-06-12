import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogController {


    public static Map<String, List<Transaction>> createUserTransactions(List<File> fileList) {
        Map<String, List<Transaction>> userTransactionMap = new HashMap<>();

        for (File file : fileList) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Transaction transaction = parseTransaction(line);
                    addTransaction(userTransactionMap, transaction);

                    if (transaction.getOperationType() == OperationType.TRANSFERRED) {
                        Transaction received = createReceivedTransaction(transaction);
                        addTransaction(userTransactionMap, received);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка чтения файла: " + file.getName(), e);
            }
        }

        return userTransactionMap;
    }


    private static Transaction parseTransaction(String line) {
        Transaction transaction = new Transaction(line);


        Pattern pattern = Pattern.compile("\\[(.*?)\\] (\\S+) (.*)");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }

        String timestamp = matcher.group(1);
        String user = matcher.group(2);
        String operationLine = matcher.group(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);

        transaction.setLocalDateTime(localDateTime);
        transaction.setUser(user);

        parseOperation(operationLine, transaction);

        return transaction;
    }


    private static void parseOperation(String operationLine, Transaction transaction) {
        String[] parts = operationLine.split(" ");
        switch (parts[0]) {
            case "balance":
                transaction.setOperationType(OperationType.BALANCE_INQUIRY);
                transaction.setAmount(new BigDecimal(parts[2]));
                break;
            case "transferred":
                transaction.setOperationType(OperationType.TRANSFERRED);
                transaction.setAmount(new BigDecimal(parts[1]));
                transaction.setUserTarget(parts[3]);
                break;
            case "withdrew":
                transaction.setOperationType(OperationType.WITHDREW);
                transaction.setAmount(new BigDecimal(parts[1]));
                break;
            default:
                throw new IllegalArgumentException("Неизвестная операция: " + parts[0]);
        }
    }


    private static Transaction createReceivedTransaction(Transaction original) {
        Transaction received = new Transaction("");

        received.setLocalDateTime(original.getLocalDateTime());
        received.setUser(original.getUserTarget());
        received.setUserTarget(original.getUser());
        received.setAmount(original.getAmount());
        received.setOperationType(OperationType.RECEIVED);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String logLine = String.format("[%s] %s recived %s from %s",
                received.getLocalDateTime().format(formatter),
                received.getUser(),
                received.getAmount(),
                received.getUserTarget());

        received.setString(logLine);

        return received;
    }


    private static void addTransaction(Map<String, List<Transaction>> map, Transaction transaction) {
        map.computeIfAbsent(transaction.getUser(), k -> new ArrayList<>()).add(transaction);
    }
}