package aad.assignment.strokeassistant.model;

/**
 * Created by JohnSiah on 17/12/2017.
 */

public class PredefinedText {
    private int id;
    private String message;

    public PredefinedText(int id,
                          String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
