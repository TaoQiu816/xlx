package com.xlx.api.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlx.api.file.entity.FileUpload;
import org.apache.ibatis.annotations.Mapper;

/** 文件上传记录数据访问接口 */
@Mapper
public interface FileUploadMapper extends BaseMapper<FileUpload> {
}
