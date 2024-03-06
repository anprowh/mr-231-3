package org.example.searadar.mr231.convert;

import org.apache.camel.Exchange;
import ru.oogis.searadar.api.convert.SearadarExchangeConverter;
import ru.oogis.searadar.api.message.*;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Конвертер сообщений МР-231
 */
public class Mr231Converter implements SearadarExchangeConverter {

    // Массив допустимых значений масштаба дальности
    private static final Double[] DISTANCE_SCALE = {0.125, 0.25, 0.5, 1.5, 3.0, 6.0, 12.0, 24.0, 48.0, 96.0};

    // Массив полей сообщения
    private String[] fields;
    private String msgType;

    /**
     * Конвертация сообщения в список сообщений типа SearadarStationMessage
     * @param exchange - контейнер для сообщения типа Exchange
     * @return - список сообщений
     */
    @Override
    public List<SearadarStationMessage> convert(Exchange exchange) {
        return convert(exchange.getIn().getBody(String.class));
    }

    /**
     * Конвертация сообщения в виде строки в список сообщений типа SearadarStationMessage
     * @param message - строка сообщения
     * @return - список сообщений
     */
    public List<SearadarStationMessage> convert(String message) {

        // При конвертации каждый раз создаём новый список сообщений
        List<SearadarStationMessage> msgList = new ArrayList<>();

        // Чтение полей сообщения. В процессе заполняется массив fields и msgType
        readFields(message);

        // В зависимости от типа сообщения применяем соответствующий метод для конвертации
        switch (msgType) {

            case "TTM" : msgList.add(getTTM());
                break;

            case "VHW" : msgList.add(getVHW());
                break;

            case "RSD" : {

                RadarSystemDataMessage rsd = getRSD();
                // Проверка сообщения на валидность
                InvalidMessage invalidMessage = checkRSD(rsd);

                if (invalidMessage != null)  msgList.add(invalidMessage);
                else msgList.add(rsd);
                break;
            }

        }

        return msgList;
    }

    /**
     * Чтение полей сообщения и заполнение массива fields и переменной msgType
     * в зависимости от типа сообщения
     * @param msg - строка сообщения
     */
    private void readFields(String msg) {

        String temp = msg.substring( 3, msg.indexOf("*") ).trim();

        fields = temp.split(Pattern.quote(","));
        msgType = fields[0];

    }

    /**
     * Создание сообщения типа TrackedTargetMessage.
     * Используется список fields, заполненная методом readFields
     * @return - сообщение типа TrackedTargetMessage
     */
    private TrackedTargetMessage getTTM() {

        // Создание сообщения
        TrackedTargetMessage ttm = new TrackedTargetMessage();
        // Время получения сообщения
        Long msgRecTimeMillis = System.currentTimeMillis();

        // Заполнение полей сообщения
        ttm.setMsgTime(msgRecTimeMillis);
        TargetStatus status = TargetStatus.UNRELIABLE_DATA;
        IFF iff = IFF.UNKNOWN;
        TargetType type = TargetType.UNKNOWN;

        switch (fields[12]) {
            case "L" : status = TargetStatus.LOST;
                break;

            case "Q" : status = TargetStatus.UNRELIABLE_DATA;
                break;

            case "T" : status = TargetStatus.TRACKED;
                break;
        }

        switch (fields[11]) {
            case "b" : iff = IFF.FRIEND;
                break;

            case "p" : iff = IFF.FOE;
                break;

            case "d" : iff = IFF.UNKNOWN;
                break;
        }

        ttm.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        ttm.setTargetNumber(Integer.parseInt(fields[1]));
        ttm.setDistance(Double.parseDouble(fields[2]));
        ttm.setBearing(Double.parseDouble(fields[3]));
        ttm.setCourse(Double.parseDouble(fields[6]));
        ttm.setSpeed(Double.parseDouble(fields[5]));
        ttm.setStatus(status);
        ttm.setIff(iff);

        ttm.setType(type);

        return ttm;
    }

    private WaterSpeedHeadingMessage getVHW() {

        WaterSpeedHeadingMessage vhw = new WaterSpeedHeadingMessage();

        vhw.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        vhw.setCourse(Double.parseDouble(fields[1]));
        vhw.setCourseAttr(fields[2]);
        vhw.setSpeed(Double.parseDouble(fields[5]));
        vhw.setSpeedUnit(fields[6]);

        return vhw;
    }

    /**
     * Создание сообщения типа RadarSystemDataMessage.
     * Используется список fields, заполненная методом readFields
     * @return - сообщение типа RadarSystemDataMessage
     */
    private RadarSystemDataMessage getRSD() {

        RadarSystemDataMessage rsd = new RadarSystemDataMessage();

        rsd.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        rsd.setInitialDistance(Double.parseDouble(fields[1]));
        rsd.setInitialBearing(Double.parseDouble(fields[2]));
        rsd.setMovingCircleOfDistance(Double.parseDouble(fields[3]));
        rsd.setBearing(Double.parseDouble(fields[4]));
        rsd.setDistanceFromShip(Double.parseDouble(fields[9]));
        rsd.setBearing2(Double.parseDouble(fields[10]));
        rsd.setDistanceScale(Double.parseDouble(fields[11]));
        rsd.setDistanceUnit(fields[12]);
        rsd.setDisplayOrientation(fields[13]);
        rsd.setWorkingMode(fields[14]);

        return rsd;
    }

    /**
     * Проверка сообщения типа RadarSystemDataMessage на валидность.
     * Сообщение считается невалидным, если значение поля distanceScale не входит в массив DISTANCE_SCALE
     * @param rsd - сообщение типа RadarSystemDataMessage
     * @return - сообщение типа InvalidMessage, если сообщение невалидно, иначе null
     */
    private InvalidMessage checkRSD(RadarSystemDataMessage rsd) {

        InvalidMessage invalidMessage = new InvalidMessage();
        String infoMsg = "";

        if (!Arrays.asList(DISTANCE_SCALE).contains(rsd.getDistanceScale())) {

            infoMsg = "RSD message. Wrong distance scale value: " + rsd.getDistanceScale();
            invalidMessage.setInfoMsg(infoMsg);
            return invalidMessage;
        }

        return null;
    }

}
