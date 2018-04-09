package org.ghooks.isky.commons.page;

/**
 * 分页对象
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class Page {

    private int pageNo;
    private int pageSize;

    public Page() {
        this.pageNo = 1;
        this.pageSize = 20;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取数据的起始值
     *
     * @return
     */
    public int getStart() {
        return (getPageNo() - 1) * getPageSize();
    }

    /**
     * 获取数据结束值
     *
     * @return
     */
    public int getEnd() {
        return getStart() + getPageSize();
    }
}
