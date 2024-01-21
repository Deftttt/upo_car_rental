package com.upo.springtest.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageUtils {

    public static void addPagesToModelAndView(ModelAndView mav, Page<?> page, String pageName) {
        mav.addObject(pageName, page);

        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            mav.addObject("pageNumbers", pageNumbers);
        }
    }


    public static <T> List<T> getPageContent(List<T> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<T> filteredList;

        if (list.size() < startItem) {
            filteredList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            filteredList = list.subList(startItem, toIndex);
        }

        return filteredList;
    }

}
