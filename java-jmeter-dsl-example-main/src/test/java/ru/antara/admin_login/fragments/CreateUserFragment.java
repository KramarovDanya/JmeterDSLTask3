package ru.antara.admin_login.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class CreateUserFragment implements SimpleController {

    @Override
    public DslSimpleController get() {
        return simpleController(
                httpSampler("<_/system_settings/", "/system_settings/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler("<_/admin/auth/user/", "/admin/auth/user/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("user_id", "\\/(\\d+)\\/")
                                        .matchNumber(1)
                                        .defaultValue("ERR_user_id")
                        ),
                httpSampler("<_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken","${csrf_token}")
                        .rawParam("username", "СТЕПАН")//"${__RandomString(10,ABCDEFabcdef12345,username)}")
                        .rawParam("password1","${__RandomString(10,ABCDEFabcdef12345,userpassword)}" )
                        .rawParam("password2", "${userpassword}")
                        .rawParam("_save", "Save"),
                httpSampler(">_/admin/auth/user/${user_id}/change/", "/admin/auth/user/${user_id}/change")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("username", "${username}")
                        .rawParam("is_active", "on")
                        .rawParam("is_staff", "on")
                        .rawParam("_save", "Save"),
                httpSampler("<_/admin/auth/user/", "/admin/auth/user"));

    }
}
