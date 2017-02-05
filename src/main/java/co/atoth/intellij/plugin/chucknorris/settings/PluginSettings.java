package co.atoth.intellij.plugin.chucknorris.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;


@State(
        name = PluginSettings.XML_ROOT_TAG_NAME,
        storages = {@Storage(id=PluginSettings.XML_ROOT_TAG_NAME, value = PluginSettings.XML_FILE_NAME)}
)
public class PluginSettings implements PersistentStateComponent<PluginSettings.State> {

    public static final String DEFAULT_FIRST_NAME = "Chuck";
    public static final String DEFAULT_LAST_NAME = "Norris";
    public static final int DEFAULT_FACT_NO = 1;

    public static final String XML_ROOT_TAG_NAME = "ChuckNorrisIntelliJPlugin";
    public static final String XML_FILE_NAME = "ChuckNorrisIntelliJPlugin.xml";

    private State state = new State();

    @Nullable
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void loadState(State state) {
        this.state = state;
    }

    static class State {
        public int factLoadCount = DEFAULT_FACT_NO;
        public String factFirstName = DEFAULT_FIRST_NAME;
        public String factLastName = DEFAULT_LAST_NAME;
    }

    public int getFactLoadCount() {
        return state.factLoadCount;
    }

    public void setFactLoadCount(int factLoadCount) {
        this.state.factLoadCount = factLoadCount;
    }

    public String getFactFirstName() {
        return state.factFirstName;
    }

    public void setFactFirstName(String factFirstName) {
        this.state.factFirstName = factFirstName;
    }

    public String getFactLastName() {
        return state.factLastName;
    }

    public void setFactLastName(String factLastName) {
        this.state.factLastName = factLastName;
    }

}