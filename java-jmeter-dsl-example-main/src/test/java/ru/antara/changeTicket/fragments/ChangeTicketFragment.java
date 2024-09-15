package ru.antara.changeTicket.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.changeTicket.postprocessors.ChangeTicketCheck;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class ChangeTicketFragment implements SimpleController {
    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/", "/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("query_encoded","query_encoded\\' value\\=\\'(.*)\\'\\/\\>")
                                        .matchNumber(1)
                                        .defaultValue("ERROR_Q_ENCODED")
                        ),
                httpSampler("<__/datatables_ticket_list/${query_encoded}","/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .children( regexExtractor("Random_ticket","ticket\":\\s\"(\\d+)").matchNumber(0).defaultValue("ERROR_Num_Ticket")),
                httpSampler("<__/tickets/","/tickets/")
                        .method(HTTPConstants.GET),
                httpSampler("<__/tickets/${Random_ticket}/","/tickets/${Random_ticket}/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("new_status","new_status'\\svalue='(\\d)").matchNumber(0).defaultValue("ERROR_New_status"),
                                regexExtractor("owner","owner'><option value='(\\d)'").matchNumber(1).defaultValue("ERROR_owner"),
                                regexExtractor("priority","<td class=\".*\">(\\d)").matchNumber(1).defaultValue("ERR_priority_id"),
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">"),
                                regexExtractor("title_name","(\\w+)\\s+\\[\\w+\\]<\\/h3>\\s+.*:.*\\s+<span class='ticket_toolbar float-right'>").matchNumber(1).defaultValue("ERROR_title_name")),
                httpSampler(">__/tickets/${Random_ticket}/update/","/tickets/${Random_ticket}/update/")
                        .method(HTTPConstants.POST)
                        .rawParam("comment","RandomComment111")
                        .rawParam("new_status","${new_status}")
                        .rawParam("public","1")
                        .rawParam("title","${title_name}")
                        .rawParam("owner","${owner}")
                        .rawParam("priority","${priority}")
                        .rawParam("csrfmiddlewaretoken","${csrf_token}")
                        .children(regexExtractor("check_status", "from\\s\\w+\\sto\\s(\\w+)")
                                .defaultValue("ERR_check_status")),

                httpSampler("<__/tickets/${Random_ticket}/","/tickets/${Random_ticket}/")
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("second_new_status", "from\\s\\w+\\sto\\s(\\w+)")
                                        .defaultValue("ERR_Sec_New_Stat"),
                                jsr223PostProcessor(ChangeTicketCheck.class))

        );
    }
}
