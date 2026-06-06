package com.xlx.api.inquiry.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlx.api.common.BusinessException;
import com.xlx.api.common.PageResult;
import com.xlx.api.inquiry.entity.Inquiry;
import com.xlx.api.inquiry.entity.InquiryItem;
import com.xlx.api.inquiry.mapper.InquiryItemMapper;
import com.xlx.api.inquiry.mapper.InquiryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InquiryService 单元测试")
class InquiryServiceTest {

    @Mock
    private InquiryMapper mapper;

    @Mock
    private InquiryItemMapper inquiryItemMapper;

    private InquiryService inquiryService;

    private InquiryItemService inquiryItemService;

    private Inquiry sampleInquiry;

    @BeforeEach
    void setUp() {
        inquiryItemService = new InquiryItemService(inquiryItemMapper);
        inquiryService = new InquiryService(mapper, inquiryItemService);
        sampleInquiry = new Inquiry();
        sampleInquiry.setId(1L);
        sampleInquiry.setName("张三");
        sampleInquiry.setCompany("测试公司");
        sampleInquiry.setEmail("test@example.com");
        sampleInquiry.setStatus("new");
    }

    @Test
    @DisplayName("list — 返回分页结果")
    void list_returnsPaginatedResults() {
        Page<Inquiry> page = new Page<>(1, 10);
        page.setRecords(List.of(sampleInquiry));
        page.setTotal(1);
        when(mapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<Inquiry> result = inquiryService.list(1, 10, null, null, null, null);

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
        verify(mapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("list — 按状态筛选")
    void list_withStatusFilter_filtersByStatus() {
        Page<Inquiry> page = new Page<>(1, 10);
        page.setRecords(List.of(sampleInquiry));
        page.setTotal(1);
        when(mapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        inquiryService.list(1, 10, "new", null, null, null);

        verify(mapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("list — 按关键词搜索")
    void list_withKeyword_searchesByNameAndCompany() {
        Page<Inquiry> page = new Page<>(1, 10);
        page.setRecords(List.of(sampleInquiry));
        page.setTotal(1);
        when(mapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<Inquiry> result = inquiryService.list(1, 10, null, "张三", null, null);

        assertThat(result.getRecords()).hasSize(1);
        verify(mapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("list — 按日期范围筛选")
    void list_withDateRange_filtersByCreatedAt() {
        Page<Inquiry> page = new Page<>(1, 10);
        page.setRecords(List.of(sampleInquiry));
        page.setTotal(1);
        when(mapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<Inquiry> result = inquiryService.list(1, 10, null, null, "2026-06-01", "2026-06-05");

        assertThat(result.getRecords()).hasSize(1);
        verify(mapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("getById — 存在时返回询盘")
    void getById_existingInquiry_returnsInquiry() {
        when(mapper.selectById(1L)).thenReturn(sampleInquiry);

        Inquiry result = inquiryService.getById(1L);

        assertThat(result.getName()).isEqualTo("张三");
        assertThat(result.getCompany()).isEqualTo("测试公司");
    }

    @Test
    @DisplayName("getById — 不存在时抛出异常")
    void getById_nonExisting_throwsBusinessException() {
        when(mapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> inquiryService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("询盘不存在");
    }

    @Test
    @DisplayName("create — 设置默认状态为 new")
    void create_setsStatusToNew() {
        Inquiry newInquiry = new Inquiry();
        newInquiry.setName("李四");
        when(mapper.insert(any(Inquiry.class))).thenReturn(1);

        inquiryService.create(newInquiry);

        assertThat(newInquiry.getStatus()).isEqualTo("new");
        verify(mapper).insert(newInquiry);
    }

    @Test
    @DisplayName("updateStatus — 有效状态正常保存")
    void updateStatus_validStatus_persists() {
        when(mapper.selectById(1L)).thenReturn(sampleInquiry);
        when(mapper.updateById(any(Inquiry.class))).thenReturn(1);

        inquiryService.updateStatus(1L, "contacted");

        assertThat(sampleInquiry.getStatus()).isEqualTo("contacted");
        verify(mapper).updateById(sampleInquiry);
    }

    @Test
    @DisplayName("updateStatus — 无效状态抛出异常")
    void updateStatus_invalidStatus_throwsBusinessException() {
        assertThatThrownBy(() -> inquiryService.updateStatus(1L, "hacked"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("无效的询盘状态");
    }

    @Test
    @DisplayName("updateStatus — 所有有效状态均可通过")
    void updateStatus_allValidStatuses_pass() {
        String[] validStatuses = {"new", "contacted", "quoted", "sample_confirmed",
                "order_confirmed", "closed", "invalid"};

        for (String status : validStatuses) {
            when(mapper.selectById(1L)).thenReturn(sampleInquiry);
            when(mapper.updateById(any(Inquiry.class))).thenReturn(1);

            inquiryService.updateStatus(1L, status);

            assertThat(sampleInquiry.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("updateRemark — 更新备注内容")
    void updateRemark_updatesRemark() {
        when(mapper.selectById(1L)).thenReturn(sampleInquiry);
        when(mapper.updateById(any(Inquiry.class))).thenReturn(1);

        inquiryService.updateRemark(1L, "已联系客户");

        assertThat(sampleInquiry.getRemark()).isEqualTo("已联系客户");
        verify(mapper).updateById(sampleInquiry);
    }

    @Test
    @DisplayName("delete — 删除询盘")
    void delete_deletesInquiry() {
        when(mapper.deleteById(1L)).thenReturn(1);

        inquiryService.delete(1L);

        verify(mapper).deleteById(1L);
    }

    @Test
    @DisplayName("createWithItems — 创建多产品询盘")
    void createWithItems_createsInquiryAndItems() {
        Inquiry newInquiry = new Inquiry();
        newInquiry.setName("John");
        InquiryItem item1 = new InquiryItem();
        item1.setProductId(1L);
        item1.setProductNameCn("镀锌铁丝");
        InquiryItem item2 = new InquiryItem();
        item2.setProductId(2L);
        item2.setProductNameCn("电焊网片");
        when(mapper.insert(any(Inquiry.class))).thenReturn(1);
        when(inquiryItemMapper.insert(any(InquiryItem.class))).thenReturn(1);

        Inquiry result = inquiryService.createWithItems(newInquiry, List.of(item1, item2));

        assertThat(newInquiry.getStatus()).isEqualTo("new");
        assertThat(newInquiry.getProductId()).isNull();
        verify(mapper).insert(newInquiry);
        verify(inquiryItemMapper, times(2)).insert(any(InquiryItem.class));
    }

    @Test
    @DisplayName("createWithItems — 空 items 只创建主记录")
    void createWithItems_emptyItems_createsInquiryOnly() {
        Inquiry newInquiry = new Inquiry();
        newInquiry.setName("John");
        when(mapper.insert(any(Inquiry.class))).thenReturn(1);

        Inquiry result = inquiryService.createWithItems(newInquiry, List.of());

        verify(mapper).insert(newInquiry);
        verify(inquiryItemMapper, never()).insert(any(InquiryItem.class));
    }

    @Test
    @DisplayName("getItems — 返回询盘明细列表")
    void getItems_returnsItemList() {
        InquiryItem item = new InquiryItem();
        item.setInquiryId(1L);
        when(inquiryItemMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(item));

        List<InquiryItem> items = inquiryService.getItems(1L);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getInquiryId()).isEqualTo(1L);
    }
}
