package ru.antara.createUser.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class CreateUserCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String check = s.vars.get("check_create_user");

        if (!Objects.equals(check, "You may edit it again below.") || Objects.equals(check,"Error_create_user")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/admin/auth/user/add/ERROR");
            s.prev.setResponseMessage(
                    "Error create user some problem");
        }
    }
}
