package com.example.demo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Table;

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
@ApiModel(value = "文件列表")
@Table(name = "f_file")
public class File {


	@ApiModelProperty(value="文件名称")
	private String fileName;
	@ApiModelProperty(value="文件后缀")
	private String fileSuffix;
	@ApiModelProperty(value="文件路径")
	private String url;

}
