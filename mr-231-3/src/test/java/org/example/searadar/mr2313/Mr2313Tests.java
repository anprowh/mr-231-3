package org.example.searadar.mr2313;

import java.util.List;

import org.example.searadar.mr2313.convert.Mr2313Converter;
import org.example.searadar.mr2313.station.Mr2313StationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.TrackedTargetMessage;

/**
 * Тесты для Mr2313
 * Проверка правильности работы конвертера
*/
public class Mr2313Tests {
    
        /**
         * Проверка следущего функционала:
         * Создание станции МР-231-3
         * Создание конвертера для станции МР-231-3
         * Для разных типов сообщений проверка правильности конвертированияЖ
         * Проверка типа конвертированного сообщения
         * Сравнение конвертированных сообщений с ожидаемыми
         */
        @Test
        public void testMr2313Converter() {
            Mr2313StationType mr2313 = new Mr2313StationType();
            Mr2313Converter converter = mr2313.createConverter();

            // Тестирование сообщений TTM
            List<SearadarStationMessage> searadarMessages3 = converter.convert("$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,,А*42");
            
            // Проверка типа сообщения
            SearadarStationMessage message = searadarMessages3.get(0);
            assertTrue(message instanceof TrackedTargetMessage);

            // Преобразование сообщения в TrackedTargetMessage
            TrackedTargetMessage targetMessage = (TrackedTargetMessage) message;
            
            //TrackedTargetMessage(targetNumber=66, distance=28.71, bearing=341.1, course=24.5, speed=57.6, type=UNKNOWN, status=LOST, iff=FRIEND)
            // Проверка, что результат соответствует ожидаемому
            assertEquals(66, targetMessage.getTargetNumber());
            assertEquals(28.71, targetMessage.getDistance());
            assertEquals(341.1, targetMessage.getBearing());
            assertEquals(24.5, targetMessage.getCourse());
            assertEquals(57.6, targetMessage.getSpeed());
            assertEquals("UNKNOWN", targetMessage.getType().name());
            assertEquals("LOST", targetMessage.getStatus().name());
            assertEquals("FRIEND", targetMessage.getIff().name());

            // Тестирование сообщений RSD
            searadarMessages3 = converter.convert("$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33");

            // Проверка типа сообщения
            message = searadarMessages3.get(0);
            assertTrue(message instanceof RadarSystemDataMessage);

            // Преобразование сообщения в RadarSystemDataMessage
            RadarSystemDataMessage radarSystemDataMessage = (RadarSystemDataMessage) message;

            //RadarSystemData{msgRecTime=, initialDistance=36.5, initialBearing=331.4, movingCircleOfDistance=8.4, bearing=320.6, distanceFromShip=11.6, bearing2=185.3, distanceScale=96.0, distanceUnit=N, displayOrientation=N, workingMode=S}
            // Проверка, что результат соответствует ожидаемому
            assertEquals(36.5, radarSystemDataMessage.getInitialDistance());
            assertEquals(331.4, radarSystemDataMessage.getInitialBearing());
            assertEquals(8.4, radarSystemDataMessage.getMovingCircleOfDistance());
            assertEquals(320.6, radarSystemDataMessage.getBearing());
            assertEquals(11.6, radarSystemDataMessage.getDistanceFromShip());
            assertEquals(185.3, radarSystemDataMessage.getBearing2());
            assertEquals(96.0, radarSystemDataMessage.getDistanceScale());
            assertEquals("N", radarSystemDataMessage.getDistanceUnit());
            assertEquals("N", radarSystemDataMessage.getDisplayOrientation());
            assertEquals("S", radarSystemDataMessage.getWorkingMode());

            // Тестирование неверного сообщения
            searadarMessages3 = converter.convert("$RARSD,14.0,0.0,96.9,306.4,,,,,97.7,11.6,0.3,K,N,S*20");

            // Проверка типа сообщения
            message = searadarMessages3.get(0);
            assertTrue(message instanceof InvalidMessage);

        }



}
