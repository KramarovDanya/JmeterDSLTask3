package ru.antara.deleteTicket.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.createUser.postprocessors.CreateUserCheck;
import ru.antara.deleteTicket.postprocessors.DeleteTicketCheck;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class DeleteTicketFragment implements SimpleController {
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
                httpSampler("<__/login/","/login/")
                        .method(HTTPConstants.GET)
                        .rawParam("next","/")
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/datatables_ticket_list/${query_encoded}", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("Random_ticket", "ticket\":\\s\"(\\d+)")
                                        .matchNumber(0)
                                        .defaultValue("ERROR_Num_Ticket")
                        ),

                httpSampler("<_/tickets/${Random_ticket}/", "/tickets/${Random_ticket}/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/tickets/${Random_ticket}/delete/", "/tickets/${Random_ticket}/delete/")
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("Ticket_name", "Are you sure you want to delete this ticket.*\\(<em>(\\w+)")
                                .defaultValue("Error_get_name")),
                httpSampler(">_/tickets/${Random_ticket}/delete/", "/tickets/${Random_ticket}/delete/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("next", "home"),
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET),
                httpSampler("/datatables_ticket_list/${query_encoded}", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("Check_name_in_database", "%s".formatted("${Ticket_name}"))
                                .defaultValue("Name_not_found_in_database"),
                        jsr223PostProcessor(DeleteTicketCheck.class))

        );
    }
}
