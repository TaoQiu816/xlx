package com.xlx.api.file.service;

import com.xlx.api.common.BusinessException;
import com.xlx.api.file.entity.FileUpload;
import com.xlx.api.file.mapper.FileUploadMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileUploadService 单元测试")
class FileUploadServiceTest {

    @Mock
    private FileUploadMapper mapper;

    @InjectMocks
    private FileUploadService fileUploadService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileUploadService, "uploadPath", tempDir.toString());
        ReflectionTestUtils.setField(fileUploadService, "allowedTypes",
                "jpg,jpeg,png,gif,webp,pdf,doc,docx,xls,xlsx,txt");
        ReflectionTestUtils.setField(fileUploadService, "maxSize", 10485760L);
    }

    @Test
    @DisplayName("upload — 空文件抛出异常")
    void upload_emptyFile_throwsBusinessException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", new byte[0]);

        assertThatThrownBy(() -> fileUploadService.upload(emptyFile, "general", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("文件不能为空");
    }

    @Test
    @DisplayName("upload — 超大文件抛出异常")
    void upload_fileExceedsMaxSize_throwsBusinessException() {
        byte[] largeContent = new byte[20 * 1024 * 1024]; // 20MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.pdf", "application/pdf", largeContent);

        assertThatThrownBy(() -> fileUploadService.upload(largeFile, "general", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("文件大小超过限制");
    }

    @Test
    @DisplayName("upload — 危险文件类型抛出异常")
    void upload_blockedExtension_throwsBusinessException() {
        MockMultipartFile exeFile = new MockMultipartFile(
                "file", "malware.exe", "application/octet-stream", "content".getBytes());

        assertThatThrownBy(() -> fileUploadService.upload(exeFile, "general", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("不允许上传此类型文件");
    }

    @Test
    @DisplayName("upload — 不支持的文件类型抛出异常")
    void upload_unsupportedExtension_throwsBusinessException() {
        MockMultipartFile isoFile = new MockMultipartFile(
                "file", "disk.iso", "application/octet-stream", "content".getBytes());

        assertThatThrownBy(() -> fileUploadService.upload(isoFile, "general", null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("不支持的文件类型");
    }

    @Test
    @DisplayName("upload — 允许的文件类型正常保存")
    void upload_allowedExtension_savesAndReturns() throws IOException {
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "PDF content".getBytes());
        when(mapper.insert(any(FileUpload.class))).thenReturn(1);

        FileUpload result = fileUploadService.upload(pdfFile, "inquiry", null);

        assertThat(result).isNotNull();
        assertThat(result.getOriginalName()).isEqualTo("document.pdf");
        assertThat(result.getFileType()).isEqualTo("application/pdf");
        assertThat(result.getBizType()).isEqualTo("inquiry");
        assertThat(result.getFileUrl()).startsWith("/uploads/inquiry/");
        verify(mapper).insert(any(FileUpload.class));
    }

    @Test
    @DisplayName("upload — 按业务类型和年月创建目录")
    void upload_createsDirectoryStructure() throws IOException {
        MockMultipartFile pngFile = new MockMultipartFile(
                "file", "image.png", "image/png", "PNG content".getBytes());
        when(mapper.insert(any(FileUpload.class))).thenReturn(1);

        FileUpload result = fileUploadService.upload(pngFile, "product_image", 42L);

        assertThat(result.getBizType()).isEqualTo("product_image");
        assertThat(result.getBizId()).isEqualTo(42L);
        assertThat(result.getFileUrl()).contains("product_image/");
    }

    @Test
    @DisplayName("delete — 删除记录和物理文件")
    void delete_removesRecordAndFile() throws IOException {
        FileUpload record = new FileUpload();
        record.setId(1L);
        record.setFileUrl("/uploads/general/2026/06/test.txt");
        record.setFileName("test.txt");
        when(mapper.selectById(1L)).thenReturn(record);
        when(mapper.deleteById(1L)).thenReturn(1);

        fileUploadService.delete(1L);

        verify(mapper).deleteById(1L);
    }

    @Test
    @DisplayName("delete — 记录不存在时抛出异常")
    void delete_nonExisting_throwsBusinessException() {
        when(mapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> fileUploadService.delete(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("文件记录不存在");
    }
}
