package fr.insy2s.commerce.shoponlineback.exceptions.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends Throwable {

    private  int status;
    private  String message;
    private  String error;

    private  String path;

    private  String timestamp;

    public ErrorResponse(String message, String error){
        this.message = message;
        this.error = error;
    }
}
