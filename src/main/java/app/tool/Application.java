package app.tool;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

public class Application {

    @Getter @Setter private int applicantId;
    @Getter @Setter private LocalDate sanctionedDate;
    @Getter @Setter private double sanctionedAmount;
    @Getter @Setter private Map<String,Double> interestCalculation;

}
