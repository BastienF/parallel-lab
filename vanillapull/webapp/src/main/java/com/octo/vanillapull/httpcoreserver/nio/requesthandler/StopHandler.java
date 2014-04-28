package com.octo.vanillapull.httpcoreserver.nio.requesthandler;

import com.octo.vanillapull.model.Instrument;
import com.octo.vanillapull.repository.InstrumentDao;
import com.octo.vanillapull.service.*;
import com.octo.vanillapull.web.PricerController;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Bastien on 08/04/2014.
 */
public class StopHandler implements HttpAsyncRequestHandler<HttpRequest> {

    @Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

    @Override
    public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) {
        HttpResponse response = httpexchange.getResponse();
        response.setStatusCode(HttpStatus.SC_OK);
        response.setEntity(new NStringEntity("STOP", ContentType.create("text/html", "UTF-8")));
        httpexchange.submitResponse();
        System.exit(0);
    }
}
