package dto;

import java.util.Collection;
import java.util.stream.Collectors;

public class FirstAidKit {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName_of_the_first_aid_kit() {
        return name_of_the_first_aid_kit;
    }

    public FirstAidKit(Long id, String name_of_the_first_aid_kit, String description, Collection<Medicament> medicaments) {
        this.id = id;
        this.name_of_the_first_aid_kit = name_of_the_first_aid_kit;
        this.description = description;
        this.medicaments = medicaments;
    }

    public void setName_of_the_first_aid_kit(String name_of_the_first_aid_kit) {
        this.name_of_the_first_aid_kit = name_of_the_first_aid_kit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Medicament> getMedicaments() {
        return medicaments;
    }

    public Collection<Medicament> getMedicamentsFiltered(String name) {
        return medicaments.stream().filter(x->x.getName().toLowerCase().startsWith(name.toLowerCase())).collect(Collectors.toList());
    }

    public void setMedicaments(Collection<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    private Long id;
    private String name_of_the_first_aid_kit;
    private String description;
    private Collection<Medicament> medicaments;
}
