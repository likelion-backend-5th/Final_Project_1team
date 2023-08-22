package mutsa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomPage<T> implements Serializable {
    private List<T> content;
    private CustomPageable pageable;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(), page.getTotalElements()
                , page.getTotalPages(), page.getNumberOfElements());
    }

    @Data
    @AllArgsConstructor
    public class CustomPageable {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private long totalPages;
        private long numberOfElements;
    }
}