package org.jwebconsole.client.application.content.summary;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.jwebconsole.client.bundle.AppResources;

import javax.inject.Inject;


public class SummaryViewImpl extends ViewWithUiHandlers<SummaryUiHandlers> implements SummaryView {
    private final AppResources appResources;

    @UiField
    Label nameLabel;

    @UiField
    Label versionLabel;

    @UiField
    Label architectureLabel;

    @UiField
    Label systemLoadAverageLabel;

    @UiField
    Label availableProcessorsLabel;

    interface Binder extends UiBinder<Widget, SummaryViewImpl> {
    }

    @Inject
    SummaryViewImpl(Binder uiBinder, AppResources appResources) {
        this.appResources = appResources;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setName(String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setAvailableProcessors(Integer availableProcessors) {
        availableProcessorsLabel.setText(availableProcessors.toString());
    }

    @Override
    public void setSystemLoadAverage(Double systemLoadAverage) {
        systemLoadAverageLabel.setText(systemLoadAverage.toString());
    }

    @Override
    public void setArchitecture(String architecture) {
        architectureLabel.setText(architecture);
    }

    @Override
    public void setVersion(String version) {
        versionLabel.setText(version);
    }
}
