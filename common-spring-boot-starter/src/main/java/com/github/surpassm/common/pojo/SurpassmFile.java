package com.github.surpassm.common.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * @author mc
 * Create date 2019/1/4 11:32
 * Version 1.0
 * Description
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "文件类")
public class SurpassmFile {

	/**
	 * 文件名称
	 */
	@ApiModelProperty(hidden = true)
	private String fileName;
	/**
	 * 文件后缀
	 */
	@ApiModelProperty(hidden = true)
	private String fileSuffix;
	/**
	 * 文件路径
	 */
	@ApiModelProperty(hidden = true)
	private String url;

}
