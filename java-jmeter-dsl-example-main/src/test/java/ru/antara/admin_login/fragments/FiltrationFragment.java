package ru.antara.admin_login.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class FiltrationFragment implements SimpleController {
    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/login/","/login/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/login/", "/login/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR"),
                                regexExtractor("query_encoded","query_encoded\\' value\\=\\'(.*)\\'\\/\\>").matchNumber(1).defaultValue("ERROR_Q_ENCODED")

                        ),
                httpSampler("<__/datatables_ticket_list/{query_encoded}","/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "10")
                        .rawParam("start", "0"),
                httpSampler("<__/datatables_ticket_list/${query_encoded}", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "10")
                        .rawParam("start", "10"),
                httpSampler("<__/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("sortx","created")
                        .rawParam("status","1"),
                httpSampler("<__/tickets/","/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("saved_query","${query_encoded}"),
                httpSampler("<__/save_query/", "/save_query/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "csrf_token")
                        .rawParam("query_encoded", "${query_encoded}")
                        .rawParam("title", "${__RandomString(5,abcdefghijk12345,myRandom)}")

        );
    }
}
