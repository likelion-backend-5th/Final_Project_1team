package mutsa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomPage<T> implements Serializable {
    public List<T> content;
    public CustomPageable pageable;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(), page.getTotalElements()
                , page.getTotalPages(), page.getNumberOfElements());
    }

    @Data
    @AllArgsConstructor
    public class CustomPageable {
        public int pageNumber;
        public int pageSize;
        public long totalElements;
        public long totalPages;
        public long numberOfElements;
    }
}