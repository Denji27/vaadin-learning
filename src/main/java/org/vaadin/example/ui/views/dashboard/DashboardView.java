package org.vaadin.example.ui.views.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.example.backend.service.CompanyService;
import org.vaadin.example.backend.service.ContactService;
import org.vaadin.example.ui.MainLayout;

import java.util.Map;

@PageTitle("Dashboard | Vaadin CRM")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
    private final ContactService contactService;
    private final CompanyService companyService;

    public DashboardView(
            ContactService contactService,
            CompanyService companyService) {
        this.contactService = contactService;
        this.companyService = companyService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(this.getContactStat(), this.getCompanyChart());
    }

    private Component getCompanyChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        Map<String, Integer> stats = this.companyService.getStats();

        stats.forEach(
                (name, number) -> {
                    dataSeries.add(new DataSeriesItem(name, number));
                });
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

    private Span getContactStat() {
        Span stats = new Span(this.contactService.count() + " contacts");
        stats.addClassName("contact-stats");
        return stats;
    }
}
