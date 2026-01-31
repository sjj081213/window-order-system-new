package com.window.system.model.req.brand;

import cn.hutool.json.JSONUtil;
import com.window.system.model.req.BasePageReq;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BrandListReq extends BasePageReq {
    private String name;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
