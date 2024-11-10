package org.vaadin.example.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.security.SecurityUtils;
import org.vaadin.example.ui.views.dashboard.DashboardView;
import org.vaadin.example.ui.views.list.ListView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        this.createHeader();
        this.createDrawer();
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("List", ListView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Dashboard", DashboardView.class)));
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassName("logo");

        Anchor logOut = new Anchor("/logout", "Log out");
        logOut.getElement().addEventListener("click", event -> {
            SecurityUtils.logout();
        });
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logOut);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }
}
