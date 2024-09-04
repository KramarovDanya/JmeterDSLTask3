package ru.antara.admin_login.fragments;

import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class CreateNewTicketFragment implements SimpleController {

    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/tickets/submit/","/tickets/submit/" )
                        .method(HTTPConstants.GET),

                httpSampler(">_/tickets/submit/", "/tickets/submit/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("queue", "2")
                        .rawParam("title", "Test tickettt kitiket ${username}")
                        .rawParam("body", "Описание тестового тикета")
                        .rawParam("priority", "3")
                        .rawParam("submitter_email", "test@test.test")
                        .children(responseAssertion().containsSubstrings("Follow-Ups")
                        ),
                httpSampler("<_/tickets/submit/", "/tickets/submit/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/view/", "/view/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/login/","/login/")
                        .method(HTTPConstants.POST)
                        .rawParam("username","${username}")
                        .rawParam("password","${password}")
                        .rawParam("next","/")
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
        );
    }
}
