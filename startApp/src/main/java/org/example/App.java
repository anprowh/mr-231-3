package org.example;

import org.example.searadar.mr231.convert.Mr231Converter;
import org.example.searadar.mr231.station.Mr231StationType;
import org.example.searadar.mr2313.convert.Mr2313Converter;
import org.example.searadar.mr2313.station.Mr2313StationType;
import ru.oogis.searadar.api.message.SearadarStationMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Практическое задание направлено на проверку умения создавать функциональные модули и работать с технической
 * документацией.
 * Задача написать парсер сообщений для протокола МР-231-3 на основе парсера МР-231.
 * Приложение к заданию файлы:
 * - Протокол МР-231.docx
 * - Протокол МР-231-3.docx
 * <p>
 * Требования:
 * 1. Перенести контрольный пример из "Протокола МР-231.docx" в метод main, по образцу в методе main;
 * 2. Проверить правильность работы конвертера путём вывода контрольного примера в консоль, по образцу в методе main;
 * 3. Создать модуль с именем mr-231-3 и добавить его в данный проект <module>../mr-231-3</module> и реализовать его
 * функционал в соответствии с "Протоколом МР-231-3.docx" на подобии модуля mr-231;
 * 4. Создать для модуля контрольный пример и проверить правильность работы конвертера путём вывода контрольного
 * примера в консоль
 *
 * <p>
 *     Примечание:
 *     1. Данные в пакете ru.oogis не изменять!!!
 *     2. Весь код должен быть покрыт JavaDoc
 */

public class App {
    public static void main(String[] args) {
        // Контрольный пример для МР-231
        List<String> mr231Messages = new ArrayList<>();
        mr231Messages.add("$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,,А*42");
        mr231Messages.add("$RATTM,23,13.88,137.2,T,63.8,094.3,T,9.2,79.4,N,b,T,,,А*42");
        mr231Messages.add("$RATTM,54,19.16,139.7,T,07.4,084.1,T,2.1,-95.8,N,d,L,,,А*7f");
        mr231Messages.add("$RATTM,46,05.14,123.4,T,52.8,139.5,T,6.3,-96.6,N,b,L,,,А*7f");
        mr231Messages.add("$RATTM,28,28.99,160.0,T,88.4,064.0,T,4.7,77.7,N,b,L,,,А*59");
        mr231Messages.add("$RAVHW,115.6,T,,,46.0,N,,*71");
        mr231Messages.add("$RAVHW,356.7,T,,,50.4,N,,*76");
        mr231Messages.add("$RARSD,14.0,0.0,96.9,306.4,,,,,97.7,11.6,0.3,K,N,S*20");
        mr231Messages.add("$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33");
        mr231Messages.add("$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28");

        // Проверка работы конвертера
        System.out.println("MR-231");
        Mr231StationType mr231 = new Mr231StationType();
        Mr231Converter converter = mr231.createConverter();
        List<SearadarStationMessage> searadarMessages = new ArrayList<>();
        for (String mr231Message : mr231Messages) {
            searadarMessages.addAll(converter.convert(mr231Message));
        }

        searadarMessages.forEach(System.out::println);

        System.out.println("---------------------------------------------------");


        // Контрольный пример для МР-231-3
        List<String> mr2313Messages = new ArrayList<>();
        mr2313Messages.add("$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,,А*42");
        mr2313Messages.add("$RATTM,23,13.88,137.2,T,63.8,094.3,T,9.2,79.4,N,b,T,,,А*42");
        mr2313Messages.add("$RATTM,54,19.16,139.7,T,07.4,084.1,T,2.1,-95.8,N,d,L,,,А*7f");
        mr2313Messages.add("$RATTM,46,05.14,123.4,T,52.8,139.5,T,6.3,-96.6,N,b,L,,,А*7f");
        mr2313Messages.add("$RATTM,28,28.99,160.0,T,88.4,064.0,T,4.7,77.7,N,b,L,,,А*59");
        mr2313Messages.add("$RARSD,14.0,0.0,96.9,306.4,,,,,97.7,11.6,0.3,K,N,S*20");
        mr2313Messages.add("$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33");
        mr2313Messages.add("$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28");

        // Проверка работы конвертера
        System.out.println("MR-231-3");

        Mr2313StationType mr2313 = new Mr2313StationType();
        Mr2313Converter converter3 = mr2313.createConverter();
        List<SearadarStationMessage> searadarMessages3 = new ArrayList<>();
        for (String mr231Message : mr231Messages) {
            searadarMessages3.addAll(converter3.convert(mr231Message));
        }

        searadarMessages3.forEach(System.out::println);
    }
}
