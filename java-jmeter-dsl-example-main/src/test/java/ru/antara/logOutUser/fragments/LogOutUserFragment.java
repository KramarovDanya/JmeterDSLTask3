package ru.antara.logOutUser.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.logOutUser.postprocessors.LogOutCheck;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;

public class LogOutUserFragment implements SimpleController {

    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/", "/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/logout/","/logout/")
                        .method(HTTPConstants.GET)
                        .children(regexExtractor("login", "(Log\\sIn)").defaultValue("ERR_login"),
                                jsr223PostProcessor(LogOutCheck.class))

        );
    }
}
