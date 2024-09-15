package ru.antara.createUser.fragments;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import ru.antara.common.interfaces.SimpleController;
import ru.antara.createNewTicket.postprocessors.CreateNewTicketChek;
import ru.antara.createUser.helpers.RandomStringHelper;
import ru.antara.createUser.postprocessors.CreateUserCheck;
import us.abstracta.jmeter.javadsl.core.controllers.DslSimpleController;

import static ru.antara.createUser.helpers.RandomStringHelper.writeFile;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class CreateUserFragment implements SimpleController {
    String username = RandomStringHelper.generateRandomString(7);
    String password = RandomStringHelper.generateRandomString(10);



    @Override
    public DslSimpleController get() {
        writeFile(username, password, "Users.csv");
        return simpleController(
                httpSampler("<_/system_settings/", "/system_settings/")
                        .method(HTTPConstants.GET)
                        .children(
                                regexExtractor("csrf_token", "csrfmiddlewaretoken\".*value=\"(.*)\">")
                                        .defaultValue("csrf_ERR")
                        ),
                httpSampler("<_/admin/auth/user/", "/admin/auth/user/")
                        .method(HTTPConstants.GET),
                httpSampler("<_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.GET),
                httpSampler(">_/admin/auth/user/add/", "/admin/auth/user/add/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken","${csrf_token}")
                        .rawParam("username", username)
                        .rawParam("password1",password)
                        .rawParam("password2", password)
                        .rawParam("_save", "Save")
                        .children(
                                regexExtractor("user_id", "\\/(\\d+)\\/")
                                        .template("$1$")
                                        .defaultValue("ERR_user_id")
                        )
                        .children(regexExtractor("date_joined_0", "date_joined_0\"\\svalue=\"(..........)").defaultValue("ERR_date_joined_0"))
                        .children(regexExtractor("date_joined_1", "date_joined_1\"\\svalue=\"(........)").defaultValue("ERR_date_joined_1"))
                        .children(regexExtractor("check_create_user", "(You may edit it again below.)")
                                .defaultValue("Error_create_user"),
                                jsr223PostProcessor(CreateUserCheck.class)),
                httpSampler(">_/admin/auth/user/${user_id}/change/",
                        "/admin/auth/user/${user_id}/change/")
                        .method(HTTPConstants.POST)
                        .rawParam("csrfmiddlewaretoken", "${csrf_token}")
                        .rawParam("username", username)
                        .rawParam("is_active", "on")
                        .rawParam("is_staff", "on")
                        .rawParam("date_joined_0", "${date_joined_0}")
                        .rawParam("date_joined_1", "${date_joined_1}")
                        .rawParam("initial-date_joined_0", "${date_joined_0}")
                        .rawParam("initial-date_joined_1", "${date_joined_1}")
                        .rawParam("_save", "Save"),
                httpSampler("<_/admin/auth/user/", "/admin/auth/user/")
                        .method(HTTPConstants.GET)
        );


    }
}
