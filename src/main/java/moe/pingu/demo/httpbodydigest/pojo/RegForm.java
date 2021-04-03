package moe.pingu.demo.httpbodydigest.pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegForm implements Serializable {
  private static final long serialVersionUID = 5136280763323449287L;
  private String username;
  private String email;
}
