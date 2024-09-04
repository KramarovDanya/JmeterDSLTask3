package ru.antara.admin_login.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class OpenTicketTestFragment implements SimpleController {
    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/", "/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("query_encoded","query_encoded\\' value\\=\\'(.*)\\'\\/\\>")
                                        .matchNumber(1)
                                        .defaultValue("ERROR_Q_ENCODED")),
                httpSampler("<_/login/","/login/" )
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler(">__/login/", "/login/")
                        .method(HTTPConstants.POST)
                        .rawParam("username","${username}")
                        .rawParam("password","${password}")
                        .rawParam("next","/")
                        .rawParam("csrfmiddlewaretoken","${csrf_token}"),

                httpSampler("<_/tickets/","/tickets/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/datatables_ticket_list/${query_encoded}","/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("Random_ticket","ticket\":\\s\"(\\d+)").matchNumber(0).defaultValue("ERROR_Num_Ticket")),
                httpSampler("<__/tickets/${Random_ticket}/","/tickets/${Random_ticket}/")
                        .method(HTTPConstants.GET)





        );
    }
}
