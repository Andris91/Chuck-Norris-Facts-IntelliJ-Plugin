package co.atoth.intellij.plugin.chucknorris;

import com.btr.proxy.search.ProxySearch;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PluginToolWindowFactory implements ToolWindowFactory {

    private final Logger log = Logger.getInstance(PluginToolWindowFactory.class);

    private JTextPane textPane;
    private List<JSONObject> facts = new ArrayList<>();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));

        //Toolbar
        GetFactAction action = new GetFactAction(project);
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(action);
        JComponent component = ActionManager.getInstance().createActionToolbar("chucknorrisplugin", actionGroup, false).getComponent();
        component.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, JBColor.border()));
        rootPanel.add(component, BorderLayout.WEST);

        //List
        textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setBackground(JBColor.background());
        textPane.setFont(com.intellij.util.ui.UIUtil.getLabelFont().deriveFont(15));
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        JBScrollPane scrollPane = new JBScrollPane(textPane);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        toolWindow.getContentManager().addContent(contentFactory.createContent(rootPanel, "", false));
    }

    private String getHtmlString(List<JSONObject> facts){
        StringBuilder builder = new StringBuilder();

        for(JSONObject fact : facts){
            try {
                if(fact.has("value")) {
                    JSONObject value = fact.getJSONObject("value");
                    builder.append(value.get("joke") + "<hr>");
                }
            } catch (JSONException ex){
                log.error(ex);
            }
        }
        return builder.toString();
    }

    public class GetFactAction extends AnAction {

        private static final String API_ADDRESS = "http://api.icndb.com/jokes/random";
        private Project project;

        public GetFactAction(Project project){
            super("Get fact", "Load new Chuck Norris fact", IconLoader.findIcon("/icons/chuck_16x16.png"));
            this.project = project;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            ApplicationManager.getApplication().invokeLater(() -> {
                Task.Backgroundable backgroundTask = new Task.Backgroundable(project, "Fetching Chuck Norris fact...", false) {
                    public void run(@NotNull ProgressIndicator indicator) {
                        e.getPresentation().setEnabled(false);
                        try {
                            URI uri = URI.create(API_ADDRESS);
                            URLConnection connection = uri.toURL().openConnection(findProxy());
                            BufferedReader bR = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String line;
                            StringBuilder responseStrBuilder = new StringBuilder();
                            while((line =  bR.readLine()) != null){
                                responseStrBuilder.append(line);
                            }
                            JSONObject result = new JSONObject(responseStrBuilder.toString());

                            facts.add(0, result);
                            textPane.setText(getHtmlString(facts));
                        } catch (IOException | JSONException ex ) {
                            log.debug(ex);
                        } finally {
                            e.getPresentation().setEnabled(true);
                        }
                    }
                };
                backgroundTask.queue();
            });
        }

        public Proxy findProxy(){
            ProxySearch proxySearch = new ProxySearch();
            proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
            proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
            proxySearch.addStrategy(ProxySearch.Strategy.BROWSER);
            ProxySelector proxySelector = proxySearch.getProxySelector();
            ProxySelector.setDefault(proxySelector);
            URI home = URI.create(API_ADDRESS);
            log.debug("ProxySelector: " + proxySelector);
            log.debug("URI: " + home);
            List<Proxy> proxyList = proxySelector.select(home);
            if (proxyList != null && !proxyList.isEmpty()) {
                return proxyList.iterator().next();
            }
            return Proxy.NO_PROXY;
        }
    }

}