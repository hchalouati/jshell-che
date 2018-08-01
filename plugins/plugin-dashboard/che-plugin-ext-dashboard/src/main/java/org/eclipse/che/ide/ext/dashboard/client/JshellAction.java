package org.eclipse.che.ide.ext.dashboard.client;

import com.google.inject.Inject;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.util.browser.BrowserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JshellAction extends BaseAction {

  private NotificationManager notificationManager;

  private static final Logger LOG = LoggerFactory.getLogger(JshellAction.class);
  private final AppContext appContext;
  private String cachedAgentUrl;

  @Inject
  public JshellAction(AppContext appContext) {

    super("Charting", "Charting");
    this.appContext = appContext;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    /* FIXME: get the dynamic port otherwise. */

    String wsUrl = appContext.getWsAgentServerApiEndpoint();
    String wsUrl_m = wsUrl.replace("/api", "");
    String[] wsUrl_mt = wsUrl_m.split(":");
    Integer port = Integer.parseInt(wsUrl_mt[2]);
    port = port - 5;
    String chartingUrl = wsUrl_mt[0] + ":" + wsUrl_mt[1] + ":" + Integer.toString(port);

    BrowserUtils.openInNewTab(chartingUrl);
  }
}
