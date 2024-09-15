package ru.antara.changeTicket.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class ChangeTicketCheck implements DslJsr223PostProcessor.PostProcessorScript {


    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String secondNewStatus = s.vars.get("second_new_status");
        String newStatus = s.vars.get("check_status");
        String randomTicket = s.vars.get("random_ticket");

        if (!Objects.equals(secondNewStatus, newStatus) || Objects.equals(secondNewStatus, "ERR_Sec_New_Stat")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/tickets/%s/Err_Change_ticket".formatted(randomTicket));
            s.prev.setResponseMessage(
                    "Check status equals %s ".formatted(newStatus) +
                            "Second check status equals %s".formatted(secondNewStatus));
        }
    }
}
