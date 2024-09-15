package ru.antara.createNewTicket.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.createNewTicket.postprocessors.CreateNewTicketChek;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static ru.antara.createUser.helpers.RandomStringHelper.generateRandomString;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class CreateNewTicketFragment implements SimpleController {

    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/submit/","/tickets/submit/" )
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                .defaultValue("csrf_ERR")),

                httpSampler(">_/tickets/submit/", "/tickets/submit/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("queue", "2")
                        .rawParam("title", "Test new name " + generateRandomString(5))
                        .rawParam("body", "Описание тестового тикета" + generateRandomString(6))
                        .rawParam("priority", "3")
                        .rawParam("submitter_email", "test@test.test")
                        .children(regexExtractor("create_ticket","(Follow-Ups)")
                                        .defaultValue("Err_create_ticket"),
                                jsr223PostProcessor(CreateNewTicketChek.class))
                        .children(responseAssertion().containsSubstrings("Follow-Ups"))
        );
    }
}
