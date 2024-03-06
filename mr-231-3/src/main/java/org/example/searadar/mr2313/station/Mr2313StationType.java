package org.example.searadar.mr2313.station;

import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import org.example.searadar.mr2313.convert.Mr2313Converter;
import ru.oogis.searadar.api.convert.SearadarExchangeConverter;
import ru.oogis.searadar.api.station.AbstractStationType;

import java.nio.charset.Charset;

public class Mr2313StationType {

    private static final String STATION_TYPE = "МР-231-3";
    private static final String CODEC_NAME = "mr2313";


    protected void doInitialize() {
        TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
                Charset.defaultCharset(),
                LineDelimiter.UNIX,
                LineDelimiter.CRLF
        );

    }

    /**
     * Создание конвертера для станции МР-231-3
     * @return - конвертер МР-231-3
     */
    public Mr2313Converter createConverter() {
        return new Mr2313Converter();
    }
}
