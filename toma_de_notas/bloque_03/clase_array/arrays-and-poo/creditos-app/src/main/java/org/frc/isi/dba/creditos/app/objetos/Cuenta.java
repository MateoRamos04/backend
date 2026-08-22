package org.frc.isi.dba.creditos.app.objetos;

import java.util.UUID;

import lombok.Data;
@Data


public class Cuenta {
    private String nombre;
    private String apellido;
    private int dni;
    private double saldo;

    public Cuenta(String nombre, String apellido, int dni, double saldo) {
        // Generación del código
        UUID guid = UUID.randomUUID();
        String guidString = guid.toString();


        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.saldo = saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
}
