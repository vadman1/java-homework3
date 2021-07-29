import java.util.List;

public class Companies {
    List<Company> companies;
}

class Company {
    public int id;
    public String name;
    public String address;
    public String phoneNumber;
    public String INN;
    public String founded;
    public List<Securities> securities;

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", INN='" + INN + '\'' +
                ", founded='" + founded + '\'' +
                ", securities=" + securities +
                '}';
    }
}

class Securities {
    public String name;
    public List<String> currency;
    public String code;
    public String date;

    @Override
    public String toString() {
        return "Securities{" +
                "name='" + name + '\'' +
                ", currency=" + currency +
                ", code='" + code + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
