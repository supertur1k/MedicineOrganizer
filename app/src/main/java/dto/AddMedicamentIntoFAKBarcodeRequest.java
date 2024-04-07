package dto;

public class AddMedicamentIntoFAKBarcodeRequest {
    private Long id;
    private String barcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public AddMedicamentIntoFAKBarcodeRequest(Long id, String barcode) {
        this.id = id;
        this.barcode = barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
