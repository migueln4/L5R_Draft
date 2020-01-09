package cards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DynastyCard extends Card implements Cloneable {

    private List<String> elements;
    private Boolean character;

    public DynastyCard() {
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
                ", ID: " + super.getId() +
                ", Limit: " + super.getDeckLimit() + "]" +
                ", Restricted: " + this.getIsRestricted() +
                "---> " + super.getQuantity() + " copies";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof DynastyCard))
            return false;
        DynastyCard card = (DynastyCard) o;
        return card.getIdFiveRingsDB().equals(this.getIdFiveRingsDB());
    }
}
