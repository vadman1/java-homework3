import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Companies companies = null;
        try(FileReader reader = new FileReader("src\\main\\java\\test.json")){

            companies = gson.fromJson(reader, Companies.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Company> companyList = companies.companies;

        // вывести все имеющиеся комании в формате "Краткое название" - "Дата основания 17/01/98"
        companyList.forEach(company -> {
            String date = LocalDate.parse(
                    company.founded,
                    DateTimeFormatter.ofPattern("d.MM.yyyy")
            ).format(
                    DateTimeFormatter.ofPattern("d/MM/yy")
            );
            System.out.println(company.name + " - Дата основания " +
                    date);
        });

        // вывести все ценные бумаги (их код, дату истечения и полное название организации-владельца), которые просрочены на текущий день, а также посчитать суммарное число всех таких бумаг
        companyList.
                forEach(company -> company.securities.stream().filter(securities -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate localDate = LocalDate.parse(securities.date, formatter);
                    return localDate.isBefore(LocalDate.now());
                }).forEach(securities -> System.out.println("Код: " + securities.code +
                        " Дата истечения: " + securities.date +
                        " Название организации-владельца: " + securities.name)));

        // суммарное число просроченных ценных бумаг
        long summary = companyList.stream().mapToLong(company -> company.securities.stream().filter(securities -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(securities.date, formatter);
            return localDate.isBefore(LocalDate.now());
        }).count()).sum();

        System.out.println(summary);

        // на запрос пользователя в виде даты вывести название и дату создания всех организаций, основанных после введенной даты
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String date = reader.readLine();

        LocalDate inputDate = parseStringToLocalDate(date);

        companyList.stream().filter(company -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
            LocalDate localDate = LocalDate.parse(company.founded, formatter);
            return localDate.isAfter(inputDate);
        }).forEach(company -> System.out.println(company.name + " " + company.founded));


        // на запрос пользователя в виде кода валюты (EU, USD, RUB и пр.) выводить id и коды ценных бумаг
        String currencyCode = reader.readLine();

        companyList.forEach(company -> company.securities.stream()
                .filter(securities -> securities.currency.contains(currencyCode))
                .forEach(securities ->
                        System.out.println(company.id + " "+ securities.code + " " + securities.currency)));

    }

    private static DateTimeFormatter[] parseFormatters = Stream.of("d.MM.yyyy", "d.MM.yy", "d/MM/yyyy", "d/MM/yy")
            .map(DateTimeFormatter::ofPattern)
            .toArray(DateTimeFormatter[]::new);

    public static LocalDate parseStringToLocalDate(String input) {
        for (DateTimeFormatter formatter : parseFormatters) {
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException dtpe) {
                // ignore, try next format
            }
        }
        throw new IllegalArgumentException("Не получается обработать дату " + input);
    }
}