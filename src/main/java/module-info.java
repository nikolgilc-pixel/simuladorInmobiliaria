module co.edu.uniquindio.poo.simuladorinmobiliaria {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.poo.simuladorinmobiliaria to javafx.fxml;
    exports co.edu.uniquindio.poo.simuladorinmobiliaria;
}