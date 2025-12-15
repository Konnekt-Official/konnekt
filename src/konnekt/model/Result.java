/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package konnekt.model;

/**
 *
 * @author Hp
 */
public class Result {
    private boolean success;
    private String description;
    
    public Result() {};
    
    public Result(boolean success, String description) {
        this.success = success;
        this.description = description;
    }
    
    public boolean getSuccess() { return success; };
    public void setSuccess(boolean success) { this.success = success; };
    
    public String getDescription() { return description; };
    public void setDescription(String description) { this.description = description; };
}
