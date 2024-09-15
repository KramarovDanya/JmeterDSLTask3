package ru.antara.logOutUser.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class LogOutCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {

        String login = s.vars.get("login");
        System.out.println(login);

        if (!Objects.equals(login, "Log In") || Objects.equals(login, "Error_login")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/logout/error");
        }
    }
}
