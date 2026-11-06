package com.uniajc.vista;

import java.util.List;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VistaEstudianteFX extends Application {

    private final ControladorEstudiante controlador =
        new ControladorEstudiante(new EstudianteService(new EstudianteDao()));

    private final ObservableList<Estudiante> datos = FXCollections.observableArrayList();
    private final TableView<Estudiante> tabla = new TableView<>(datos);
    private final TextField txtBuscar = new TextField();
    private final TextField txtNombre = new TextField();
    private final TextField txtApellido = new TextField();
    private final TextField txtEmail = new TextField();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gestión de Estudiantes (JavaFX)");

        TableColumn<Estudiante, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Estudiante, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Estudiante, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Estudiante, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tabla.getColumns().add(colId);
        tabla.getColumns().add(colNombre);
        tabla.getColumns().add(colApellido);
        tabla.getColumns().add(colEmail);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Al seleccionar una fila se llena el formulario
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, sel) -> {
            if (sel != null) {
                txtNombre.setText(sel.getNombre());
                txtApellido.setText(sel.getApellido());
                txtEmail.setText(sel.getEmail());
            }
        });

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> cargar());
        txtBuscar.setPromptText("Nombre, apellido, email o ID");
        HBox panelBusqueda = new HBox(10, new Label("Buscar:"), txtBuscar, btnBuscar);
        panelBusqueda.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.addRow(0, new Label("Nombre:"), txtNombre);
        form.addRow(1, new Label("Apellido:"), txtApellido);
        form.addRow(2, new Label("Email:"), txtEmail);

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setOnAction(e -> registrar());
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> actualizar());
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminar());
        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setOnAction(e -> limpiar());
        HBox botones = new HBox(10, btnRegistrar, btnActualizar, btnEliminar, btnLimpiar);
        botones.setPadding(new Insets(0, 10, 10, 10));

        VBox abajo = new VBox(form, botones);

        BorderPane raiz = new BorderPane();
        raiz.setTop(panelBusqueda);
        raiz.setCenter(tabla);
        raiz.setBottom(abajo);

        stage.setScene(new Scene(raiz, 700, 500));
        stage.show();

        cargar();
    }

    private void cargar() {
        datos.clear();
        String filtro = txtBuscar.getText().trim().toLowerCase();
        try {
            List<Estudiante> lista = controlador.listar();
            for (Estudiante e : lista) {
                boolean coincide = filtro.isEmpty()
                    || String.valueOf(e.getId()).contains(filtro)
                    || e.getNombre().toLowerCase().contains(filtro)
                    || e.getApellido().toLowerCase().contains(filtro)
                    || e.getEmail().toLowerCase().contains(filtro);
                if (coincide) {
                    datos.add(e);
                }
            }
        } catch (RuntimeException ex) {
            mostrar(AlertType.ERROR, "Error", ex.getMessage());
        }
    }

    private void registrar() {
        String error = controlador.registrar(
            txtNombre.getText(), txtApellido.getText(), txtEmail.getText());
        resultado(error, "Estudiante registrado.");
    }

    private void actualizar() {
        Estudiante sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrar(AlertType.WARNING, "Aviso", "Seleccione una fila.");
            return;
        }
        String error = controlador.actualizar(
            sel.getId(), txtNombre.getText(), txtApellido.getText(), txtEmail.getText());
        resultado(error, "Estudiante actualizado.");
    }

    private void eliminar() {
        Estudiante sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrar(AlertType.WARNING, "Aviso", "Seleccione una fila.");
            return;
        }
        Alert confirmar = new Alert(AlertType.CONFIRMATION,
            "¿Eliminar al estudiante con ID " + sel.getId() + "?");
        confirmar.setTitle("Confirmar");
        confirmar.setHeaderText(null);
        if (confirmar.showAndWait().orElse(null) == ButtonType.OK) {
            resultado(controlador.eliminar(sel.getId()), "Estudiante eliminado.");
        }
    }

    private void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        tabla.getSelectionModel().clearSelection();
    }

    private void resultado(String error, String exito) {
        if (error == null) {
            mostrar(AlertType.INFORMATION, "Éxito", exito);
            limpiar();
            cargar();
        } else {
            mostrar(AlertType.ERROR, "Error", error);
        }
    }

    private void mostrar(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}