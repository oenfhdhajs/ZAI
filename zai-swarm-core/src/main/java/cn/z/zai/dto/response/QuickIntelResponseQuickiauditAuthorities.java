package cn.z.zai.dto.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class QuickIntelResponseQuickiauditAuthorities implements Serializable {


    private static final long serialVersionUID = 964752461631854702L;
    private String full_Authority;

    private String mint_Authority;

    private String freeze_Authority;

    private String update_Authority;

    private String withdraw_Authority;

    private String fee_Authority;

}
