package co.atoth.intellij.plugin.chucknorris.ui;

import co.atoth.intellij.plugin.chucknorris.service.Fact;
import co.atoth.intellij.plugin.chucknorris.service.IcndbFactService;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FactJTextPane extends JTextPane {

    private final Logger log = Logger.getInstance(IcndbFactService.class);

    private List<Fact> factList = new ArrayList<>();
    private boolean isLoading;

    public FactJTextPane() {
        setOpaque(false);
        setBackground(JBColor.background());
        setFont(com.intellij.util.ui.UIUtil.getLabelFont().deriveFont(15));
        setEditable(false);
        setContentType("text/html");
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        updateView();
    }

    private String getHtmlString(List<Fact> facts) {
        StringBuilder builder = new StringBuilder();
        for (Fact fact : facts) {
            builder.append(fact.getText() + "<br><br>");
        }
        if (isLoading) {
            builder.append("Loading... <br>");
        }
        return builder.toString();
    }

    public void addFact(int index, Fact fact) {
        factList.add(index, fact);
        updateView();
    }

    public void addFact(Fact fact) {
        factList.add(fact);
        updateView();
    }

    public void addFacts(List<Fact> facts) {
        factList.addAll(facts);
        updateView();
    }

    private void updateView() {
        setText(getHtmlString(factList));
    }

}