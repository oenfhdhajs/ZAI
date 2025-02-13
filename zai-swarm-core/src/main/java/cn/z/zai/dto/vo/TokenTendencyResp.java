package cn.z.zai.dto.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class TokenTendencyResp implements Serializable {

    private static final long serialVersionUID = -6711677456022839478L;

    private List<TokenTendencyMaxVo> live = new ArrayList<>();

    private List<TokenTendencyMaxVo> fourHours = new ArrayList<>();

    private List<TokenTendencyMaxVo> oneDay = new ArrayList<>();

    private List<TokenTendencyMaxVo> oneWeek = new ArrayList<>();

    private List<TokenTendencyMaxVo> oneMonth = new ArrayList<>();

    private List<TokenTendencyMaxVo> oneYear = new ArrayList<>();

    private List<TokenTendencyMaxVo> max = new ArrayList<>();

}
