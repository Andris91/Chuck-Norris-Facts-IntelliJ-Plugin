package co.atoth.intellij.plugin.chucknorris.service;

import co.atoth.intellij.plugin.chucknorris.settings.PluginSettings;

import java.util.List;

public interface FactService {
    public List<Fact> getRandomFacts(PluginSettings settings);
}
