package projet;

import java.util.List;

public class TravelErrors {
    private Travel travel;
    private List<String> errors;

    public TravelErrors(Travel travel, List<String> errors) {
        this.travel = travel;
        this.errors = errors;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

