import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // Метод возвращает объект Document, который содержит HTML-код загруженной страницы
    private static Document getPage() throws IOException {
        String url = "http://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    // Метод для того, что бы из строки достать только дату
    // С помощью регулярного выражения
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract data from string!");
    }

    // Метод нужен для вывода на экран значений из таблицы с прогнозом погоды на 4 промежутка дня.
    // Возвращает количество итераций (строк), которые были выведены на экран.
    private static int printFourValues(Elements values, int index) {
        int iterationCount = 4;
        if(index == 0) {
           Element valueLn = values.get(3);
           boolean isMorning = valueLn.text().contains("День");
           if(isMorning) {
               iterationCount = 2;
           }


           }

            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "      ");
                }
                System.out.println();
        }
        return iterationCount;
    }
    public static void main(String[] args) throws Exception {
        // Получаю HTML-страницу с помощью метода getPage().
        Document page = getPage();
        // Нахожу таблицу с прогнозом погоды, использую селектор "table[class=wt]".
        Element tableWth = page.select("table[class=wt]").first();

        // Получаю список элементов th с классом "wth" и "top", в них информация о датах и погоде по дням.
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        // В этом цикле делаю следущее:
        // Использую метод getDateFromString() для преобразования даты в нужный формат.
        // Затем вызываю метод printFourValues(), который выводит на экран.
        // Обновляю значение переменной index.
        for (Element name : names) {


            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "             Явления                  Температура   Давл   Влажность     Ветер");
            int iterationCount = printFourValues(values, index);
            index = index + iterationCount;

        }

    }

}
