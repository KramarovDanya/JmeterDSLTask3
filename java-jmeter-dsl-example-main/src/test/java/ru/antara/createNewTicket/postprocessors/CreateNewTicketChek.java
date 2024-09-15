package ru.antara.createNewTicket.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class CreateNewTicketChek implements DslJsr223PostProcessor.PostProcessorScript{

    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String check = s.vars.get("create_ticket");

        if (!Objects.equals(check, "Follow-Ups") || Objects.equals(check,"Err_create_ticket")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/tickets/submit/ERROR");
            s.prev.setResponseMessage(
                    "Error create ticket some problem");
        }
    }
}
