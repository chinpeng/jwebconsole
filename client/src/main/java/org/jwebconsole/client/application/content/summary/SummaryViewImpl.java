package org.jwebconsole.client.application.content.summary;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

import javax.inject.Inject;


public class SummaryViewImpl extends ViewWithUiHandlers<SummaryUiHandlers> implements SummaryView {
    @UiField
    PortalLayoutContainer portal;

    @UiField
    Label nameLabel;
    @UiField
    Label versionLabel;
    @UiField
    Label architectureLabel;
//    @UiField
//    Label systemLoadAverageLabel;
    @UiField
    Label availableProcessorsLabel;
    @UiField
    Label processCPUTimeLabel;
    @UiField
    Label committedVirtualMemorySizeLabel;
    @UiField
    Label totalPhysicalMemorySizeLabel;
    @UiField
    Label freePhysicalMemorySizeLabel;
    @UiField
    Label totalSwapSpaceSizeLabel;
    @UiField
    Label freeSwapSpaceSizeLabel;

    private NumberFormat fmt = NumberFormat.getDecimalFormat();

    interface Binder extends UiBinder<Widget, SummaryViewImpl> {
    }

    @Inject
    SummaryViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        initPortal();
    }

    private void initPortal() {
        portal.setColumnWidth(0, 1);
    }

    @Override
    public void setName(String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setAvailableProcessors(Integer availableProcessors) {
        availableProcessorsLabel.setText(availableProcessors.toString());
    }

//    @Override
//    public void setSystemLoadAverage(Double systemLoadAverage) {
//        systemLoadAverageLabel.setText(systemLoadAverage.toString());
//    }

    @Override
    public void setArchitecture(String architecture) {
        architectureLabel.setText(architecture);
    }

    @Override
    public void setVersion(String version) {
        versionLabel.setText(version);
    }

    @Override
    public void setProcessCPUTime(Long processCPUTime) {
        processCPUTimeLabel.setText(fmt.format(processCPUTime));
    }

    @Override
    public void setCommittedVirtualMemorySize(Long committedVirtualMemorySize) {
        committedVirtualMemorySizeLabel.setText(fmt.format(committedVirtualMemorySize));
    }

    @Override
    public void setTotalPhysicalMemorySize(Long totalPhysicalMemorySize) {
        totalPhysicalMemorySizeLabel.setText(fmt.format(totalPhysicalMemorySize));
    }

    @Override
    public void setFreePhysicalMemorySize(Long freePhysicalMemorySize) {
        freePhysicalMemorySizeLabel.setText(fmt.format(freePhysicalMemorySize));
    }

    @Override
    public void setTotalSwapSpaceSize(Long totalSwapSpaceSize) {
        totalSwapSpaceSizeLabel.setText(fmt.format(totalSwapSpaceSize));
    }

    @Override
    public void setFreeSwapSpaceSize(Long freeSwapSpaceSize) {
        freeSwapSpaceSizeLabel.setText(fmt.format(freeSwapSpaceSize));
    }
}
