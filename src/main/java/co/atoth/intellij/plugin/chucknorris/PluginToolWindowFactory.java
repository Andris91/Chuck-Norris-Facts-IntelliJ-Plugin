package co.atoth.intellij.plugin.chucknorris;

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
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PluginToolWindowFactory implements ToolWindowFactory {

    private final Logger log = Logger.getInstance(PluginToolWindowFactory.class);

    private JTextPane textPane;

    private FactService factService = new FactService();
    private FactJTextPane factPanel = new FactJTextPane();

    private AnAction getFactAction = new AnAction("Get fact", "Load new Chuck Norris fact", IconLoader.findIcon("/icons/chuck_16x16.png")){
        @Override
        public void actionPerformed(AnActionEvent e) {
            ApplicationManager.getApplication().invokeLater(() -> {
                factPanel.setLoading(true);
                e.getPresentation().setEnabled(false);

                Task.Backgroundable backgroundTask = new Task.Backgroundable(null,"Fetching Chuck Norris fact...", false) {
                    public void run(@NotNull ProgressIndicator indicator) {
                        factPanel.addFact(factService.getRandomFact());
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
        JComponent component = ActionManager.getInstance().createActionToolbar("chucknorrisplugin", actionGroup, false).getComponent();
        rootPanel.add(component, BorderLayout.WEST);

        //Facts
        JBScrollPane scrollPane = new JBScrollPane(factPanel);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        toolWindow.getContentManager().addContent(contentFactory.createContent(rootPanel, "", false));

        //Load 1 fact at the start
        factPanel.setLoading(true);
        Task.Backgroundable backgroundTask = new Task.Backgroundable(null,"Fetching Chuck Norris fact...", false) {
            public void run(@NotNull ProgressIndicator indicator) {
                factPanel.addFact(factService.getRandomFact());
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