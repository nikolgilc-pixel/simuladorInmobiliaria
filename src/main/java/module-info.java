module co.edu.uniquindio.poo.simuladorinmobiliaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires twilio;
    requires java.desktop;
    requires org.apache.httpcomponents.httpclient;

    opens co.edu.uniquindio.poo.simuladorinmobiliaria to javafx.fxml;
    opens co.edu.uniquindio.poo.simuladorinmobiliaria.model to javafx.fxml;
    opens co.edu.uniquindio.poo.simuladorinmobiliaria.controller to javafx.fxml;
    opens co.edu.uniquindio.poo.simuladorinmobiliaria.viewController to javafx.fxml;

    exports co.edu.uniquindio.poo.simuladorinmobiliaria;
    exports co.edu.uniquindio.poo.simuladorinmobiliaria.model;
    exports co.edu.uniquindio.poo.simuladorinmobiliaria.controller;
    exports co.edu.uniquindio.poo.simuladorinmobiliaria.viewController;
}
