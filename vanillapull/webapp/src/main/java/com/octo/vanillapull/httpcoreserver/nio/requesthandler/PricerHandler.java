package com.octo.vanillapull.httpcoreserver.nio.requesthandler;

import com.octo.vanillapull.httpcoreserver.nio.AsyncServer;
import com.octo.vanillapull.httpcoreserver.nio.Server;
import com.octo.vanillapull.httpcoreserver.nio.dao.InstrumentDAO;
import com.octo.vanillapull.model.Instrument;
import com.octo.vanillapull.service.scala.PricingService;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Bastien on 08/04/2014.
 */
public class PricerHandler implements HttpAsyncRequestHandler<HttpRequest> {
    public final static Logger logger = LoggerFactory.getLogger(PricerHandler.class);
    private final PricingService _pricingService;
    private final InstrumentDAO _instrumentDAO;

    public PricerHandler(PricingService pricingService, InstrumentDAO instrumentDAO) {
        _instrumentDAO = instrumentDAO;
        _pricingService = pricingService;
    }

    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) {
        if (Server.POLLED)
            AsyncServer.pool.submit(new HandleRequestTask(request, httpexchange, context));
        else
            executeRequest(request, httpexchange, context);
    }

    private void executeRequest(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) {
        String symbol = null;
        double maturity = 0;
        double strike = 0;
        List<NameValuePair> parameters = null;
        try {
            parameters = URLEncodedUtils.parse(new URI(
                    request.getRequestLine().getUri()), HTTP.UTF_8);
        } catch (URISyntaxException e) {
            logger.error("Failed to parse requested URL : " + e.getMessage());
        }
        for (NameValuePair nameValuePair : parameters) {
            if (nameValuePair.getName().equals("symbol"))
                symbol = nameValuePair.getValue();
            else if (nameValuePair.getName().equals("maturity"))
                maturity = Double.parseDouble(nameValuePair.getValue());
            else if (nameValuePair.getName().equals("strike"))
                strike = Double.parseDouble(nameValuePair.getValue());
        }
        Instrument instrument = _instrumentDAO.getInstrument(symbol);
        Double result = _pricingService.calculatePrice(maturity, instrument.getSpot(), strike, instrument.getVolatility());
        HttpResponse response = httpexchange.getResponse();
        response.setStatusCode(HttpStatus.SC_OK);
        response.setEntity(new NStringEntity(result.toString(), ContentType.create("text/html", "UTF-8")));
        httpexchange.submitResponse();
    }

    private class HandleRequestTask implements Callable<Void> {
        private final HttpRequest _request;
        private final HttpAsyncExchange _httpexchange;
        private final HttpContext _context;

        private HandleRequestTask(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) {
            _request = request;
            _httpexchange = httpexchange;
            _context = context;
        }

        @Override
        public Void call() {
            executeRequest(_request, _httpexchange, _context);
            return null;
        }
    }
}
