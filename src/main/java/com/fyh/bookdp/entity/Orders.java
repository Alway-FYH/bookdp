package com.fyh.bookdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Orders implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 用户主键
     */
      private Integer userId;

      /**
     * 用户名
     */
      private String loginName;

      /**
     * 用户地址
     */
      private String userAddress;

      /**
     * 总金额
     */
      private Float cost;

      /**
     * 订单号
     */
      private String serialnumber;

      private Integer pay;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;

  /**
   * 发货状态
   */
  private Integer state;
}
