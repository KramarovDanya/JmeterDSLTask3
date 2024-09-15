package ru.antara.pagination.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.pagination.postprocessors.PaginationCheck;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class PaginationFragment implements SimpleController {
    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/login/", "/login/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler(">_/login/","/login/")
                        .method(HTTPConstants.POST)
                        .rawParam("username","${username}")
                        .rawParam("password", "${password}")
                        .rawParam("next","/")
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}"),
                httpSampler("<_/tickets/","/tickets")
                        .method(HTTPConstants.GET),
                httpSampler("<_/datatables_ticket_list/","/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "10")
                        .rawParam("start", "0")
                        .rawParam("draw","1")
                        .children(regexExtractor("first_ticket_db1", "\\[(\\w+-\\d+)\\]")
                                .defaultValue("Error first ticket data base one")),
                httpSampler("<_/datatables_ticket_list/", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "25")
                        .rawParam("start", "10")
                        .rawParam("draw","2")
                        .children(regexExtractor("first_ticket_db2", "\\[(\\w+-\\d+)\\]")
                                        .defaultValue("Error first ticket data base two"),
                                jsr223PostProcessor(PaginationCheck.class))


        );
    }
}
