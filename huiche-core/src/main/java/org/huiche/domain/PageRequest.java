package org.huiche.domain;

/**
 * @author Maning
 */
public class PageRequest implements Pageable {
    private Long page;
    private Long size;

    public Long getPage() {
        return page();
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size();
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }

    @Override
    public long page() {
        return page == null ? 1L : page;
    }

    @Override
    public long size() {
        return size == null ? 10L : size;
    }
}
