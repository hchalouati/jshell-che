package org.eclipse.che.ide.ext.dashboard.client;

import static org.eclipse.che.ide.api.action.IdeActions.GROUP_MAIN_MENU;

import com.google.inject.Inject;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.extension.Extension;

@Extension(title = "Sample Actions Extension", version = "1.0.0")
public class SampleActionsExtensions {
  @Inject
  public SampleActionsExtensions(JshellAction jshellAction, ActionManager actionManager) {

    actionManager.registerAction("Charting", jshellAction);
    DefaultActionGroup sampleGroup = new DefaultActionGroup("JSHELL", true, actionManager);

    sampleGroup.add(jshellAction);

    DefaultActionGroup mainMenu = (DefaultActionGroup) actionManager.getAction(GROUP_MAIN_MENU);
    mainMenu.add(sampleGroup);


  }
}
