package fun.junjie.ftgo.common;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Address {
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
}
