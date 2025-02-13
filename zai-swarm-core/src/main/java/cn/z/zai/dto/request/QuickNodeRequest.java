package cn.z.zai.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeRequest {

  private String jsonrpc = "2.0";

  private Integer id = 1;

  private String method;

  private List<Object> params;
}
