package ru.antara.openTicketTest.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class OpenTicketCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String check = s.vars.get("Check_ticket");

        if (!Objects.equals(check, "Submitter E-Mail") || Objects.equals(check,"Err_open_ticket")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/tickets//ERROR_open_ticket");
            s.prev.setResponseMessage(
                    "Error open ticket some problem");
        }
    }
}
