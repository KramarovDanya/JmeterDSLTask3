package ru.antara.admin_login.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class PaginationFragment implements SimpleController {
    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<__/login/", "/login/")
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
                httpSampler("<__/tickets/","/tickets")
                        .method(HTTPConstants.GET),
                httpSampler("<__/datatables_ticket_list/","/datatables_ticket_list/eyJmaWx0ZXJpbmciOiB7InN0YXR1c19faW4iOiBbMSwgMl19LCAic29ydGluZyI6ICJjcmVhdGVkIiwgInNlYXJjaF9zdHJpbmciOiAiIiwgInNvcnRyZXZlcnNlIjogZmFsc2V9")
                        .method(HTTPConstants.GET)
                        .rawParam("draw","1"),
                httpSampler("<__/datatables_ticket_list/", "/datatables_ticket_list/eyJmaWx0ZXJpbmciOiB7InN0YXR1c19faW4iOiBbMSwgMl19LCAic29ydGluZyI6ICJjcmVhdGVkIiwgInNlYXJjaF9zdHJpbmciOiAiIiwgInNvcnRyZXZlcnNlIjogZmFsc2V9")
                        .method(HTTPConstants.GET)
                        .rawParam("draw","2"),
                httpSampler("<__/datatables_ticket_list/","/datatables_ticket_list/eyJmaWx0ZXJpbmciOiB7InN0YXR1c19faW4iOiBbMSwgMl19LCAic29ydGluZyI6ICJjcmVhdGVkIiwgInNlYXJjaF9zdHJpbmciOiAiIiwgInNvcnRyZXZlcnNlIjogZmFsc2V9")
                        .method(HTTPConstants.GET)
                        .rawParam("draw","3")


        );
    }
}
