package dto;

public class Medicament {
    private Long id;
    private String name;
    private String releaseForm;
    private String amount;
    private String directionsForUse;

    public Medicament(Long id, String name, String releaseForm, String amount, String directionsForUse, String indicationsForUse, String contraindications, String barcode) {
        this.id = id;
        this.name = name;
        this.releaseForm = releaseForm;
        this.amount = amount;
        this.directionsForUse = directionsForUse;
        this.indicationsForUse = indicationsForUse;
        this.contraindications = contraindications;
        this.barcode = barcode;
    }

    private String indicationsForUse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseForm() {
        return releaseForm;
    }

    public void setReleaseForm(String releaseForm) {
        this.releaseForm = releaseForm;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDirectionsForUse() {
        return directionsForUse;
    }

    public void setDirectionsForUse(String directionsForUse) {
        this.directionsForUse = directionsForUse;
    }

    public String getIndicationsForUse() {
        return indicationsForUse;
    }

    public void setIndicationsForUse(String indicationsForUse) {
        this.indicationsForUse = indicationsForUse;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String contraindications;
    private String barcode;
}
