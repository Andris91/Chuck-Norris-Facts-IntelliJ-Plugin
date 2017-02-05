package co.atoth.intellij.plugin.chucknorris.settings;

import co.atoth.intellij.plugin.chucknorris.ui.SettingsJPanel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginSearchableConfigurable implements SearchableConfigurable {

    private PluginSettings settings = ServiceManager.getService(PluginSettings.class);
    private SettingsJPanel settingsPanel = new SettingsJPanel();;

    @NotNull
    @Override
    public String getId() {
        return PluginSearchableConfigurable.class.getCanonicalName();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Chuck Norris Facts Plugin Settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            settingsPanel.setFactNo(settings.getFactLoadCount());
            settingsPanel.setFirstName(settings.getFactFirstName());
            settingsPanel.setLastName(settings.getFactLastName());
        }, ModalityState.current());
        return settingsPanel;
    }

    @Override
    public boolean isModified() {
        if(settings.getFactLoadCount() != settingsPanel.getFactNo() ||
                settings.getFactFirstName() != settingsPanel.getFirstName() ||
                settings.getFactLastName()  != settingsPanel.getLastName()){
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setFactLastName(settingsPanel.getLastName());
        settings.setFactFirstName(settingsPanel.getFirstName());
        settings.setFactLoadCount(settingsPanel.getFactNo());
    }

    @Override
    public void reset() {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            settingsPanel.setFactNo(settings.getFactLoadCount());
            settingsPanel.setFirstName(settings.getFactFirstName());
            settingsPanel.setLastName(settings.getFactLastName());
        }, ModalityState.current());
    }

    @Override
    public Runnable enableSearch(String option) {
        return () -> {};
    }

    @Override
    public void disposeUIResources() {}
}