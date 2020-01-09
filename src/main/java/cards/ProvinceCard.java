package cards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProvinceCard extends Card implements Cloneable {

    private List<String> elements;

    public ProvinceCard() {
        this.elements = new ArrayList<>();
    }

    @Override
    public Object clone() throws
            CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[Name: " + super.getName() +
                ", Clan: " + super.getClan() +
                ", Element: " + this.elements +
                ", Restrictions: " + super.getElementLimit() + "/" + super.getRoleLimit() +
                ", Restricted: " + this.getIsRestricted() +
                ", ID: " + super.getId() + "] ---> " + this.getQuantity() + " copies.";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof ProvinceCard))
            return false;
        ProvinceCard card = (ProvinceCard) o;
        return card.getIdFiveRingsDB().equals(this.getIdFiveRingsDB());
    }

}
