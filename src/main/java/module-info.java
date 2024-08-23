module com.jook.dpsmeter {
    requires javafx.controls;
    requires javafx.fxml;
    requires ini4j;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires org.apache.logging.log4j;


    opens com.jook.dpsmeter to javafx.fxml;
    opens com.jook.dpsmeter.controllers to javafx.fxml;
    exports com.jook.dpsmeter;

}