package com.xlx.api.common;

import java.util.List;

/**
 * 分页查询结果包装类。
 * <p>封装分页数据，包含记录列表、总条数、当前页码和每页大小。</p>
 *
 * @param <T> 记录数据类型
 */
public class PageResult<T> {

    /** 当前页数据列表 */
    private List<T> records;
    /** 总记录数 */
    private long total;
    /** 当前页码 */
    private long page;
    /** 每页大小 */
    private long size;

    public PageResult(List<T> records, long total, long page, long size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public long getPage() { return page; }
    public void setPage(long page) { this.page = page; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
}
