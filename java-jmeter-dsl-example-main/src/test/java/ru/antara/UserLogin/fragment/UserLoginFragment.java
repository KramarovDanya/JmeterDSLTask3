package ru.antara.UserLogin.fragment;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.admin_login.postprocessors.LoginCheck;
import ru.antara.common.interfaces.SimpleController;

import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static ru.antara.createUser.helpers.RandomStringHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class UserLoginFragment implements SimpleController {
    public DslSimpleController get() {
        return simpleController(

                httpSampler("<_/login", "/login")
                        .method(HTTPConstants.GET)
                        .rawParam("next", "/")
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("ERR_csrf_token")
                        ),

                httpSampler(">_/login/", "/login/")
                        .method(HTTPConstants.POST)
                        .rawParam("username", getLogin(readFile("Users.csv")))
                        .rawParam("password", getPassword(readFile("Users.csv")))
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("next", "/")
                        .children(regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                .defaultValue("ERR_csrf_token"))
                        .children(regexExtractor("query_encoded", "query_encoded\\' value\\=\\'(.*)\\'\\/\\>")
                                .defaultValue("ERR_query_encoded"))
                        .children(
                                regexExtractor("login_check", "(Logout)")
                                        .defaultValue("login_check_error"),
                                jsr223PostProcessor(LoginCheck.class)),

                httpSampler("<_/tickets/", "/tickets/")
                        .method(HTTPConstants.GET),

                httpSampler("<_/datatables_ticket_list/${query_encoded}", "/datatables_ticket_list/${query_encoded}")
                        .method(HTTPConstants.GET)

        );
    }
}
