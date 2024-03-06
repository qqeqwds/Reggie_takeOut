package com.heu.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 */
@ApiModel("套餐")
@Data
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("套餐分类ID")
    //分类id
    private Long categoryId;

    @ApiModelProperty("套餐名称")
    //套餐名称
    private String name;

    @ApiModelProperty("套餐价格")
    //套餐价格
    private BigDecimal price;

    @ApiModelProperty("套餐状态")
    //状态 0:停用 1:启用
    private Integer status;

    @ApiModelProperty("套餐编号")
    //编码
    private String code;

    @ApiModelProperty("套餐描述信息")
    //描述信息
    private String description;

    @ApiModelProperty("套餐图片地址")
    //图片
    private String image;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
