package com.example.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showFirstPage;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;// current page
    private List<Integer> pages = new ArrayList<>();//page's list
    private Integer totalPage;


    public void setPagination(Integer totalCount, Integer page, Integer size) {
        pages.clear();
        //  totalCount是记录数
        //  totalPage是分页数
//        Integer totalPage = 0;
        if (totalCount % size ==0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

//        if (page < 1) { page = 1; }
//        if (page > totalPage) { page = totalPage; }
        this.page = page;
        pages.add(page);
        //计算pages里面内容(当前显示哪几页)
        for (int i=1; i<=3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //  是否展示上一页/下一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        //是否展示第一页/最后一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
