package cn.z.zai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JupiterQuoteRequest {

  private String inputMint;

  private String outputMint;

  private Integer amount;
}
