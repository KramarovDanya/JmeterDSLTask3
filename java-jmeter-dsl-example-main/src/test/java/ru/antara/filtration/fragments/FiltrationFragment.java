package ru.antara.filtration.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.filtration.postprocessors.FiltrationCheck;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import java.util.Random;

import static ru.antara.createUser.helpers.RandomStringHelper.generateRandomString;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class FiltrationFragment implements SimpleController {
    Random rand = new Random();

    String title = generateRandomString(10);

    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/login/","/login/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/login/", "/login/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),

                httpSampler("<__/datatables_ticket_list/${query_encoded}","/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "10")
                        .rawParam("start", "0"),
                httpSampler("<__/datatables_ticket_list/${query_encoded}", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)
                        .rawParam("length", "10")
                        .rawParam("start", "10"),
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("sortx","created")
                        .rawParam("status",String.valueOf(rand.nextInt(5)))
                        .children(regexExtractor("first_saved_query",
                                "saved_query=(\\d+)\">.*\\s+<\\/a>\\s+<\\/div>")
                                .defaultValue("Error_first_saved_query")),

                httpSampler("<_/tickets/","/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("saved_query","${query_encoded}"),


                httpSampler(">_/save_query/", "/save_query/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("query_encoded", "${query_encoded}")
                        .rawParam("title", title)
                        .children(regexExtractor("second_saved_query",
                                        "saved_query=(\\d+)\">.*\\s+<\\/a>\\s+<\\/div>")
                                        .defaultValue("Error_second_saved_query"),
                                jsr223PostProcessor(FiltrationCheck.class)),
                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET)
                        .rawParam("saved_query", "${second_saved_query}")
        );

    }
}
