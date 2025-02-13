package cn.z.zai.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponse<T> implements Serializable {

      private String jsonrpc;

      private T result;

      private Integer id;

      private QuickNodeResponseError error;
}
