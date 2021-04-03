package moe.pingu.demo.httpbodydigest.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegResult implements Serializable {
  private static final long serialVersionUID = 5136280763323449287L;
  private String status;
}
