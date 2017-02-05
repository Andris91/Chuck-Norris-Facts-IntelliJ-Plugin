package co.atoth.intellij.plugin.chucknorris;

import co.atoth.intellij.plugin.chucknorris.service.FactService;
import co.atoth.intellij.plugin.chucknorris.service.IcndbFactService;
import co.atoth.intellij.plugin.chucknorris.settings.PluginSearchableConfigurable;
import co.atoth.intellij.plugin.chucknorris.settings.PluginSettings;
import co.atoth.intellij.plugin.chucknorris.ui.FactJTextPane;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PluginToolWindowFactory implements ToolWindowFactory {

    private final Logger log = Logger.getInstance(PluginToolWindowFactory.class);

    //Services
    private FactService factService = new IcndbFactService();
    private PluginSettings settings = ServiceManager.getService(PluginSettings.class);

    //UI
    private FactJTextPane factPanel = new FactJTextPane();

    //Actions
    private String loadingFactMessage = "Loading " + settings.getFactFirstName() + " " +  settings.getFactFirstName() + " fact...";
    private String actionDescription = "Load new " + settings.getFactFirstName() + " " +  settings.getFactFirstName() + " fact";
    private String actionName = "Load fact";
    private Icon actionIcon = IconLoader.findIcon("/img/chuck_16x16.png");

    private AnAction getFactAction = new AnAction(actionName, actionDescription, actionIcon){
        @Override
        public void actionPerformed(AnActionEvent e) {
            ApplicationManager.getApplication().invokeLater(() -> {
                factPanel.setLoading(true);
                e.getPresentation().setEnabled(false);

                Task.Backgroundable backgroundTask = new Task.Backgroundable(null,loadingFactMessage, false) {
                    public void run(@NotNull ProgressIndicator indicator) {
                        factPanel.addFacts(factService.getRandomFacts(settings));
                    }
                    @Override
                    public void onFinished() {
                        factPanel.setLoading(false);
                        e.getPresentation().setEnabled(true);
                    }
                    @Override
                    public void onError(@NotNull Exception error) {
                        factPanel.setLoading(false);
                        e.getPresentation().setEnabled(true);
                        super.onError(error);
                    }
                };
                backgroundTask.queue();
            });
        }
    };

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));

        //Toolbar
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(getFactAction);
        //Settings action
        actionGroup.add(new AnAction("Settings", "See Chuck Norris Plugin Settings", AllIcons.General.Settings) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, PluginSearchableConfigurable.class);
            }
        });
        JComponent component = ActionManager.getInstance().createActionToolbar("chucknorrisplugin", actionGroup, false).getComponent();
        rootPanel.add(component, BorderLayout.WEST);

        //Facts
        JBScrollPane scrollPane = new JBScrollPane(factPanel);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        toolWindow.getContentManager().addContent(contentFactory.createContent(rootPanel, "", false));

        //Load facts at the start
        factPanel.setLoading(true);
        Task.Backgroundable backgroundTask = new Task.Backgroundable(null,loadingFactMessage, false) {
            public void run(@NotNull ProgressIndicator indicator) {
                factPanel.addFacts(factService.getRandomFacts(settings));
            }
            @Override
            public void onFinished() {
                factPanel.setLoading(false);
                super.onFinished();
            }
        };
        backgroundTask.queue();
    }

}