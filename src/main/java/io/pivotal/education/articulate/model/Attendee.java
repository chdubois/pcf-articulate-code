package io.pivotal.education.articulate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
public class Attendee {

  private Long id;

  private String firstName, lastName;

  private String address, city, state, zipCode;
  private String phoneNumber, emailAddress;
  
  

}
