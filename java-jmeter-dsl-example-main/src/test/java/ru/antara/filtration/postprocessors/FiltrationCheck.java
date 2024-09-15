package ru.antara.filtration.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class FiltrationCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String firstQuery = s.vars.get("first_saved_query");
        String secondQuery = s.vars.get("second_saved_query");
        System.out.println(firstQuery +" "+ secondQuery);


        if (Objects.equals(firstQuery , secondQuery) || Objects.equals(firstQuery, "Error_first_saved_query") ||
                Objects.equals(secondQuery, "Error_second_saved_query")) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/save_query//Error");
            s.prev.setResponseMessage(
                    "Error, save query was some problem");
        }
    }
}
