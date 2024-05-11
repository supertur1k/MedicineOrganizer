package dto;

public class AddMedIntoFirstAidKitDTO {
    private Long id;
    private String nameOfTheMedicament;
    private String description;
    private String amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfTheMedicament() {
        return nameOfTheMedicament;
    }

    public AddMedIntoFirstAidKitDTO(Long id, String nameOfTheMedicament, String description, String amount) {
        this.id = id;
        this.nameOfTheMedicament = nameOfTheMedicament;
        this.description = description;
        this.amount = amount;
    }

    public void setNameOfTheMedicament(String nameOfTheMedicament) {
        this.nameOfTheMedicament = nameOfTheMedicament;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
