package org.jwebconsole.client.application.left;

import com.gwtplatform.mvp.client.UiHandlers;
import org.jwebconsole.client.model.host.HostConnection;

public interface AvailableHostsUiHandlers extends UiHandlers {

    void onTreeItemSelected(HostConnection connection);

}
