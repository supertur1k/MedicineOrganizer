package dto;

import java.util.List;

public class ListFaksDto {
    public List<Long> getFirst_aid_kit_ids() {
        return first_aid_kit_ids;
    }

    public ListFaksDto(List<Long> first_aid_kit_ids) {
        this.first_aid_kit_ids = first_aid_kit_ids;
    }

    public void setFirst_aid_kit_ids(List<Long> first_aid_kit_ids) {
        this.first_aid_kit_ids = first_aid_kit_ids;
    }

    List<Long> first_aid_kit_ids;
}
